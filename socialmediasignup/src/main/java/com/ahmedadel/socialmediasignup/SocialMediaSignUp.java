package com.ahmedadel.socialmediasignup;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;

import com.ahmedadel.socialmediasignup.callback.SocialMediaSignUpCallback;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * SocialMediaSignUp is the entry point of the social media sign up library that the user will use it to sign up with
 * our social media platforms we integrated with.
 */

public class SocialMediaSignUp {

    @SuppressLint("StaticFieldLeak")
    private static final SocialMediaSignUp instance = new SocialMediaSignUp();

    private Context appContext;

    private SocialMediaData facebookSocialData;
    private SocialMediaData googleSocialData;
    private SocialMediaData twitterSocialData;
    private SocialMediaData instagramSocialData;
    private SocialMediaData linkedInSocialData;

    private ProgressDialog loadingDialog;

    private SocialMediaSignUp() {

    }

    public static synchronized SocialMediaSignUp getInstance() {
        return instance;
    }

    static void init(Context context) {
        Context appContext = context.getApplicationContext();
        getInstance().appContext = appContext;
        getInstance().initFacebook(appContext);
        getInstance().initTwitter(appContext);
    }

    private void initFacebook(Context appContext) {
        String fbAppId = SocialMediaSignUpUtils.getMetaDataValue(appContext, appContext.getString(R.string.com_ahmedadel_socialmediasignup_facebookAppId));
        if (!SocialMediaSignUpUtils.isEmpty(fbAppId)) {
            FacebookSdk.setApplicationId(fbAppId);
        }
    }

    private void initTwitter(Context appContext) {
        String consumerKey = SocialMediaSignUpUtils.getMetaDataValue(appContext, appContext.getString(R.string.com_ahmedadel_socialmediasignup_twitterConsumerKey));
        String consumerSecret = SocialMediaSignUpUtils.getMetaDataValue(appContext, appContext.getString(R.string.com_ahmedadel_socialmediasignup_twitterConsumerSecret));

        if (consumerKey != null && consumerSecret != null) {
            TwitterConfig twitterConfig = new TwitterConfig.Builder(appContext)
                    .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
                    .build();
            Twitter.initialize(twitterConfig);
        }
    }

    private void connectFacebook(@Nullable List<String> scopes, @NonNull SocialMediaSignUpCallback callback) {
        facebookSocialData = new SocialMediaData(scopes != null && !scopes.isEmpty() ?
                scopes : Arrays.asList("email", "public_profile"), callback);
        FacebookSignUpActivity.start(appContext);
    }

    private void disconnectFacebook() {
        facebookSocialData = null;
        LoginManager.getInstance().logOut();
    }

    private void connectGoogle(@Nullable List<String> scopes, @NonNull SocialMediaSignUpCallback callback) {
        googleSocialData = new SocialMediaData(scopes != null && !scopes.isEmpty() ?
                scopes : Collections.singletonList(
                "https://www.googleapis.com/auth/youtube.upload"), callback);
        GoogleSignUpActivity.start(appContext);
    }

    private void disconnectGoogle() {
        googleSocialData = null;
    }

    private void connectTwitter(@NonNull SocialMediaSignUpCallback callback) {
        twitterSocialData = new SocialMediaData(Collections.<String>emptyList(), callback);
        TwitterSignUpActivity.start(appContext);
    }

    private void disconnectTwitter() {
        twitterSocialData = null;
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        clearCookies();
    }

    private void connectInstagram(@Nullable List<String> scopes, @NonNull SocialMediaSignUpCallback callback) {
        instagramSocialData = new SocialMediaData(scopes != null && !scopes.isEmpty() ?
                scopes : Collections.singletonList("public_content"), callback);
        InstagramSignUpActivity.start(appContext);
    }

    private void disconnectInstagram() {
        instagramSocialData = null;
        clearCookies();
    }

    private void connectLinkedIn(@Nullable Scope linkedInScope, @NonNull SocialMediaSignUpCallback callback) {
        linkedInSocialData = new SocialMediaData(Collections.<String>emptyList(), callback);
        linkedInSocialData.setLinkedInScope(linkedInScope);
        LinkedInSignUpActivity.start(appContext);
    }

    private void disconnectLinkedIn() {
        LISessionManager.getInstance(appContext).clearSession();
        linkedInSocialData = null;
        clearCookies();
    }

    private void clearCookies() {
        CookieManager.getInstance().removeAllCookies(null);
    }

    SocialMediaData getFacebookData() {
        return facebookSocialData;
    }

    SocialMediaData getGoogleData() {
        return googleSocialData;
    }

    SocialMediaData getTwitterData() {
        return twitterSocialData;
    }

    SocialMediaData getInstagramData() {
        return instagramSocialData;
    }

    SocialMediaData getLinkedInData() {
        return linkedInSocialData;
    }

    public void connectTo(SocialMediaType socialMediaType, @Nullable List<String> scopes, @NonNull SocialMediaSignUpCallback callback) {
        switch (socialMediaType) {
            case FACEBOOK:
                connectFacebook(scopes, callback);
                break;
            case GOOGLE_PLUS:
                connectGoogle(scopes, callback);
                break;
            case TWITTER:
                connectTwitter(callback);
                break;
            case INSTAGRAM:
                connectInstagram(scopes, callback);
                break;
        }
    }

    public void connectToLinkedIn(@Nullable Scope linkedInScope, @NonNull SocialMediaSignUpCallback callback) {
        connectLinkedIn(linkedInScope, callback);
    }

    public void disconnectService(SocialMediaType socialMediaType, @NonNull SocialMediaSignUpCallback callback) {
        switch (socialMediaType) {
            case FACEBOOK:
                disconnectFacebook();
                callback.onSignOut(socialMediaType);
                break;
            case GOOGLE_PLUS:
                disconnectGoogle();
                break;
            case TWITTER:
                disconnectTwitter();
                callback.onSignOut(socialMediaType);
                break;
            case INSTAGRAM:
                disconnectInstagram();
                break;
            case LINKEDIN:
                disconnectLinkedIn();
                callback.onSignOut(socialMediaType);
                break;
        }
    }

    ProgressDialog getCustomLoadingDialog() {
        return loadingDialog;
    }

    public void setCustomLoadingDialog(ProgressDialog loadingDialog) {
        this.loadingDialog = loadingDialog;
    }

    public enum SocialMediaType {
        FACEBOOK("facebook"),
        GOOGLE_PLUS("google_plus"),
        TWITTER("twitter"),
        INSTAGRAM("instagram"),
        LINKEDIN("linkedin");

        private final String value;

        SocialMediaType(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}

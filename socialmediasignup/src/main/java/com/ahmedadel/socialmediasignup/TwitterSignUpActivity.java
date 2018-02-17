package com.ahmedadel.socialmediasignup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ahmedadel.socialmediasignup.model.SocialMediaUser;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import retrofit2.Call;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * TwitterSignUpActivity is the activity that handle all the logic behind twitter sdk and that will be launched
 * once the developer twitter facebook social type.
 */

public class TwitterSignUpActivity extends SocialMediaSignUpActivity {

    private static final String PROFILE_PIC_URL = "https://twitter.com/%1$s/profile_image?size=original";
    private static final String PAGE_LINK = "https://twitter.com/%1$s";

    private TwitterAuthClient twitterAuthClient;

    private Call<User> call;

    public static void start(Context context) {
        Intent intent = new Intent(context, TwitterSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterSession activeSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (activeSession != null) {
            handleSuccess(activeSession);
        } else {
            getTwitterAuthClient().authorize(this, new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    startLoading();
                    handleSuccess(result.data);
                }

                @Override
                public void failure(TwitterException exception) {
                    handleError(exception);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            handCancel(SocialMediaSignUp.SocialMediaType.TWITTER);
            return;
        }
        if (requestCode == getTwitterAuthClient().getRequestCode())
            getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected SocialMediaData getAuthData() {
        return SocialMediaSignUp.getInstance().getTwitterData();
    }

    @Override
    protected void handCancel(SocialMediaSignUp.SocialMediaType socialMediaType) {
        getTwitterAuthClient().cancelAuthorize();
        super.handCancel(socialMediaType);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (call != null)
            call.cancel();
    }

    private void handleSuccess(final TwitterSession session) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        AccountService accountService = twitterApiClient.getAccountService();
        call = accountService.verifyCredentials(false, true, true);
        call.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                SocialMediaUser user = new SocialMediaUser();
                User data = userResult.data;
                user.setUserId(String.valueOf(data.getId()));
                user.setAccessToken(session.getAuthToken().token);
                user.setProfilePictureUrl(String.format(PROFILE_PIC_URL, data.screenName));
                user.setEmail(data.email != null ? data.email : "");
                user.setFullName(data.name);
                user.setUsername(data.screenName);
                user.setPageLink(String.format(PAGE_LINK, data.screenName));
                handleSuccess(SocialMediaSignUp.SocialMediaType.TWITTER, user);
            }

            public void failure(TwitterException error) {
                handleError(error);
            }
        });
    }

    private TwitterAuthClient getTwitterAuthClient() {
        if (twitterAuthClient == null) {
            synchronized (TwitterSignUpActivity.class) {
                if (twitterAuthClient == null) {
                    twitterAuthClient = new TwitterAuthClient();
                }
            }
        }
        return twitterAuthClient;
    }
}

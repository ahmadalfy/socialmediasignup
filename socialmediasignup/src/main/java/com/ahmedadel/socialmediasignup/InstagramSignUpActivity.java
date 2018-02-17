package com.ahmedadel.socialmediasignup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ahmedadel.socialmediasignup.model.SocialMediaUser;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * InstagramSignUpActivity is the activity that handle all the logic behind instagram sdk and that will be launched
 * once the developer chooses instagram social type.
 */

public class InstagramSignUpActivity extends SocialMediaSignUpActivity {

    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/?client_id=%1$s&redirect_uri=%2$s&response_type=code&display=touch&scope=%3$s";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String PAGE_LINK = "https://www.instagram.com/%1$s/";
    private static final String INSTAGRAM_URL = "https://www.instagram.com/";

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    private OkHttpClient okHttpClient;

    public static void start(Context context) {
        Intent intent = new Intent(context, InstagramSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientId = SocialMediaSignUpUtils.getMetaDataValue(this, getString(R.string.com_ahmedadel_socialmediasignup_instagramClientId));
        clientSecret = SocialMediaSignUpUtils.getMetaDataValue(this, getString(R.string.com_ahmedadel_socialmediasignup_instagramClientSecret));
        redirectUri = SocialMediaSignUpUtils.getMetaDataValue(this, getString(R.string.com_ahmedadel_socialmediasignup_instagramRedirectUri));

        final String authUrl = String.format(AUTH_URL, clientId, redirectUri, getScopes());

        WebView webView = new WebView(this);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(authUrl);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                startLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                stopLoading();
                if (url.equals(INSTAGRAM_URL))
                    view.loadUrl(authUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(redirectUri)) {
                    startLoading();
                    getCode(Uri.parse(url));
                    return true;
                }
                return false;
            }
        });

        setContentView(webView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (okHttpClient != null)
            okHttpClient.dispatcher().cancelAll();
    }

    @Override
    protected SocialMediaData getAuthData() {
        return SocialMediaSignUp.getInstance().getInstagramData();
    }

    private void getCode(Uri uri) {
        String code = uri.getQueryParameter("code");
        if (code != null) {
            getAccessToken(code);
        } else if (uri.getQueryParameter("error") != null) {
            String errorMsg = uri.getQueryParameter("error_description");
            handleError(new Throwable(errorMsg));
        }
    }

    private void getAccessToken(String code) {
        startLoading();
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("grant_type", "authorization_code")
                .add("redirect_uri", redirectUri)
                .add("code", code)
                .build();

        Request request = new Request.Builder().post(formBody)
                .url(TOKEN_URL)
                .build();

        okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleError(e);
                    }
                });
            }

            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleError(new Throwable("Failed to get access token."));
                        }
                    });
                    return;
                }
                //noinspection ConstantConditions
                InstagramUser instagramUser = new Gson().fromJson(response.body().string(), InstagramUser.class);

                final SocialMediaUser user = new SocialMediaUser();
                user.setAccessToken(instagramUser.accessToken);
                user.setUserId(instagramUser.user.id);
                user.setUsername(instagramUser.user.username);
                user.setFullName(instagramUser.user.fullName);
                user.setPageLink(String.format(PAGE_LINK, user.getUsername()));
                user.setProfilePictureUrl(instagramUser.user.profilePicture);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        handleSuccess(SocialMediaSignUp.SocialMediaType.INSTAGRAM, user);
                    }
                });
            }
        });

    }

    public String getScopes() {
        return TextUtils.join("+", getAuthData().getScopes());
    }

    private class InstagramUser {
        @SerializedName("access_token")
        String accessToken;
        @SerializedName("user")
        User user;

        class User {
            @SerializedName("id")
            String id;
            @SerializedName("username")
            String username;
            @SerializedName("full_name")
            String fullName;
            @SerializedName("profile_picture")
            String profilePicture;
        }
    }
}

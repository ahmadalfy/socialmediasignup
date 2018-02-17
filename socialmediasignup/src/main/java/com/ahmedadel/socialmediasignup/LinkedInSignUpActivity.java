package com.ahmedadel.socialmediasignup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ahmedadel.socialmediasignup.model.SocialMediaUser;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONObject;

/**
 * Created by Ahmed Adel on 2/17/18.
 * <p>
 * LinkedInSignUpActivity is the activity that handle all the logic behind linkedin sdk and that will be launched
 * once the developer chooses linkedin social type.
 */

public class LinkedInSignUpActivity extends SocialMediaSignUpActivity {

    private static final String LINKEDIN_URL = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address)";
    private APIHelper apiHelper;

    public static void start(Context context) {
        Intent intent = new Intent(context, LinkedInSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LISessionManager.getInstance(getApplicationContext()).init(this, getScopes(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                startLoading();
                fetchPersonalInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                handleError(new Throwable(error.toString()));
            }
        }, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected SocialMediaData getAuthData() {
        return SocialMediaSignUp.getInstance().getLinkedInData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (apiHelper != null)
            apiHelper.cancelCalls(this);
    }

    private void fetchPersonalInfo() {
        apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, LINKEDIN_URL, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                SocialMediaUser socialMediaUser = new SocialMediaUser();
                socialMediaUser.setFullName(jsonObject.optString("firstName") + " " + jsonObject.optString("firstName"));
                socialMediaUser.setEmail(jsonObject.optString("emailAddress"));
                socialMediaUser.setProfilePictureUrl(jsonObject.optString("pictureUrl"));
                socialMediaUser.setAccessToken(LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue());
                handleSuccess(SocialMediaSignUp.SocialMediaType.LINKEDIN, socialMediaUser);
            }

            @Override
            public void onApiError(LIApiError error) {
                handleError(new Throwable(error.toString()));
            }
        });
    }

    private Scope getScopes() {
        return getAuthData().getLinkedInScope() != null ? getAuthData().getLinkedInScope() :
                Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
}

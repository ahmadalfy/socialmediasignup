package com.ahmedadel.socialmediasignup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ahmedadel.socialmediasignup.model.SocialMediaUser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * FacebookSignUpActivity is the activity that handle all the logic behind facebook sdk and that will be launched
 * once the developer chooses facebook social type.
 */

public class FacebookSignUpActivity extends SocialMediaSignUpActivity
        implements FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {

    private static final String PROFILE_PIC_URL = "https://graph.facebook.com/%1$s/picture?type=large";

    private CallbackManager callbackManager;

    public static void start(Context context) {
        Intent intent = new Intent(context, FacebookSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, this);
        LoginManager.getInstance().logInWithReadPermissions(this, getScopes());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected SocialMediaData getAuthData() {
        return SocialMediaSignUp.getInstance().getFacebookData();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        startLoading();
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {
        handCancel(SocialMediaSignUp.SocialMediaType.FACEBOOK);
    }

    @Override
    public void onError(FacebookException error) {
        handleError(error);
        if (error instanceof FacebookAuthorizationException)
            LoginManager.getInstance().logOut();
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        SocialMediaUser user = new SocialMediaUser();
        user.setUserId(object.optString("id"));
        user.setAccessToken(AccessToken.getCurrentAccessToken().getToken());
        user.setProfilePictureUrl(String.format(PROFILE_PIC_URL, user.getUserId()));
        user.setEmail(object.optString("email"));
        user.setFullName(object.optString("name"));
        user.setPageLink(object.optString("link"));
        loadingDialog.dismiss();
        handleSuccess(SocialMediaSignUp.SocialMediaType.FACEBOOK, user);
    }

    private List<String> getScopes() {
        return getAuthData().getScopes();
    }
}

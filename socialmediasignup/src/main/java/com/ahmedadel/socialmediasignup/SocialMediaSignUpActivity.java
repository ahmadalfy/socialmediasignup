package com.ahmedadel.socialmediasignup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.ahmedadel.socialmediasignup.model.SocialMediaUser;
import com.ahmedadel.socialmediasignup.SocialMediaSignUp.SocialMediaType;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * SocialMediaSignUpActivity is the base activity that our social media classes we integrated with will extend.
 */

abstract class SocialMediaSignUpActivity extends AppCompatActivity {

    protected ProgressDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        loadingDialog = SocialMediaSignUp.getInstance().getCustomLoadingDialog();
        if (loadingDialog == null)
            loadingDialog = SocialMediaSignUpUtils.createLoadingDialog(this);
    }

    protected void handCancel(SocialMediaType socialMediaType) {
        stopLoading();
        getAuthData().getCallback().onSignOut(socialMediaType);
        finish();
    }

    protected void handleError(Throwable error) {
        stopLoading();
        getAuthData().getCallback().onError(error);
        finish();
    }

    protected void handleSuccess(SocialMediaSignUp.SocialMediaType socialMediaType, SocialMediaUser socialMediaUser) {
        stopLoading();
        getAuthData().getCallback().onSuccess(socialMediaType, socialMediaUser);
        finish();
    }

    protected abstract SocialMediaData getAuthData();

    protected void startLoading() {
        if (loadingDialog != null)
            loadingDialog.show();
    }

    protected void stopLoading() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

}

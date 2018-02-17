package com.ahmedadel.socialmediasignup.callback;

import com.ahmedadel.socialmediasignup.SocialMediaSignUp;
import com.ahmedadel.socialmediasignup.model.SocialMediaUser;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * SocialMediaSignUpCallback is the callback that will be fired once the sign up with
 * our social media platforms we integrated with is happened.
 */

public interface SocialMediaSignUpCallback {

    void onSuccess(SocialMediaSignUp.SocialMediaType socialMediaType, SocialMediaUser socialMediaUser);

    void onError(Throwable error);

    void onSignOut(SocialMediaSignUp.SocialMediaType socialMediaType);

}

package com.ahmedadel.socialmediasignup;

import com.ahmedadel.socialmediasignup.callback.SocialMediaSignUpCallback;
import com.linkedin.platform.utils.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * SocialMediaData is the data that will used to set the scopes of our social media platforms we integrated with.
 */

class SocialMediaData {

    private List<String> scopes;
    private Scope linkedInScope;
    private SocialMediaSignUpCallback callback;

    @SuppressWarnings("ConstantConditions")
    SocialMediaData(List<String> scopes, SocialMediaSignUpCallback callback) {
        this.scopes = new ArrayList<>(scopes);
        this.callback = callback;
    }

    Scope getLinkedInScope() {
        return linkedInScope;
    }

    void setLinkedInScope(Scope linkedInScope) {
        this.linkedInScope = linkedInScope;
    }

    List<String> getScopes() {
        return scopes;
    }

    SocialMediaSignUpCallback getCallback() {
        return callback;
    }

}

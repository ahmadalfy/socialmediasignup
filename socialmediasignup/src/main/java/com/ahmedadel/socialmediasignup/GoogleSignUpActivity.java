package com.ahmedadel.socialmediasignup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ahmedadel.socialmediasignup.model.SocialMediaUser;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * GoogleSignUpActivity is the activity that handle all the logic behind google plus sdk and that will be launched
 * once the developer chooses google plus social type.
 */

public class GoogleSignUpActivity extends SocialMediaSignUpActivity
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int RC_SIGN_IN = 1000;

    private GoogleApiClient googleApiClient;

    public static void start(Context context) {
        Intent intent = new Intent(context, GoogleSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String clientId = SocialMediaSignUpUtils.getMetaDataValue(this, getString(R.string.com_ahmedadel_socialmediasignup_googleWebClientId));

        GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestProfile()
                .requestEmail()
                .requestIdToken(clientId);

        setupScopes(gsoBuilder);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
            handleSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
    }

    @Override
    protected SocialMediaData getAuthData() {
        return SocialMediaSignUp.getInstance().getGoogleData();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionSuspended(int i) {
        handleError(new Throwable("connection suspended."));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        handleError(new Throwable(connectionResult.getErrorMessage()));
    }

    private void setupScopes(GoogleSignInOptions.Builder builder) {
        List<Scope> scopes = getScopes();
        if (scopes.size() == 1) {
            builder.requestScopes(scopes.get(0));
        } else if (scopes.size() > 1) {
            List<Scope> restScopes = scopes.subList(1, scopes.size());
            Scope[] restScopesArray = new Scope[restScopes.size()];
            restScopesArray = scopes.toArray(restScopesArray);
            builder.requestScopes(scopes.get(0), restScopesArray);
        }
    }

    private List<Scope> getScopes() {
        List<Scope> scopes = new ArrayList<>();
        for (String str : getAuthData().getScopes())
            scopes.add(new Scope(str));
        return scopes;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result == null) {
            handCancel(SocialMediaSignUp.SocialMediaType.GOOGLE_PLUS);
            return;
        }
        if (result.isSuccess() && result.getSignInAccount() != null) {
            final GoogleSignInAccount acct = result.getSignInAccount();
            final SocialMediaUser user = new SocialMediaUser();
            user.setUserId(acct.getId());
            user.setAccessToken(acct.getIdToken());
            user.setProfilePictureUrl(acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "");
            user.setEmail(acct.getEmail());
            user.setFullName(acct.getDisplayName());
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (acct.getAccount() == null) {
                            handleError(new RuntimeException("Account is null"));
                        } else {
                            String accessToken = GoogleAuthUtil.getToken(getApplicationContext(), acct.getAccount(), getAccessTokenScope());
                            user.setAccessToken(accessToken);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    handleSuccess(SocialMediaSignUp.SocialMediaType.GOOGLE_PLUS, user);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        handleError(e);
                    }
                }
            });
        } else {
            String errorMsg = result.getStatus().getStatusMessage();
            if (errorMsg == null) {
                handCancel(SocialMediaSignUp.SocialMediaType.GOOGLE_PLUS);
            } else {
                handleError(new Throwable(result.getStatus().getStatusMessage()));
            }
        }
    }

    private String getAccessTokenScope() {
        String scopes = "oauth2:id profile email";
        if (getAuthData().getScopes().size() > 0)
            scopes = "oauth2:" + TextUtils.join(" ", getAuthData().getScopes());
        return scopes;
    }
}

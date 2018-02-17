package com.ahmedadel.client.socialmediasignup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmedadel.socialmediasignup.SocialMediaSignUp;
import com.ahmedadel.socialmediasignup.SocialMediaSignUp.SocialMediaType;
import com.ahmedadel.socialmediasignup.callback.SocialMediaSignUpCallback;
import com.ahmedadel.socialmediasignup.model.SocialMediaUser;
import com.squareup.picasso.Picasso;

import static com.ahmedadel.client.socialmediasignup.MainActivity.SOCIAL_MEDIA_TYPE;

/**
 * Created by Ahmed Adel on 2/16/18.
 */

public class SocialMediaActivity extends AppCompatActivity implements SocialMediaSignUpCallback {

    private Button socialMediaLoginButton, socialMediaLogoutButton;
    private TextView socialMediaContentTextView;
    private ImageView socialMedialContentImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_media_content);

        socialMediaLoginButton = findViewById(R.id.social_media_content_log_in_btn);
        socialMediaLogoutButton = findViewById(R.id.social_media_content_log_out_btn);
        socialMediaContentTextView = findViewById(R.id.social_media_content_tv);
        socialMedialContentImageView = findViewById(R.id.social_media_content_iv);

        socialMediaContentTextView.setMovementMethod(new ScrollingMovementMethod());

        if (getIntent().getExtras() != null) {
            String socialMediaType = getIntent().getExtras().getString(SOCIAL_MEDIA_TYPE);
            updateSocialMediaState(socialMediaType);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(socialMediaType);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void onSuccess(SocialMediaType socialMediaType, SocialMediaUser socialMediaUser) {
        setSocialMediaContent(socialMediaUser);
    }

    @Override
    public void onError(Throwable error) {
        errorOrCancel(error.getMessage());
    }

    @Override
    public void onSignOut(SocialMediaType socialMediaType) {
        Toast.makeText(this, socialMediaType.getValue() + " is signed out.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSocialMediaState(String socialMediaType) {
        if (socialMediaType.equals(SocialMediaType.FACEBOOK.getValue())) {
            socialMediaLoginButton.setText(R.string.facebook_title);
            socialMediaLoginButton.setBackgroundColor(getResources().getColor(R.color.facebook_bg));
            socialMediaLogoutButton.setBackgroundColor(getResources().getColor(R.color.facebook_bg));
            socialMediaLoginButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().connectTo(SocialMediaType.FACEBOOK, null, this));
            socialMediaLogoutButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().disconnectService(SocialMediaType.FACEBOOK, this));
        } else if (socialMediaType.equals(SocialMediaType.GOOGLE_PLUS.getValue())) {
            socialMediaLoginButton.setText(R.string.google_plus_title);
            socialMediaLoginButton.setBackgroundColor(getResources().getColor(R.color.google_plus_bg));
            socialMediaLogoutButton.setBackgroundColor(getResources().getColor(R.color.google_plus_bg));
            socialMediaLoginButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().connectTo(SocialMediaType.GOOGLE_PLUS, null, this));
            socialMediaLogoutButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().disconnectService(SocialMediaType.GOOGLE_PLUS, this));
        } else if (socialMediaType.equals(SocialMediaType.TWITTER.getValue())) {
            socialMediaLoginButton.setText(R.string.twitter_title);
            socialMediaLoginButton.setBackgroundColor(getResources().getColor(R.color.twitter_bg));
            socialMediaLogoutButton.setBackgroundColor(getResources().getColor(R.color.twitter_bg));
            socialMediaLoginButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().connectTo(SocialMediaType.TWITTER, null, this));
            socialMediaLogoutButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().disconnectService(SocialMediaType.TWITTER, this));
        } else if (socialMediaType.equals(SocialMediaType.INSTAGRAM.getValue())) {
            socialMediaLoginButton.setText(R.string.instagram_title);
            socialMediaLoginButton.setBackgroundColor(getResources().getColor(R.color.instagram_bg));
            socialMediaLogoutButton.setBackgroundColor(getResources().getColor(R.color.instagram_bg));
            socialMediaLoginButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().connectTo(SocialMediaType.INSTAGRAM, null, this));
            socialMediaLogoutButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().disconnectService(SocialMediaType.INSTAGRAM, this));
        } else if (socialMediaType.equals(SocialMediaType.LINKEDIN.getValue())) {
            socialMediaLoginButton.setText(R.string.linkedin_title);
            socialMediaLoginButton.setBackgroundColor(getResources().getColor(R.color.linkedin_bg));
            socialMediaLogoutButton.setBackgroundColor(getResources().getColor(R.color.linkedin_bg));
            socialMediaLoginButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().connectToLinkedIn(null, this));
            socialMediaLogoutButton.setOnClickListener(view -> SocialMediaSignUp.getInstance().disconnectService(SocialMediaType.LINKEDIN, this));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSocialMediaContent(SocialMediaUser socialMediaUser) {
        socialMediaContentTextView.setText("Access Token :" + socialMediaUser.getAccessToken() + "\n" +
                "Full Name : " + socialMediaUser.getFullName() + "\n" +
                "Profile Picture Link :" + socialMediaUser.getProfilePictureUrl());
        Picasso.with(this).load(socialMediaUser.getProfilePictureUrl()).into(socialMedialContentImageView);
        socialMediaLogoutButton.setVisibility(View.VISIBLE);
    }

    private void errorOrCancel(String message) {
        Toast.makeText(SocialMediaActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}

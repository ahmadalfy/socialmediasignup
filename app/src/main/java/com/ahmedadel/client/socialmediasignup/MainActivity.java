package com.ahmedadel.client.socialmediasignup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.ahmedadel.socialmediasignup.SocialMediaSignUp.SocialMediaType;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    public static final String SOCIAL_MEDIA_TYPE = "social_media_type";

    private CharSequence[] socialMediaTypes = {SocialMediaType.FACEBOOK.getValue(),
            SocialMediaType.TWITTER.getValue(), SocialMediaType.GOOGLE_PLUS.getValue(),
            SocialMediaType.INSTAGRAM.getValue(), SocialMediaType.LINKEDIN.getValue()};
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.open_social_media_fragment_btn).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TabbedSocialMediaActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.open_social_media_activity_btn).setOnClickListener(view -> selectSocialMedia());

        generatePackageHash();
    }

    public void selectSocialMedia() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Your Social Media Type");
        builder.setSingleChoiceItems(socialMediaTypes, -1, (dialog, item) -> {
            Intent intent = new Intent(MainActivity.this, SocialMediaActivity.class);
            intent.putExtra(SOCIAL_MEDIA_TYPE, socialMediaTypes[item]);
            startActivity(intent);
            alertDialog.dismiss();
        });
        alertDialog = builder.create();
        alertDialog.show();

    }

    private void generatePackageHash() {
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo("com.ahmedadel.client.socialmediasignup",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }
}

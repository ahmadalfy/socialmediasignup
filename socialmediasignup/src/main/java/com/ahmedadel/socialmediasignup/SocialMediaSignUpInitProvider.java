package com.ahmedadel.socialmediasignup;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * SocialMediaSignUpInitProvider is the provider that will act as an initial point of the social media sign up library.
 */

public class SocialMediaSignUpInitProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        SocialMediaSignUp.init(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}

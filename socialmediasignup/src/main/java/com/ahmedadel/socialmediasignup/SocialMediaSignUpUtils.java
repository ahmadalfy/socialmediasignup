package com.ahmedadel.socialmediasignup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

/**
 * Created by Ahmed Adel on 2/16/18.
 * <p>
 * SocialMediaSignUpUtils are the utils that will need in this library.
 */

class SocialMediaSignUpUtils {

    static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    @Nullable
    static String getMetaDataValue(Context context, String name) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }

        //noinspection ConstantConditions
        return ((String) applicationInfo.metaData.get(name)).trim();
    }

    static ProgressDialog createLoadingDialog(Context context) {
        ProgressDialog loadingDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage(context.getString(R.string.social_media_loading_dialog));
        return loadingDialog;
    }

}

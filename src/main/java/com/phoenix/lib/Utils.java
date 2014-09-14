package com.phoenix.lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.phoenix.lib.activities.PhoenixActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dylan on 7/3/2014.
 */
public class Utils {

    public static final String DEVICE_TEST_ID_NEXUS_5 = "62510AF153B8033719E3A1420DD55B84";
    public static final String DEVICE_TEST_ID_NEXUS_7 = "FE0B946965A78593BB520CFF0FB15BA3";

    private Utils() {

    }

    @SuppressWarnings("deprecation")
    public static void setViewBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= 16)
            view.setBackground(background);
        else
            view.setBackgroundDrawable(background);
    }

    public static void fadeInView(final View view) {
        final int FADE_IN_ANIMATION_DURATION = 1000;
        fadeInView(view, FADE_IN_ANIMATION_DURATION);
    }

    public static void fadeInView(final View view, int duration) {
        view.setAlpha(.0f);
        view.setVisibility(View.VISIBLE);

        view.animate().setDuration(duration).alpha(1.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    public static String getDeviceId(PhoenixActivity activity) {

        String android_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        return Utils.md5(android_id).toUpperCase();

    }

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            if (e != null && e.getStackTrace() != null)
                Log.e("Phoenix Utils", e.getStackTrace().toString());
        }
        return "";
    }
}

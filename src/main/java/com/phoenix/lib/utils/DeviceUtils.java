package com.phoenix.lib.utils;

import android.provider.Settings;
import android.util.Log;

import com.phoenix.lib.activities.PhoenixActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dylan on 9/14/2014.
 */
public class DeviceUtils {
    private DeviceUtils() {

    }

    public static String getDeviceId(PhoenixActivity activity) {
        String android_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        return DeviceUtils.md5(android_id).toUpperCase();
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
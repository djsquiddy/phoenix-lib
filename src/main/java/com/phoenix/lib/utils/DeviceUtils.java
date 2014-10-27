package com.phoenix.lib.utils;

import android.provider.Settings;
import android.util.Log;

import com.phoenix.lib.app.PhoenixActivity;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * date: 9/14/2014
 *
 * @author Dylan
 */
public class DeviceUtils {
    public static final String TAG = DeviceUtils.class.getSimpleName();

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
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error getting device md5 id", e);
        }

        return StringUtils.EMPTY;
    }

}

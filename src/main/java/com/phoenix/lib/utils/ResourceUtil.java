package com.phoenix.lib.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Dylan on 10/25/2014.
 */
public class ResourceUtil {
    public static final String TAG = ResourceUtil.class.getSimpleName();

    private ResourceUtil() {

    }

    /**
     * Finds the resource ID for the current application's resources.
     *
     * @param Rclass Resource class to find resource in.
     *               Example: R.string.class, R.layout.class, R.drawable.class
     * @param name   Name of the resource to search for.
     * @return The id of the resource or -1 if not found.
     */
    public static int getResourceByName(Class<?> Rclass, String name) {
        int id = -1;
        try {
            if (Rclass != null) {
                final Field field = Rclass.getField(name);
                if (field != null) {
                    id = field.getInt(null);
                }
            }
        } catch (final Exception e) {
            Log.e(TAG, "GET_RESOURCE_BY_NAME: ", e);
        }

        return id;
    }

    /**
     * Finds the resource ID for the current application's resources.
     *
     * @param Rclass Resource class to find resource in.
     *               Example: R.string.class, R.layout.class, R.drawable.class
     * @param prefix Name of the resource to search for.
     * @return List of resource ids.
     */
    public static HashMap<String, Integer> getResourceByNamePrefix(Class<?> Rclass, String prefix) {
        HashMap<String, Integer> fields = new HashMap<String, Integer>();
        if (Rclass != null) {
            try {
                for (Field field : Rclass.getDeclaredFields()) {
                    if (field.getName().contains(prefix)) {
                        fields.put(field.getName(), field.getInt(null));
                    }
                }
            } catch (final Exception e) {
                Log.e(TAG, "Error while getting resource names by prefix", e);

            }
        }

        return fields;
    }
}

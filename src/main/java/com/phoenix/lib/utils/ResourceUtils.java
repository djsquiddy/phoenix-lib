package com.phoenix.lib.utils;

import com.phoenix.lib.log.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * date: 10/25/2014
 *
 * @author Dylan
 */
public class ResourceUtils {
    private static final String TAG = ResourceUtils.class.getSimpleName();

    private ResourceUtils() {

    }

    /**
     * Finds the resource ID for the current application's resources.
     *
     * @param resourceClass Resource class to find resource in.
     *                      Example: R.string.class, R.layout.class, R.drawable.class
     * @param name          Name of the resource to search for.
     *
     * @return The id of the resource or -1 if not found.
     */
    public static int getResourceByName(Class<?> resourceClass, String name) {
        int id = -1;
        try {
            if (resourceClass != null) {
                final Field field = resourceClass.getField(name);
                if (field != null) {
                    id = field.getInt(null);
                }
            }
        } catch (final NoSuchFieldException e) {
            Logger.e(TAG, e, "No such field: %s", name);
        } catch (final IllegalAccessException e) {
            Logger.e(TAG, e, "Illegal access exception for field: %s", name);
        }

        return id;
    }

    /**
     * Finds the resource ID for the current application's resources.
     *
     * @param resourceClass Resource class to find resource in.
     *                      Example: R.string.class, R.layout.class, R.drawable.class
     * @param prefix        Name of the resource to search for.
     *
     * @return List of resource ids.
     */
    public static HashMap<String, Integer> getResourceByNamePrefix(Class<?> resourceClass, String prefix) {
        HashMap<String, Integer> fields = new HashMap<String, Integer>();

        if (resourceClass != null) {
            try {
                for (Field field : resourceClass.getDeclaredFields()) {
                    if (field.getName().contains(prefix)) {
                        fields.put(field.getName(), field.getInt(null));
                    }
                }
            } catch (final IllegalAccessException e) {
                Logger.e(TAG, e, "Illegal access exception for prefix: %s", prefix);
            }
        }

        return fields;
    }
}

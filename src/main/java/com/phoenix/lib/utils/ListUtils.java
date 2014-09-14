package com.phoenix.lib.utils;

import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dylan on 9/13/2014.
 */
public class ListUtils {
    private ListUtils() {

    }

    public static <C> List<C> asListFromValues(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }

    public static <C> List<C> asListFromKeys(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }

    public static List<Integer> asListFromValues(SparseIntArray sparseArray) {
        if (sparseArray == null) return null;
        List<Integer> arrayList = new ArrayList<Integer>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }

    public static List<Integer> asListFromKeys(SparseIntArray sparseArray) {
        if (sparseArray == null) return null;
        List<Integer> arrayList = new ArrayList<Integer>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.keyAt(i));
        return arrayList;
    }


}

package com.phoenix.lib.utils;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

/**
 * date: 9/13/2014
 *
 * @author Dylan
 */
public class ListUtils {
    private ListUtils() {

    }

    public static <TType> List<TType> asListFromValues(@NonNull SparseArray<TType> sparseArray) {
        List<TType> arrayList = new ArrayList<TType>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }

        return arrayList;
    }

    public static <TType> List<TType> asListFromKeys(@NonNull SparseArray<TType> sparseArray) {
        List<TType> arrayList = new ArrayList<TType>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }

        return arrayList;
    }

    public static List<Integer> asListFromValues(@NonNull SparseIntArray sparseArray) {
        List<Integer> arrayList = new ArrayList<Integer>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }

        return arrayList;
    }

    public static List<Integer> asListFromKeys(@NonNull SparseIntArray sparseArray) {
        List<Integer> arrayList = new ArrayList<Integer>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.keyAt(i));
        }

        return arrayList;
    }

    public static SparseArray<List<Integer>> asSparseListFromKeys(@NonNull SparseArray<SparseIntArray> sparseArray){
        SparseArray<List<Integer>> sparseArrayList = new SparseArray<List<Integer>>(sparseArray.size());

        for(int i = 0; i < sparseArray.size(); ++i){
            List<Integer> list = asListFromKeys(sparseArray.get(sparseArray.keyAt(i)));
            sparseArrayList.append(sparseArray.keyAt(i), list);
        }

        return sparseArrayList;
    }
}

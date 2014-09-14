package com.phoenix.lib.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Dylan on 7/3/2014.
 */
public abstract class PhoenixFragment extends Fragment {
    public abstract String getFragmentTag();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }
}

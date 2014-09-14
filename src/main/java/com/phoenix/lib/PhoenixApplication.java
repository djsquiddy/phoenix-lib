package com.phoenix.lib;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.phoenix.lib.utils.GAUtils;

/**
 * Created by Dylan on 9/9/2014.
 */
public abstract class PhoenixApplication extends Application {

    public PhoenixApplication() {
        super();
    }

    public abstract int getAnalyticsId();

    @Override
    public void onCreate() {
        super.onCreate();

        if(GAUtils.getInstance(this).isEnabled())
        {
            if (BuildConfig.DEBUG) {// Set the log level to verbose.
                GoogleAnalytics.getInstance(this).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
                GoogleAnalytics.getInstance(this).setDryRun(true);
            }

            GoogleAnalytics.getInstance(this).enableAutoActivityReports(this);
        }

    }
}

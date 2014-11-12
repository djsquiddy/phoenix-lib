package com.phoenix.lib.app;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.phoenix.lib.BuildConfig;
import com.phoenix.lib.utils.GAUtils;

/**
 * date: 9/9/2014
 *
 * @author Dylan
 */
public abstract class BaseApplication extends Application {
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    public abstract int getAnalyticsId();

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        if (GAUtils.getInstance(this).isEnabled()) {
            if (BuildConfig.DEBUG) {// Set the log level to verbose.
                GoogleAnalytics.getInstance(this).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
                GoogleAnalytics.getInstance(this).setDryRun(true);
            }

            GoogleAnalytics.getInstance(this).enableAutoActivityReports(this);
        }

    }
}

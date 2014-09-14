package com.phoenix.lib;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by Dylan on 9/9/2014.
 */
public abstract class PhoenixApplication extends Application {
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public PhoenixApplication() {
        super();
    }

    public abstract int getAnalyticsId();

    public abstract String getApplicationName();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (getAnalyticsId() == 0) {
            Log.e(getApplicationName(), "Trying to get Analytics tracker without an id");
            return null;
        }
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(getAnalyticsId())
                    : analytics.newTracker(R.xml.global_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this
        // app.
        GLOBAL_TRACKER, // Tracker used by all the
        // apps from a company.
        // eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all
        // ecommerce
        // transactions from a
        // company.
    }
}

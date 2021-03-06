package com.phoenix.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.phoenix.lib.R;
import com.phoenix.lib.app.BaseApplication;
import com.phoenix.lib.dialogs.GoogleAnalyticsDialog;

import java.util.HashMap;

/**
 * date: 9/14/2014
 *
 * @author Dylan
 */
public class GAUtils {
    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        // Tracker used only in this
        // app.
        APP_TRACKER,
        // Tracker used by all the
        // apps from a company.
        // eg: roll-up tracking.
        GLOBAL_TRACKER,
        // Tracker used by all
        // ecommerce
        // transactions from a
        // company.
        ECOMMERCE_TRACKER
    }

    private static final String TAG = GAUtils.class.getSimpleName();
    private static GAUtils mInstance;
    private final HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    private Context mContext;

    public static GAUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GAUtils();
        }

        mInstance.mContext = context;

        return mInstance;
    }

    public synchronized void sendInfo(TrackerName trackerId, int categoryResource, int actionResource) {
        Tracker tracker = getTracker(trackerId);
        if (tracker == null) {
            return;
        }

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(mContext.getString(categoryResource))
                .setAction(mContext.getString(actionResource))
                .build());
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        BaseApplication baseApplication = getPhoenixApplication();

        if (baseApplication == null || isEnabled() || baseApplication.getAnalyticsId() == 0) {
            Log.e(TAG, "Trying to get Analytics tracker without an id");
            return null;
        }

        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(baseApplication);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(baseApplication.getAnalyticsId())
                    : analytics.newTracker(R.xml.global_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }

        return mTrackers.get(trackerId);
    }

    private BaseApplication getPhoenixApplication() {
        return (mContext.getApplicationContext() instanceof BaseApplication) ? ((BaseApplication) mContext.getApplicationContext()) : null;
    }

    public boolean isEnabled() {
        SharedPreferences preferences = mContext.getSharedPreferences(GoogleAnalyticsDialog.PREF_NAME, Context.MODE_PRIVATE);

        return !preferences.getBoolean(GoogleAnalyticsDialog.PREF_KEY_OPT_OUT, false);
    }

    public synchronized void sendInfoApp(int categoryResource, int actionResource) {
        Tracker tracker = getTracker(TrackerName.APP_TRACKER);
        if (tracker == null) {
            return;
        }

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(mContext.getString(categoryResource))
                .setAction(mContext.getString(actionResource))
                .build());
    }

    public synchronized void sendInfoApp(String categoryResource, String actionResource) {
        Tracker tracker = getTracker(TrackerName.APP_TRACKER);
        if (tracker == null) {
            return;
        }

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(categoryResource)
                .setAction(actionResource)
                .build());
    }
}

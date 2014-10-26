package com.phoenix.lib.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

import com.phoenix.lib.BuildConfig;
import com.phoenix.lib.R;

import java.util.Date;

/// <summary>
/// Usage
///
///@Override
///protected void onStart()
///{
///  super.onStart();
///
///  BaseRequestFromUserDialog dialog = new InheritedClass();
///  dialog.onStart(this);
///  dialog.showRateDialogIfNeeded(this);
///  //If the criteria is satisfied, "Rate this app" dialog will be shown
///  RateThisApp.showRateDialogIfNeeded(this);
///}
///
/// </summary>

/**
 * Created by Dylan on 9/14/2014.
 */
public abstract class BaseRequestFromUserDialog {

    public static final String PREF_NAME = "com.phoenix.lib.RequestFromUserPrefs";
    private Date mInstallDate = new Date();
    private int mLaunchTimes = 0;
    private boolean mOptOut = false;
    private boolean isShowing;
    private Dialog mDialog;

    /**
     * Call this API when the launcher activity is launched.<br>
     * It is better to call this API in onStart() of the launcher activity.
     */
    public void onStart(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        // If it is the first launch, save the date in shared preference.
        if (pref.getLong(getKeyInstallDate(), 0) == 0L) {
            Date now = new Date();
            editor.putLong(getKeyInstallDate(), now.getTime());
            log("First install: " + now.toString());
        }
        // Increment launch times
        int launchTimes = pref.getInt(getKeyLaunchTimes(), 0);
        launchTimes++;
        editor.putInt(getKeyLaunchTimes(), launchTimes);
        log("Launch times; " + launchTimes);

        editor.apply();

        mInstallDate = new Date(pref.getLong(getKeyInstallDate(), 0));
        mLaunchTimes = pref.getInt(getKeyLaunchTimes(), 0);
        mOptOut = pref.getBoolean(getKeyOptOut(), false);

        printStatus(context);
    }

    protected abstract String getKeyInstallDate();

    /**
     * Print log if enabled
     *
     * @param message
     */
    protected void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.v(getDialogTag(), message);
        }
    }

    protected abstract String getKeyLaunchTimes();

    protected abstract String getKeyOptOut();

    /**
     * Print values in SharedPreferences (used for debug)
     *
     * @param context
     */
    protected void printStatus(final Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        log("*** RateThisApp Status ***");
        log("Install Date: " + new Date(pref.getLong(getKeyInstallDate(), 0)));
        log("Launch Times: " + pref.getInt(getKeyLaunchTimes(), 0));
        log("Opt out: " + pref.getBoolean(getKeyOptOut(), false));
    }

    protected abstract String getDialogTag();

    /**
     * /**
     * Show the rate dialog if the criteria is satisfied
     *
     * @param context
     */
    public void showRateDialogIfNeeded(final Context context) {
        if (shouldShowRateDialog()) {
            showRateDialog(context);
        }
    }

    /**
     * Check whether the rate dialog shoule be shown or not
     *
     * @return
     */
    private boolean shouldShowRateDialog() {
        if (mOptOut) {
            return false;
        } else {
            if (mLaunchTimes >= getLaunchTimes()) {
                return true;
            }
            long threshold = getInstallDays() * 24 * 60 * 60 * 1000L;    // msec

            return new Date().getTime() - mInstallDate.getTime() >= threshold;
        }
    }

    /**
     * Show the rate dialog
     *
     * @param context
     */
    public void showRateDialog(final Context context) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getDialogTitle());
        builder.setMessage(getDialogMessage());
        builder.setPositiveButton(getPositiveButtonText(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositiveButtonPress(context);
            }
        });
        builder.setNeutralButton(R.string.phoenix_base_request_dialog_remind_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearSharedPreferences(context);
            }
        });
        builder.setNegativeButton(R.string.phoenix_base_request_dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setOptOut(context, true);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mDialog = null;
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setCancelable(false);

        mDialog = builder.create();

        mDialog.show();
    }

    /**
     * App launching times until showing rate dialog
     */
    protected abstract int getLaunchTimes();

    /**
     * Days after installation until showing rate dialog
     */
    protected abstract int getInstallDays();

    protected abstract int getDialogTitle();

    protected abstract int getDialogMessage();

    protected abstract int getPositiveButtonText();

    protected abstract void onPositiveButtonPress(final Context context);

    /**
     * Clear data in shared preferences.<br>
     * This API is called when the rate dialog is approved or canceled.
     *
     * @param context
     */
    protected void clearSharedPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(getKeyInstallDate());
        editor.remove(getKeyLaunchTimes());
        editor.apply();
    }

    /**
     * Set opt out flag. If it is true, the rate dialog will never shown unless app data is cleared.
     *
     * @param context
     * @param optOut
     */
    protected void setOptOut(final Context context, boolean optOut) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(getKeyOptOut(), optOut);
        editor.apply();
    }

    public void closeDialogIfOpened(Context context) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
            mDialog = null;
        }
    }
}

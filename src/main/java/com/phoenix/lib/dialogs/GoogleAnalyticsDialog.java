package com.phoenix.lib.dialogs;

import android.content.Context;

import com.phoenix.lib.R;

/**
 * date: 9/10/2014
 *
 * @author Dylan
 */
public class GoogleAnalyticsDialog extends BaseRequestFromUserDialog {
    public static final String PREF_KEY_OPT_OUT = "phoenix_google_analytics_opt_out";

    @Override
    protected String getKeyInstallDate() {
        return "phoenix_install_date";
    }

    @Override
    protected String getKeyLaunchTimes() {
        return "phoenix_launch_times";
    }

    @Override
    protected String getKeyOptOut() {
        return PREF_KEY_OPT_OUT;
    }

    @Override
    protected String getDialogTag() {
        return RateThisAppDialog.class.getSimpleName();
    }

    @Override
    protected int getLaunchTimes() {
        return 5;
    }

    @Override
    protected int getInstallDays() {
        return 1;
    }

    @Override
    protected CharSequence getDialogTitle() {
        return mContext.getResources().getString(R.string.phoenix_google_analytics_dialog_title);
    }

    @Override
    protected CharSequence getDialogMessage() {
        return mContext.getResources().getString(R.string.phoenix_google_analytics_dialog_message);
    }

    @Override
    protected CharSequence getPositiveButtonText() {
        return mContext.getResources().getString(R.string.phoenix_google_analytics_dialog_ok);
    }

    @Override
    protected void onPositiveButtonPress(final Context context) {
        setOptOut(context, true);
    }
}

package com.phoenix.lib.dialogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.phoenix.lib.R;

/**
 * date: 7/3/2014
 *
 * @author Dylan
 */
public class RateThisAppDialog extends BaseRequestFromUserDialog {
    private static final String PREF_KEY_OPT_OUT = "phoenix_rate_app_opt_out";

    @Override
    protected String getKeyInstallDate() {
        return "phoenix_rate_app_install_date";
    }

    @Override
    protected String getKeyLaunchTimes() {
        return "phoenix_rate_app_launch_times";
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
        return 7;
    }

    @Override
    protected int getInstallDays() {
        return 7;
    }

    @Override
    protected CharSequence getDialogTitle() {
        return mContext.getResources().getString(R.string.phoenix_rate_app_dialog_title);
    }

    @Override
    protected CharSequence getDialogMessage() {
        return mContext.getResources().getString(R.string.phoenix_rate_app_dialog_message);
    }

    @Override
    protected CharSequence getPositiveButtonText() {
        return mContext.getResources().getString(R.string.phoenix_rate_app_dialog_ok);
    }

    @Override
    protected void onPositiveButtonPress(Context context) {
        String appPackage = context.getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage));
        context.startActivity(intent);
        setOptOut(context, true);
    }
}

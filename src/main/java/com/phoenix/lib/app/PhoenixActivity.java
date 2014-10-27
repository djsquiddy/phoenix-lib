package com.phoenix.lib.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.phoenix.lib.R;
import com.phoenix.lib.dialogs.RateThisAppDialog;
import com.phoenix.lib.utils.GAUtils;

/**
 * date: 7/3/2014
 *
 * @author Dylan
 */
public abstract class PhoenixActivity extends FragmentActivity {
    public interface BackPressedAction {
        public void onBackPressedAction();
    }

    private static RateThisAppDialog rateThisAppDialog;
    private BackPressedAction backPressedAction;

    public void setBackPressedAction(BackPressedAction backPressedAction) {
        this.backPressedAction = backPressedAction;
    }

    public void reset(Class type) {
        Intent intent = new Intent(this, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void setFragment(PhoenixFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(getSlideInAnimation(), getSlideOutAnimation(), getSlideBackStackInAnimation(), getSlideBackStackOutAnimation())
                .replace(getContentId(), fragment, fragment.getFragmentTag())
                .addToBackStack(fragment.getFragmentTag())
                .commit();
    }

    protected int getSlideInAnimation() {
        return R.anim.slide_in_right;
    }

    protected int getSlideOutAnimation() {
        return R.anim.slide_out_left;
    }

    protected int getSlideBackStackInAnimation() {
        return R.anim.slide_in_left;
    }

    protected int getSlideBackStackOutAnimation() {
        return R.anim.slide_out_right;
    }

    protected abstract int getContentId();

    public void reset() {
        Intent intent = new Intent(this, PhoenixActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }

    protected void setStartFragment(PhoenixFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(getContentId(), fragment, fragment.getFragmentTag())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (backPressedAction != null) {
            Log.d(getActivityTag(), "Back Pressed Action");
            backPressedAction.onBackPressedAction();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d(getActivityTag(), "Popping backstack");
            getSupportFragmentManager().popBackStack();
        } else {
            Log.d(getActivityTag(), "Nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (rateThisAppDialog != null) {
            rateThisAppDialog.closeDialogIfOpened();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (rateThisAppDialog == null) {
            rateThisAppDialog = new RateThisAppDialog();
            rateThisAppDialog.onStart(this);
            rateThisAppDialog.showRateDialogIfNeeded(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (GAUtils.getInstance(this).isEnabled()) {
            GoogleAnalytics.getInstance(this).reportActivityStart(this);
        } else {
            Log.d(getActivityTag(), "Google Analytics is not enabled.");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (GAUtils.getInstance(this).isEnabled()) {
            GoogleAnalytics.getInstance(this).reportActivityStop(this);
        }
    }

    public abstract String getActivityTag();

}

package com.phoenix.lib.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.phoenix.lib.BuildConfig;
import com.phoenix.lib.dialogs.RateThisAppDialog;
import com.phoenix.lib.enums.AnimationDirection;
import com.phoenix.lib.utils.DeviceUtils;
import com.phoenix.lib.utils.GAUtils;

/**
 * date: 7/3/2014
 *
 * @author Dylan
 */
public abstract class BaseActivity extends FragmentActivity implements IBaseView {
    public interface IOnBackPressedAction {
        public void onBackPressedAction();
    }

    private static RateThisAppDialog rateThisAppDialog;
    private AdView adView;
    private IOnBackPressedAction onBackPressedAction;

    public void setBackPressedAction(IOnBackPressedAction onBackPressedAction) {
        this.onBackPressedAction = onBackPressedAction;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    public void reset(Class type) {
        Intent intent = new Intent(this, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Sets the fragment in place of the {@link #getContentId() content view}
     *
     * @param fragment       to set.
     * @param addToBackStack <code>true</code> add the fragment to the backstack;
     *                       <code>false</code> otherwise.
     */
    public void setFragment(BaseFragment fragment, boolean addToBackStack) {
        if (getSupportFragmentManager().findFragmentById(getContentId()) instanceof IOnBackPressedAction) {
            setBackPressedAction(null);
        }

        BaseFragment.Transaction transaction =
                new BaseFragment.Transaction.Builder(this, fragment, getContentId())
                        .setPushAnimation(AnimationDirection.RIGHT_TO_LEFT)
                        .setPopAnimation(AnimationDirection.LEFT_TO_RIGHT)
                        .setTag(fragment.getTag())
                        .setAddToBackStack(addToBackStack)
                        .build();

        transaction.commit();
    }

    /**
     * Sets the fragment in place of the {@link #getContentId() content view}, then places it on the
     * fragment backstack.
     *
     * @param fragment to set.
     */
    public void setFragment(BaseFragment fragment) {
        setFragment(fragment, true);
    }

    protected String getActivityTag() {
        /**
         * ((Object) this).getClass() is to workout a bug in Android Studios
         *
         * @see <a href="https://code.google.com/p/android/issues/detail?id=70135">Android Sudio Issue</a>
         * @see <a href="https://youtrack.jetbrains.com/issue/IDEA-79680">IntellJ Issue</a>
         */
        return ((Object) this).getClass().getSimpleName();
    }

    protected abstract int getContentId();

    public void reset() {
        /**
         * ((Object) this).getClass() is to workout a bug in Android Studios
         *
         * @see <a href="https://code.google.com/p/android/issues/detail?id=70135">Android Sudio Issue</a>
         * @see <a href="https://youtrack.jetbrains.com/issue/IDEA-79680">IntellJ Issue</a>
         */
        Intent intent = new Intent(this, ((Object) this).getClass());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        finish();
    }

    private void setStartFragment(BaseFragment fragment) {
        if (fragment == null) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(getContentId(), fragment, fragment.getFragmentTag())
                .commit();
    }

    protected abstract BaseFragment getStartingFragment();

    protected int getAdViewId() {
        return -1;
    }

    private void setUpAdMob() {
        AdRequest adRequest;

        if (BuildConfig.DEBUG) {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(DeviceUtils.getDeviceId(this))
                    .build();
        } else {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
        }

        adView.loadAd(adRequest);
    }

    /**
     * @see {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        if (onBackPressedAction != null) {
            Log.d(getActivityTag(), "Back Pressed Action");
            onBackPressedAction.onBackPressedAction();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d(getActivityTag(), "Popping backstack");
            getSupportFragmentManager().popBackStack();
        } else {
            Log.d(getActivityTag(), "Nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    /**
     * Sets the content view with the layout id from {@link #getLayoutId()}
     *
     * @see {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        if (savedInstanceState == null) {
            setStartFragment(getStartingFragment());

            if (getAdViewId() > 0) {
                adView = findView(getAdViewId());
                setUpAdMob();
            }
        }
    }

    /**
     * Called before the activity is destroyed.
     *
     * @see {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    /**
     * @see {@inheritDoc}
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (rateThisAppDialog != null) {
            rateThisAppDialog.closeDialogIfOpened();
        }

        if (adView != null) {
            adView.pause();
        }
    }

    /**
     * @see {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (adView != null) {
            adView.resume();
        }
    }

    /**
     * @see {@inheritDoc}
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (rateThisAppDialog == null) {
            rateThisAppDialog = new RateThisAppDialog();
            rateThisAppDialog.onStart(this);
            rateThisAppDialog.showRateDialogIfNeeded(this);
        }
    }

    /**
     * Checks to see if {@link com.google.android.gms.analytics.GoogleAnalytics GoogleAnalytices} is
     * enabled. If it is then start reporting.
     *
     * @see {@inheritDoc}
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (GAUtils.getInstance(this).isEnabled()) {
            GoogleAnalytics.getInstance(this).reportActivityStart(this);
        } else {
            Log.d(getActivityTag(), "Google Analytics is not enabled.");
        }
    }

    /**
     * Checks to see if {@link com.google.android.gms.analytics.GoogleAnalytics GoogleAnalytices} is
     * enabled. If it is then stop reporting.
     *
     * @see {@inheritDoc}
     */
    @Override
    protected void onStop() {
        if (GAUtils.getInstance(this).isEnabled()) {
            GoogleAnalytics.getInstance(this).reportActivityStop(this);
        }

        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
        }

        super.onStop();
    }
}

package com.phoenix.lib.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.phoenix.lib.R;
import com.phoenix.lib.fragments.PhoenixFragment;

/**
 * Created by Dylan on 7/3/2014.
 */
public abstract class PhoenixActivity extends FragmentActivity {
    BackPressedAction backPressedAction;


    public void setBackPressedAction(BackPressedAction backPressedAction) {
        this.backPressedAction = backPressedAction;
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    public abstract String getActivityTag();

    protected abstract int getContentId();

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
            Log.i(getActivityTag(), "Back Pressed Action");
            backPressedAction.onBackPressedAction();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.i(getActivityTag(), "Popping backstack");
            getSupportFragmentManager().popBackStack();
        } else {
            Log.i(getActivityTag(), "Nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    public interface BackPressedAction {
        public void onBackPressedAction();
    }
}

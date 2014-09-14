package com.phoenix.lib.views;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.phoenix.lib.R;

/**
 * Created by Dylan on 8/17/2014.
 */
public class ViewUtils {
    private static int ANIMATION_TIME = 1000;

    public static void setViewFlipperAnimNext(ViewFlipper viewFlipper) {
        setViewFlipperAnimNext(viewFlipper, ANIMATION_TIME);
    }

    public static void setViewFlipperAnimNext(ViewFlipper viewFlipper, int animationTime) {
        final Animation inAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_in_right);
        final Animation outAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_out_left);
        inAnim.setDuration(animationTime);
        outAnim.setDuration(animationTime);

        viewFlipper.setInAnimation(inAnim);
        viewFlipper.setOutAnimation(outAnim);
    }

    public static void setViewFlipperAnimPrev(ViewFlipper viewFlipper) {
        setViewFlipperAnimPrev(viewFlipper, ANIMATION_TIME);
    }

    public static void setViewFlipperAnimPrev(ViewFlipper viewFlipper, int animationTime) {
        final Animation inAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_in_left);
        final Animation outAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_out_right);
        inAnim.setDuration(animationTime);
        outAnim.setDuration(animationTime);

        viewFlipper.setInAnimation(inAnim);
        viewFlipper.setOutAnimation(outAnim);
    }

    public static void setViewSwitcherAnimNext(ViewSwitcher viewFlipper) {
        setViewSwitcherAnimNext(viewFlipper, ANIMATION_TIME);
    }

    public static void setViewSwitcherAnimNext(ViewSwitcher viewFlipper, int animationTime) {
        final Animation inAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_in_right);
        final Animation outAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_out_left);
        inAnim.setDuration(animationTime);
        outAnim.setDuration(animationTime);

        viewFlipper.setInAnimation(inAnim);
        viewFlipper.setOutAnimation(outAnim);
    }

    public static void setViewSwitcherAnimPrev(ViewSwitcher viewFlipper) {
        setViewSwitcherAnimPrev(viewFlipper, ANIMATION_TIME);
    }

    public static void setViewSwitcherAnimPrev(ViewSwitcher viewFlipper, int animationTime) {
        final Animation inAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_in_left);
        final Animation outAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_out_right);
        inAnim.setDuration(animationTime);
        outAnim.setDuration(animationTime);

        viewFlipper.setInAnimation(inAnim);
        viewFlipper.setOutAnimation(outAnim);
    }
}

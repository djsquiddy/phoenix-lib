package com.phoenix.lib.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.phoenix.lib.R;

/**
 * Created by Dylan on 8/17/2014.
 */
public class ViewUtils {
    private static int ANIMATION_TIME = 1000;

    private ViewUtils() {

    }

    @SuppressWarnings("deprecation")
    public static void setViewBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= 16)
            view.setBackground(background);
        else
            view.setBackgroundDrawable(background);
    }

    public static void fadeInView(final View view) {
        final int FADE_IN_ANIMATION_DURATION = 1000;
        fadeInView(view, FADE_IN_ANIMATION_DURATION);
    }

    public static void fadeInView(final View view, int duration) {
        view.setAlpha(.0f);
        view.setVisibility(View.VISIBLE);

        view.animate().setDuration(duration).alpha(1.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }


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

    public static void setColor(TextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}

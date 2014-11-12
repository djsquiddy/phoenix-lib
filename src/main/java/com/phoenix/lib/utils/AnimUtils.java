package com.phoenix.lib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

import com.phoenix.lib.R;

/**
 * date: 11/11/2014
 *
 * @author Dylan
 */
public class AnimUtils {
    private static final int DEFAULT_ANIMATION_TIME = 1000;

    private AnimUtils() {

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

    public static class ViewAnimatorHelper {
        public static void setViewSwitcherAnimRightToLeft(ViewAnimator viewFlipper) {
            setViewSwitcherAnimRightToLeft(viewFlipper, DEFAULT_ANIMATION_TIME);
        }

        public static void setViewSwitcherAnimRightToLeft(ViewAnimator viewFlipper, int animationTime) {
            final Animation inAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_in_right);
            final Animation outAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_out_left);
            inAnim.setDuration(animationTime);
            outAnim.setDuration(animationTime);

            viewFlipper.setInAnimation(inAnim);
            viewFlipper.setOutAnimation(outAnim);
        }

        public static void setViewSwitcherAnimLeftToRight(ViewAnimator viewFlipper) {
            setViewSwitcherAnimLeftToRight(viewFlipper, DEFAULT_ANIMATION_TIME);
        }

        public static void setViewSwitcherAnimLeftToRight(ViewAnimator viewFlipper, int animationTime) {
            final Animation inAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_in_left);
            final Animation outAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_out_right);
            inAnim.setDuration(animationTime);
            outAnim.setDuration(animationTime);

            viewFlipper.setInAnimation(inAnim);
            viewFlipper.setOutAnimation(outAnim);
        }

        public static void setViewSwitcherAnimTopToBottom(ViewAnimator viewFlipper) {
            setViewSwitcherAnimTopToBottom(viewFlipper, DEFAULT_ANIMATION_TIME);
        }

        public static void setViewSwitcherAnimTopToBottom(ViewAnimator viewFlipper, int animationTime) {
            final Animation inAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_in_top);
            final Animation outAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_out_bottom);
            inAnim.setDuration(animationTime);
            outAnim.setDuration(animationTime);

            viewFlipper.setInAnimation(inAnim);
            viewFlipper.setOutAnimation(outAnim);
        }

        public static void setViewSwitcherAnimBottomToTop(ViewAnimator viewFlipper) {
            setViewSwitcherAnimBottomToTop(viewFlipper, DEFAULT_ANIMATION_TIME);
        }

        public static void setViewSwitcherAnimBottomToTop(ViewAnimator viewFlipper, int animationTime) {
            final Animation inAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_in_bottom);
            final Animation outAnim = AnimationUtils.loadAnimation(viewFlipper.getContext(), R.anim.slide_out_top);
            inAnim.setDuration(animationTime);
            outAnim.setDuration(animationTime);

            viewFlipper.setInAnimation(inAnim);
            viewFlipper.setOutAnimation(outAnim);
        }
    }
}

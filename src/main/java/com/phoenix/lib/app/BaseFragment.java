package com.phoenix.lib.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phoenix.lib.R;
import com.phoenix.lib.enums.AnimationDirection;

import org.apache.commons.lang3.StringUtils;

/**
 * date: 7/3/2014
 *
 * @author Dylan
 */
public abstract class BaseFragment extends Fragment implements IBaseView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) getView().findViewById(id);
    }

    public abstract String getFragmentTag();

    public static class Transaction {
        private FragmentTransaction mFragmentTransaction;

        private Transaction(FragmentTransaction fragmentTransaction) {
            this.mFragmentTransaction = fragmentTransaction;
        }

        public void commit() {
            mFragmentTransaction.commit();
        }

        public void commitAllowingStateLoss() {
            mFragmentTransaction.commitAllowingStateLoss();
        }

        public static class Builder {
            private static final int INVALID_ANIMATION = -1;

            private final Fragment mFragment;
            private final FragmentManager mFragmentManager;
            private final int mContainerId;

            private String mTag;
            private int mEnterAnimation;
            private int mExitAnimation;
            private int mPopEnterAnimation;
            private int mPopExitAnimation;
            private boolean mAddToBackStack;

            public Builder(FragmentManager fragmentManager, Fragment fragment, int containerId) {
                mFragmentManager = fragmentManager;
                mFragment = fragment;
                mContainerId = containerId;
                mEnterAnimation = mExitAnimation = mPopEnterAnimation = mPopExitAnimation = INVALID_ANIMATION;
                mAddToBackStack = true;
            }

            public Builder(FragmentActivity fragmentActivity, Fragment fragment, int containerId) {
                this(fragmentActivity.getSupportFragmentManager(), fragment, containerId);
            }

            public Builder setTag(String tag) {
                this.mTag = tag;
                return this;
            }

            public Builder setPopAnimation(int enterAnimation, int exitAnimation) {
                mPopEnterAnimation = enterAnimation;
                mPopExitAnimation = exitAnimation;
                return this;
            }

            public Builder setPushAnimation(int enterAnimation, int exitAnimation) {
                mEnterAnimation = enterAnimation;
                mExitAnimation = exitAnimation;
                return this;
            }

            private void setAnimationDirection(AnimationDirection animationDirection, boolean isPushAnimation) {
                int enterAnimation = INVALID_ANIMATION;
                int exitAnimation = INVALID_ANIMATION;

                switch (animationDirection) {
                    case TOP_TO_BOTTOM:
                        enterAnimation = R.anim.slide_in_top;
                        exitAnimation = R.anim.slide_out_bottom;
                        break;

                    case BOTTOM_TO_TOP:
                        enterAnimation = R.anim.slide_in_bottom;
                        exitAnimation = R.anim.slide_out_top;
                        break;

                    case LEFT_TO_RIGHT:
                        enterAnimation = R.anim.slide_in_left;
                        exitAnimation = R.anim.slide_out_right;
                        break;

                    case RIGHT_TO_LEFT:
                        enterAnimation = R.anim.slide_in_right;
                        exitAnimation = R.anim.slide_out_left;
                        break;
                }

                if (isPushAnimation) {
                    mEnterAnimation = enterAnimation;
                    mExitAnimation = exitAnimation;
                } else {
                    mEnterAnimation = enterAnimation;
                    mExitAnimation = exitAnimation;
                }

            }

            public Builder setPushAnimation(AnimationDirection pushAnimationDirection) {
                setAnimationDirection(pushAnimationDirection, true);
                return this;
            }

            public Builder setPopAnimation(AnimationDirection popAnimationDirection) {
                setAnimationDirection(popAnimationDirection, false);
                return this;
            }

            public Builder setAddToBackStack(boolean addToBackStack) {
                mAddToBackStack = addToBackStack;
                return this;
            }

            public Transaction build() {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

                if ((mEnterAnimation & mExitAnimation) != INVALID_ANIMATION) {
                    if ((mPopEnterAnimation & mPopExitAnimation) != INVALID_ANIMATION) {
                        fragmentTransaction.setCustomAnimations(mEnterAnimation, mExitAnimation,
                                                                mPopEnterAnimation, mPopExitAnimation);
                    } else {
                        fragmentTransaction.setCustomAnimations(mEnterAnimation, mExitAnimation);
                    }
                }

                if(StringUtils.isBlank(mTag) && mFragment instanceof BaseFragment){
                    mTag = mFragment.getTag();
                }

                fragmentTransaction.replace(mContainerId, mFragment, mTag);

                if (mAddToBackStack) {
                    fragmentTransaction.addToBackStack(null);
                }

                return new Transaction(fragmentTransaction);
            }

        }
    }
}

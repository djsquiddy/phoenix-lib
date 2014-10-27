package com.phoenix.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.phoenix.lib.R;

import java.util.List;
import java.util.Stack;

/**
 * date: 10/25/2014
 *
 * @author Dylan
 */
public class SwipeTextChanger extends TextSwitcher implements GestureDetector.OnGestureListener, ViewSwitcher.ViewFactory, View.OnTouchListener {
    public interface IOnValuesCompleted {
        void onValuesCompleted();
    }

    public interface IValueListener {
        void onNextValue(int value);

        void onPreviousValue(int value);
    }

    public interface IAnimationListener {
        void onAnimate(AnimationDirection direction);
    }

    public enum AnimationDirection {
        TOP_TO_BOTTOM, BOTTOM_TO_TOP, LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 200;
    private static final int MAX_PREV_LIST_SIZE = 10;
    private final Stack<Integer> mPrevList = new Stack<Integer>();
    private final TextViewPadding mTextViewPadding = new TextViewPadding();
    private boolean mIsRightToLeftChangeValue;
    private List<Integer> mValues;
    private int mCurrentIndex;
    private int mTextColor;
    private float mTextSize;
    private int mTextGravityFlags;
    private GestureDetector mGestureDetector;
    private IAnimationListener mAnimationListener;
    private IValueListener mValueListener;
    private IOnValuesCompleted mOnValuesCompleted;

    public SwipeTextChanger(Context context) {
        super(context, null);
    }

    public SwipeTextChanger(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SwipeTextChangerAttributes, 0, 0);

            try {
                mIsRightToLeftChangeValue = attributes.getBoolean(R.styleable.SwipeTextChangerAttributes_rightToLeftNextValue, true);
                mTextColor = attributes.getColor(R.styleable.SwipeTextChangerAttributes_textColor, Color.rgb(255, 255, 255));
                mTextGravityFlags = attributes.getInt(R.styleable.SwipeTextChangerAttributes_textGravity, Gravity.END | Gravity.TOP);
                mTextSize = attributes.getDimension(R.styleable.SwipeTextChangerAttributes_textSize, getResources().getDimension(R.dimen.swipeTextChanger_textSize));
                mTextViewPadding.top = attributes.getDimensionPixelOffset(R.styleable.SwipeTextChangerAttributes_textViewPaddingTop, 0);
                mTextViewPadding.bottom = attributes.getDimensionPixelOffset(R.styleable.SwipeTextChangerAttributes_textViewPaddingBottom, 0);
                mTextViewPadding.left = attributes.getDimensionPixelOffset(R.styleable.SwipeTextChangerAttributes_textViewPaddingLeft, 0);
                mTextViewPadding.right = attributes.getDimensionPixelOffset(R.styleable.SwipeTextChangerAttributes_textViewPaddingRight, 0);
            } finally {
                if (attributes != null) {
                    attributes.recycle();
                }
            }
        }

        setFactory(this);
        mGestureDetector = new GestureDetector(context, this);
        setOnTouchListener(this);
        setLongClickable(true);
    }

    public void setAnimationListener(IAnimationListener animationListener) {
        mAnimationListener = animationListener;
    }

    public void setValueListener(IValueListener valueListener) {
        mValueListener = valueListener;
    }

    public void setOnValuesCompleted(IOnValuesCompleted onValuesCompleted) {
        mOnValuesCompleted = onValuesCompleted;
    }

    public void setValues(List<Integer> values) {
        mValues = values;
        mCurrentIndex = 0;
        mPrevList.clear();
        setNextValue();
    }

    protected void setNextValue() {
        mPrevList.push(mValues.get(mCurrentIndex));
        Integer val;
        if (++mCurrentIndex == mValues.size()) {
            if (mOnValuesCompleted != null) {
                mOnValuesCompleted.onValuesCompleted();
            }

            mCurrentIndex = 0;
            val = mValues.get(mCurrentIndex);
        } else {
            val = mValues.get(mCurrentIndex);
        }

        setText(getResources().getString(val));

        if (mValueListener != null) {
            mValueListener.onNextValue(val);
        }

        if (mPrevList.size() > MAX_PREV_LIST_SIZE) {
            mPrevList.remove(MAX_PREV_LIST_SIZE);
        }
    }

    public void setWordColor(int color, int wordIndex) {
        String text = getCurrentText();
        String[] words = getCurrentText().split(" ");
        String match = words[wordIndex];
        int start = text.indexOf(match);

        changeCurrentTextColor(color, start, match.length());
    }

    public String getCurrentText() {
        return getResources().getString(mValues.get(mCurrentIndex));
    }

    public void changeCurrentTextColor(int color, int startIndex, int count) {
        String text = getCurrentText();
        int end = startIndex + count;
        Spannable wordToSpan = new SpannableString(text);
        wordToSpan.setSpan(new ForegroundColorSpan(color), startIndex, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        getView().setText(wordToSpan);
    }

    public TextView getView() {
        return (TextView) getCurrentView();
    }

    public void removeTextChange() {
        getView().setText(getCurrentText());
    }

    public int getCurrentResource() {
        return mValues.get(mCurrentIndex);
    }

    public void changeCharTextColor(int index) {
        String text = getCurrentText();
        Spannable wordToSpan = new SpannableString(text);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.BLACK), index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        getView().setText(wordToSpan);
    }

    protected void setPrevValue() {
        if (mPrevList.size() > 0) {
            int val = mPrevList.pop();

            setText(getResources().getString(val));

            if (mValueListener != null) {
                mValueListener.onPreviousValue(val);
            }

        } else {
            setNextValue();
        }
    }

    private void rightToLeft() {
        ViewUtils.setViewSwitcherAnimRightToLeft(this);

        if (mIsRightToLeftChangeValue) {
            setNextValue();
        } else {
            setPrevValue();
        }

        if (mAnimationListener != null) {
            mAnimationListener.onAnimate(AnimationDirection.RIGHT_TO_LEFT);
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    leftToRight();
                } else {
                    rightToLeft();
                }
            }
        } else {
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeTopToBottom();
                } else {
                    onSwipeBottomToTop();
                }
            }
        }

        return false;
    }

    private void leftToRight() {
        ViewUtils.setViewSwitcherAnimLeftToRight(this);
        if (mIsRightToLeftChangeValue) {
            setPrevValue();
        } else {
            setNextValue();
        }

        if (mAnimationListener != null) {
            mAnimationListener.onAnimate(AnimationDirection.LEFT_TO_RIGHT);
        }
    }

    private void onSwipeTopToBottom() {
        setPrevValue();
        ViewUtils.setViewSwitcherAnimTopToBottom(this);

        if (mAnimationListener != null) {
            mAnimationListener.onAnimate(AnimationDirection.TOP_TO_BOTTOM);
        }
    }

    private void onSwipeBottomToTop() {
        setNextValue();
        ViewUtils.setViewSwitcherAnimBottomToTop(this);

        if (mAnimationListener != null) {
            mAnimationListener.onAnimate(AnimationDirection.BOTTOM_TO_TOP);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return false;
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(getContext());
        textView.setGravity(mTextGravityFlags);
        textView.setTextSize(mTextSize);
        textView.setTextColor(mTextColor);
        mTextViewPadding.setPadding(textView);

        return textView;
    }

    private class TextViewPadding {
        public int top;
        public int bottom;
        public int right;
        public int left;

        public void setPadding(TextView view) {
            view.setPadding(left, top, right, bottom);
        }
    }
}

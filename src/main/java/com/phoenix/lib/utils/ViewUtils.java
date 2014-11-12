package com.phoenix.lib.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

/**
 * date: 8/17/2014
 *
 * @author Dylan
 */
public class ViewUtils {

    private ViewUtils() {

    }

    @SuppressWarnings("deprecation")
    public static void setViewBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

    public static void setColor(TextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}

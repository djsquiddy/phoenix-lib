/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 phoenix-lib
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.phoenix.lib.log;

import android.content.Context;

import com.phoenix.lib.R;
import com.phoenix.lib.app.BaseApplication;


/**
 * date:   10/26/2014
 *
 * @author Dylan Jones
 */
public enum LogLevel {
    VERBOSE(0x01, R.color.log_level_verbose, R.string.log_level_verbose),
    INFO(0x02, R.color.log_level_info, R.string.log_level_info),
    DEBUG(0x02, R.color.log_level_debug, R.string.log_level_debug),
    ERROR(0x02, R.color.log_level_error, R.string.log_level_error),
    ASSERT(0x02, R.color.log_level_assert, R.string.log_level_assert),
    WARNING(0x02, R.color.log_level_warning, R.string.log_level_warning);

    private final int mColorId;
    private final int mValue;
    private final int mNameId;

    LogLevel(int value, int color, int nameId) {
        this.mValue = value;
        this.mColorId = color;
        this.mNameId = nameId;
    }

    private static int getDefaultColor(LogLevel level) {
        int color = 0;

        switch (level) {
            case VERBOSE:
                color = 0xFFBBBBBB;
                break;

            case INFO:
                color = 0xFFBBB400;
                break;

            case DEBUG:
                color = 0xFF00BB00;
                break;

            case ERROR:
                color = 0xFFBBB400;
                break;

            case ASSERT:
                color = 0xFFFF6B68;
                break;

            case WARNING:
                color = 0xFFBB7A00;
                break;
        }

        return color;
    }

    public int getValue() {
        return mValue;
    }

    public int getColor() {
        return (BaseApplication.getContext() != null) ? BaseApplication.getContext().getResources().getColor(mColorId) : getDefaultColor(this);
    }

    public int getColor(final Context context) {
        return context.getResources().getColor(mColorId);
    }

    @Override
    public String toString() {
        return (BaseApplication.getContext() != null) ? BaseApplication.getContext().getString(mNameId) : super.name();
    }

    public String toString(final Context context) {
        return context.getString(mNameId);
    }
}

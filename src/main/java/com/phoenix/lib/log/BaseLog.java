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
 */

package com.phoenix.lib.log;

import android.support.annotation.NonNull;

import java.util.EnumSet;
import java.util.Objects;

import static java.util.Arrays.asList;

/**
 * date: 10/26/2014.
 *
 * @author Dylan
 */
public abstract class BaseLog {
    protected final EnumSet<LogLevel> mEnabledLevels = EnumSet.allOf(LogLevel.class);

    public void enableLogLevel(LogLevel level) {
        if (!mEnabledLevels.contains(level)) {
            mEnabledLevels.add(level);
        }
    }

    public void disableLevel(LogLevel level) {
        if (mEnabledLevels.contains(level)) {
            mEnabledLevels.remove(level);
        }
    }

    public boolean isLevelEnabled(LogLevel level) {
        return mEnabledLevels.contains(level);
    }

    public void enableLevels(LogLevel... levels) {
        for (LogLevel level : levels) {
            if (!mEnabledLevels.contains(level)) {
                mEnabledLevels.add(level);
            }
        }
    }

    public void disableLevels(EnumSet<LogLevel> levels) {
        for (LogLevel level : levels) {
            if (mEnabledLevels.contains(level)) {
                mEnabledLevels.remove(level);
            }
        }
    }

    public void enabledLevels(EnumSet<LogLevel> levels) {
        for (LogLevel level : levels) {
            if (!mEnabledLevels.contains(level)) {
                mEnabledLevels.add(level);
            }
        }
    }

    public void enableReleaseMode() {
        disableLevels(LogLevel.VERBOSE, LogLevel.DEBUG, LogLevel.INFO);
    }

    protected String createDefaultLogEntry(LogData logData) {
        return String.format("%1s %s [%2s]:%3s%n", logData.dateTimeStr, logData.pid, logData.tag, logData.getMsg());
    }

    public void disableLevels(LogLevel... levels) {
        for (LogLevel level : levels) {
            if (mEnabledLevels.contains(level)) {
                mEnabledLevels.remove(level);
            }
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public final void v(@NonNull String tag, @NonNull String msg) {
        LogHelper.v(this, tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public final void v(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        LogHelper.v(this, tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag  Tag for for the log data. Can be used to organize log statements.
     * @param msg  The actual message to be logged.
     * @param args for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void v(@NonNull String tag, @NonNull String msg, @NonNull Objects... args) {
        LogHelper.v(this, tag, String.format(msg, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void v(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable, @NonNull Objects... args) {
        LogHelper.v(this, tag, String.format(msg, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public final void i(@NonNull String tag, @NonNull String msg) {
        LogHelper.i(this, tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public final void i(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        LogHelper.i(this, tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag  Tag for for the log data. Can be used to organize log statements.
     * @param msg  The actual message to be logged.
     * @param args for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void i(@NonNull String tag, @NonNull String msg, @NonNull Objects... args) {
        LogHelper.i(this, tag, String.format(msg, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void i(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable, @NonNull Objects... args) {
        LogHelper.i(this, tag, String.format(msg, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public final void d(@NonNull String tag, @NonNull String msg) {
        LogHelper.d(this, tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public final void d(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        LogHelper.d(this, tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag  Tag for for the log data. Can be used to organize log statements.
     * @param msg  The actual message to be logged.
     * @param args for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void d(@NonNull String tag, @NonNull String msg, @NonNull Objects... args) {
        LogHelper.d(this, tag, String.format(msg, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#DEBUG} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void d(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable, @NonNull Objects... args) {
        LogHelper.d(this, tag, String.format(msg, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public final void e(@NonNull String tag, @NonNull String msg) {
        LogHelper.e(this, tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public final void e(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        LogHelper.e(this, tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag  Tag for for the log data. Can be used to organize log statements.
     * @param msg  The actual message to be logged.
     * @param args for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void e(@NonNull String tag, @NonNull String msg, @NonNull Objects... args) {
        LogHelper.e(this, tag, String.format(msg, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void e(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable, @NonNull Objects... args) {
        LogHelper.e(this, tag, String.format(msg, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public final void wtf(@NonNull String tag, @NonNull String msg) {
        LogHelper.wtf(this, tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public final void wtf(@NonNull String tag, @NonNull Throwable throwable) {
        LogHelper.wtf(this, tag, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public final void wtf(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        LogHelper.wtf(this, tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag  Tag for for the log data. Can be used to organize log statements.
     * @param msg  The actual message to be logged.
     * @param args for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void wtf(@NonNull String tag, @NonNull String msg, @NonNull Objects... args) {
        LogHelper.wtf(this, tag, String.format(msg, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @see {@link String#format(String, Object...)}
     */
    public final void wtf(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable, @NonNull Objects... args) {
        LogHelper.wtf(this, tag, String.format(msg, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public final void w(@NonNull String tag, @NonNull String msg) {
        LogHelper.w(this, tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public final void w(@NonNull String tag, @NonNull Throwable throwable) {
        LogHelper.w(this, tag, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public final void w(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        LogHelper.w(this, tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag  Tag for for the log data. Can be used to organize log statements.
     * @param msg  The actual message to be logged.
     * @param args for formatting the output
     */
    public final void w(@NonNull String tag, @NonNull String msg, @NonNull Objects... args) {
        LogHelper.w(this, tag, String.format(msg, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     */
    public final void w(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable, @NonNull Objects... args) {
        LogHelper.w(this, tag, String.format(msg, asList(args)), throwable);
    }

    /**
     * Instructs first ILogNode in the list to print the log data provided.
     *
     * @param priority Log level of the data being logged.
     *                 {@link android.util.Log#VERBOSE Log.VERBOSE},
     *                 {@link android.util.Log#DEBUG Log.DEBUG},
     *                 {@link android.util.Log#INFO Log.INFO},
     *                 {@link android.util.Log#WARN Log.WARN}, or
     *                 {@link android.util.Log#ERROR Log.ERROR}.
     * @param tag      Tag for for the log data.  Can be used to organize log statements.
     * @param msg      The actual message to be logged. The actual message to be logged.
     */
    protected void print(LogLevel priority, @NonNull String tag, @NonNull String msg) {
        print(new LogData(priority, tag, msg));
    }

    /**
     * Instructs first ILogNode in the list to print the log data provided.
     *
     * @param logData contains the data for the log.
     */
    protected abstract void print(@NonNull LogData logData);
}

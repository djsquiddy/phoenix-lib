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
import android.util.Log;

/**
 * date: 10/26/2014
 *
 * @author Dylan
 */
class LogHelper {
    private LogHelper() {

    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void v(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        if (log.mEnabledLevels.contains(LogLevel.VERBOSE)) {
            log.print(new LogData(LogLevel.VERBOSE, tag, String.format("%s%n%s", msg, Log.getStackTraceString(throwable))));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void v(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg) {
        if (log.mEnabledLevels.contains(LogLevel.VERBOSE)) {
            log.print(new LogData(LogLevel.VERBOSE, tag, msg));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void i(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        if (log.mEnabledLevels.contains(LogLevel.INFO)) {
            log.print(new LogData(LogLevel.INFO, tag, String.format("%s%n%s", msg, Log.getStackTraceString(throwable))));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO`} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void i(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg) {
        if (log.mEnabledLevels.contains(LogLevel.INFO)) {
            log.print(new LogData(LogLevel.INFO, tag, msg));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#DEBUG} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void d(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        if (log.mEnabledLevels.contains(LogLevel.DEBUG)) {
            log.print(new LogData(LogLevel.DEBUG, tag, String.format("%s%n%s", msg, Log.getStackTraceString(throwable))));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#DEBUG} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void d(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg) {
        if (log.mEnabledLevels.contains(LogLevel.DEBUG)) {
            log.print(new LogData(LogLevel.DEBUG, tag, msg));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void e(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        if (log.mEnabledLevels.contains(LogLevel.ERROR)) {
            log.print(new LogData(LogLevel.ERROR, tag, String.format("%s%n%s", msg, Log.getStackTraceString(throwable))));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void e(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg) {
        if (log.mEnabledLevels.contains(LogLevel.ERROR)) {
            log.print(new LogData(LogLevel.ERROR, tag, msg));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void wtf(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg) {
        if (log.mEnabledLevels.contains(LogLevel.ASSERT)) {
            log.print(new LogData(LogLevel.ASSERT, tag, msg));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void w(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg) {
        if (log.mEnabledLevels.contains(LogLevel.WARNING)) {
            log.print(new LogData(LogLevel.WARNING, tag, msg));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void wtf(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        if (log.mEnabledLevels.contains(LogLevel.ASSERT)) {
            log.print(new LogData(LogLevel.ASSERT, tag, String.format("%s%n%s", msg, Log.getStackTraceString(throwable))));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void w(@NonNull BaseLog log, @NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
        if (log.mEnabledLevels.contains(LogLevel.WARNING)) {
            log.print(new LogData(LogLevel.WARNING, tag, String.format("%s%n%s", msg, Log.getStackTraceString(throwable))));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void wtf(@NonNull BaseLog log, @NonNull String tag, @NonNull Throwable throwable) {
        if (log.mEnabledLevels.contains(LogLevel.ASSERT)) {
            log.print(new LogData(LogLevel.ASSERT, tag, Log.getStackTraceString(throwable)));
        }
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void w(@NonNull BaseLog log, @NonNull String tag, @NonNull Throwable throwable) {
        if (log.mEnabledLevels.contains(LogLevel.WARNING)) {
            log.print(new LogData(LogLevel.WARNING, tag, Log.getStackTraceString(throwable)));
        }
    }
}

package com.phoenix.lib.log;

import android.support.annotation.NonNull;

import static java.util.Arrays.asList;

/**
 * date:   10/26/2014
 *
 * @author Dylan Jones
 */
public class Logger {
    private static Logger sInstance;
    private BaseLog mLogger;

    private Logger() {
        mLogger = new LogcatLogger();
    }

    private static BaseLog getLogger() {
        if (sInstance == null) {
            sInstance = new Logger();
        }

        return sInstance.mLogger;
    }

    private static Logger getInstance() {
        if (sInstance == null) {
            sInstance = null;
        }

        return sInstance;
    }

    public static void enableReleaseMode() {
        getLogger().enableReleaseMode();
    }

    public static boolean isLogLevelEnabled(LogLevel logLevel) {
        return getLogger().isLevelEnabled(logLevel);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void v(@NonNull String tag, @NonNull String msg) {
        LogHelper.v(getLogger(), tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void v(@NonNull String tag, @NonNull Throwable throwable, @NonNull String msg) {
        LogHelper.v(getLogger(), tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag    Tag for for the log data. Can be used to organize log statements.
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void v(@NonNull String tag, @NonNull String format, @NonNull Object... args) {
        LogHelper.v(getLogger(), tag, String.format(format, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#VERBOSE} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param format    the format string (see {@link java.util.Formatter#format})
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void v(@NonNull String tag, @NonNull Throwable throwable, @NonNull String format, @NonNull Object... args) {
        LogHelper.v(getLogger(), tag, String.format(format, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void i(@NonNull String tag, @NonNull String msg) {
        LogHelper.i(getLogger(), tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void i(@NonNull String tag, @NonNull Throwable throwable, @NonNull String msg) {
        LogHelper.i(getLogger(), tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag    Tag for for the log data. Can be used to organize log statements.
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void i(@NonNull String tag, @NonNull String format, @NonNull Object... args) {
        LogHelper.i(getLogger(), tag, String.format(format, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#INFO} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param format    the format string (see {@link java.util.Formatter#format})
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void i(@NonNull String tag, @NonNull Throwable throwable, @NonNull String format, @NonNull Object... args) {
        LogHelper.i(getLogger(), tag, String.format(format, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void d(@NonNull String tag, @NonNull String msg) {
        LogHelper.d(getLogger(), tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void d(@NonNull String tag, @NonNull Throwable throwable, @NonNull String msg) {
        LogHelper.d(getLogger(), tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag    Tag for for the log data. Can be used to organize log statements.
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void d(@NonNull String tag, @NonNull String format, @NonNull Object... args) {
        LogHelper.d(getLogger(), tag, String.format(format, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#DEBUG} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param format    the format string (see {@link java.util.Formatter#format})
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void d(@NonNull String tag, @NonNull Throwable throwable, @NonNull String format, @NonNull Object... args) {
        LogHelper.d(getLogger(), tag, String.format(format, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void e(@NonNull String tag, @NonNull String msg) {
        LogHelper.e(getLogger(), tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void e(@NonNull String tag, @NonNull Throwable throwable, @NonNull String msg) {
        LogHelper.e(getLogger(), tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag    Tag for for the log data. Can be used to organize log statements.
     * @param format The actual message to be logged.
     * @param args   for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void e(@NonNull String tag, @NonNull String format, @NonNull Object... args) {
        LogHelper.e(getLogger(), tag, String.format(format, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ERROR} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param format    the format string (see {@link java.util.Formatter#format})
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void e(@NonNull String tag, @NonNull Throwable throwable, @NonNull String format, @NonNull Object... args) {
        LogHelper.e(getLogger(), tag, String.format(format, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void wtf(@NonNull String tag, @NonNull String msg) {
        LogHelper.wtf(getLogger(), tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void wtf(@NonNull String tag, @NonNull Throwable throwable) {
        LogHelper.wtf(getLogger(), tag, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void wtf(@NonNull String tag, @NonNull Throwable throwable, @NonNull String msg) {
        LogHelper.wtf(getLogger(), tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag    Tag for for the log data. Can be used to organize log statements.
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void wtf(@NonNull String tag, @NonNull String format, @NonNull Object... args) {
        LogHelper.wtf(getLogger(), tag, String.format(format, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#ASSERT} priority.
     * Used as {@link String#format(String, Object...) format}.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param format    the format string (see {@link java.util.Formatter#format})
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     * @see {@link String#format(String, Object...)}
     */
    public static void wtf(@NonNull String tag, @NonNull Throwable throwable, @NonNull String format, @NonNull Object... args) {
        LogHelper.wtf(getLogger(), tag, String.format(format, asList(args)), throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void w(@NonNull String tag, @NonNull String msg) {
        LogHelper.w(getLogger(), tag, msg);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void w(@NonNull String tag, @NonNull Throwable throwable) {
        LogHelper.w(getLogger(), tag, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param msg       The actual message to be logged.
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     */
    public static void w(@NonNull String tag, @NonNull Throwable throwable, @NonNull String msg) {
        LogHelper.w(getLogger(), tag, msg, throwable);
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag    Tag for for the log data. Can be used to organize log statements.
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void w(@NonNull String tag, @NonNull String format, @NonNull Object... args) {
        LogHelper.w(getLogger(), tag, String.format(format, asList(args)));
    }

    /**
     * Prints a message at {@link com.phoenix.lib.log.LogLevel#WARNING} priority.
     *
     * @param tag       Tag for for the log data. Can be used to organize log statements.
     * @param format    the format string (see {@link java.util.Formatter#format})
     * @param throwable If an exception was thrown, this can be sent along for the logging facilities
     *                  to extract and print useful information.
     * @param args      for formatting the output
     *
     * @throws NullPointerException             if {@code format == null}
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void w(@NonNull String tag, @NonNull Throwable throwable, @NonNull String format, @NonNull Object... args) {
        LogHelper.w(getLogger(), tag, String.format(format, asList(args)), throwable);
    }

    public static class Builder {
        private static final Logger mLogger = getInstance();

        public Builder setLogger(BaseLog logger) {
            mLogger.mLogger = logger;
            return this;
        }

        public Builder disableLogLevel(LogLevel... levels) {
            mLogger.mLogger.disableLevels(levels);
            return this;
        }

        public void build() {
            sInstance = mLogger;
        }
    }

}

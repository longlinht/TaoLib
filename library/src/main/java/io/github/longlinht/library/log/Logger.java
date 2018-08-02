package io.github.longlinht.library.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import io.github.longlinht.library.utils.GlobalContext;


/**
 * Logger 工具类
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class Logger {

    public static final String TAG = "LOGGER";

    private Logger() {
        //no instance
    }

    public static final int LEVEL_DEBUG = 0b1111;
    public static final int LEVEL_INFO = 0b1110;
    public static final int LEVEL_WARN = 0b1100;
    public static final int LEVEL_ERROR = 0b1000;
    public static final int LEVEL_DISABLE_LOG = 0b0000;

    private static volatile int sLevel = LEVEL_DEBUG;

    public static void setLevel(int level) {
        sLevel = level;
    }

    public static void init() {
    }

    public static void init(String tag) {
    }

    public static void init(String tag, String logPath) {
    }

    /**
     * 打印debug级别的日志
     *
     * @param msg  支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args 需要填充格式化占位符的数据
     */
    public static void d(@NonNull String msg, @NonNull Object... args) {

        if (_isLoggable(LEVEL_DEBUG)) {
            Log.d(TAG, format(msg, args));
        }
    }

    /**
     * 记录debug级别的日志
     *
     * @param msg  支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args 需要填充格式化占位符的数据
     */
    public static void d(boolean isRecord, @NonNull String msg, @NonNull Object... args) {
        if (isRecord) {
            //LogRecorder.d(msg, args);
        } else {
            d(msg, args);
        }
    }

    /**
     * 打印debug级别的日志
     *
     * @param throwable 异常信息
     * @param msg       支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args      需要填充格式化占位符的数据
     */
    public static void d(@Nullable Throwable throwable, @NonNull String msg, @NonNull Object... args) {

        if (_isLoggable(LEVEL_DEBUG)) {
            Log.d(TAG, format(msg, args), throwable);
        }
    }

    /**
     * 打印info级别的日志
     *
     * @param msg  支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args 需要填充格式化占位符的数据
     */
    public static void i(@NonNull String msg, @NonNull Object... args) {

        if (_isLoggable(LEVEL_INFO)) {
            Log.i(TAG, format(msg, args));

        }
    }

    /**
     * 打印info级别的日志
     *
     * @param msg  支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args 需要填充格式化占位符的数据
     */
    public static void i(boolean isRecord, @NonNull String msg, @NonNull Object... args) {
        if (isRecord) {
            //LogRecorder.i(msg, args);
        } else {
            i(msg, args);
        }
    }

    /**
     * 打印info级别的日志
     *
     * @param throwable 异常信息
     * @param msg       支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args      需要填充格式化占位符的数据
     */
    public static void i(@Nullable Throwable throwable, @NonNull String msg, @NonNull Object... args) {

        if (_isLoggable(LEVEL_INFO)) {
            Log.i(TAG, format(msg, args), throwable);
        }
    }

    /**
     * 打印warn级别的日志
     *
     * @param msg  支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args 需要填充格式化占位符的数据
     */
    public static void w(@NonNull String msg, @NonNull Object... args) {

        if (_isLoggable(LEVEL_WARN)) {
            Log.w(TAG, format(msg, args));
        }
    }

    /**
     * 打印warn级别的日志
     *
     * @param msg  支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args 需要填充格式化占位符的数据
     */
    public static void w(boolean isRecord, @NonNull String msg, @NonNull Object... args) {
        if (isRecord) {
            //LogRecorder.w(msg, args);
        } else {
            w(msg, args);
        }
    }

    /**
     * 打印warn级别的日志
     *
     * @param throwable 异常信息
     * @param msg       支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args      需要填充格式化占位符的数据
     */
    public static void w(@Nullable Throwable throwable, @NonNull String msg, @NonNull Object... args) {

        if (_isLoggable(LEVEL_WARN)) {
            Log.w(TAG, format(msg, args), throwable);
        }
    }

    /**
     * 打印error级别的日志
     *
     * @param msg  支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args 需要填充格式化占位符的数据
     */
    public static void e(@NonNull String msg, @NonNull Object... args) {

        if (_isLoggable(LEVEL_ERROR)) {
            Log.e(TAG, format(msg, args));
        }
    }

    /**
     * 打印error级别的日志
     *
     * @param msg  支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args 需要填充格式化占位符的数据
     */
    public static void e(boolean isRecord, @NonNull String msg, @NonNull Object... args) {
        if (isRecord) {

        } else {
            e(msg, args);
        }
    }

    /**
     * 打印error级别的日志
     *
     * @param throwable 异常信息
     * @param msg       支持 %s 格式的占位符，注意不支持%d, %f等情况。
     * @param args      需要填充格式化占位符的数据
     */
    public static void e(@Nullable Throwable throwable, @NonNull String msg, @NonNull Object... args) {

        if (_isLoggable(LEVEL_ERROR)) {
            Log.e(TAG, format(msg, args), throwable);
        }
    }

    @VisibleForTesting
    static boolean _isLoggable(int level) {
        final int curLevel = Logger.sLevel;
        return GlobalContext.isDebug() && (curLevel & level) == level;
    }

    /**
     * Substitutes each {@code %s} in {@code template} with an argument. These are matched by
     * position: the first {@code %s} gets {@code args[0]}, etc. If there are more arguments than
     * placeholders, the unmatched arguments will be appended to the end of the format(ted message in
     * square braces.
     *
     * @param template a non-null string containing 0 or more {@code %s} placeholders.
     * @param args     the arguments to be substituted into the message template. Arguments are converted
     *                 to strings using {@link String#valueOf(Object)}. Arguments can be null.
     */
    // Note that this is somewhat-improperly used from Verify.java as well.
    public static String format(String template, @NonNull Object... args) {
        template = String.valueOf(template); // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}

package io.github.longlinht.library.android;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * 进程判断工具类
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */

public enum Processes {
    ;

    private static final String TAG = "Processes";


    // 注意这三个值不能随便改变，和后台活跃埋点紧密相关

    /**
     * 进程前台可见
     */
    public static final int STATE_FOREGROUND = 1;
    /**
     * 进程后台存活，前台不可见
     */
    public static final int STATE_BACKGROUND = 2;
    /**
     * 进程已经死亡
     */
    public static final int STATE_DEAD = 3;

    @IntDef({STATE_BACKGROUND, STATE_DEAD, STATE_FOREGROUND})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    /**
     * 获取进程状态
     *
     * @param processName 要查看状态的进程名字
     * @return one of {@link Processes#STATE_BACKGROUND},  {@link Processes#STATE_FOREGROUND},  {@link Processes#STATE_DEAD}
     */
    @State
    public static int getProcessState(Context context, String processName) {

        if (isProcessShowing(context, processName)) {
            return STATE_FOREGROUND;
        }

        if (isProcessRunning(context, processName)) {
            return STATE_BACKGROUND;
        }

        return STATE_DEAD;
    }

    /**
     * 判断进程是否显示
     * <p>
     * 只有存在界面的进程才有可能正在显示
     */
    public static boolean isProcessShowing(Context context, String processName) {
        final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // 因为我们只要判断自己是否在前台，这个方法依然可以使用，参考 ActivityManager#getRunningTask的注释
        List<ActivityManager.RunningTaskInfo> tasksInfo = manager.getRunningTasks(1);

        if (tasksInfo != null && tasksInfo.size() > 0) {
            ComponentName componentName = tasksInfo.get(0).topActivity;

            if (processName.equals(componentName.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断进程是否正在运行
     */
    public static boolean isProcessRunning(Context context, String processName) {
        final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();

        if (processes == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo process : processes) {
            if (TextUtils.equals(process.processName, processName)) {
                return true;
            }
        }

        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // copy form facebook ProcessUtil
    ///////////////////////////////////////////////////////////////////////////

    private static String sProcessName;
    private static boolean sProcessNameRead;

    /**
     * 获取进程名字
     */
    @Nullable
    public static synchronized String getProcessName() {
        if (!sProcessNameRead) {
            sProcessNameRead = true;

            try {
                sProcessName = readProcessName();
            } catch (IOException var1) {
                ;
            }
        }

        return sProcessName;
    }

    private static String readProcessName() throws IOException {
        byte[] cmdlineBuffer = new byte[64];
        FileInputStream stream = new FileInputStream("/proc/self/cmdline");
        boolean success = false;

        String var5;
        try {
            int n = stream.read(cmdlineBuffer);
            success = true;
            int endIndex = indexOf(cmdlineBuffer, 0, n, (byte) 0);
            var5 = new String(cmdlineBuffer, 0, endIndex > 0 ? endIndex : n);
        } finally {
            close(stream, !success);
        }

        return var5;
    }

    private static void close(Closeable closeable, boolean hideException) throws IOException {
        if (closeable != null) {
            if (hideException) {
                try {
                    closeable.close();
                } catch (IOException var3) {
                    Log.e(TAG, "Hiding IOException because another is pending", var3);
                }
            } else {
                closeable.close();
            }
        }

    }

    private static int indexOf(byte[] haystack, int offset, int length, byte needle) {
        for (int i = 0; i < haystack.length; ++i) {
            if (haystack[i] == needle) {
                return i;
            }
        }

        return -1;
    }
}

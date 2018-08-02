package io.github.longlinht.library.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.job.JobScheduler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import io.github.longlinht.library.annotation.NotThreadSafe;
import io.github.longlinht.library.annotation.ThreadSafe;
import io.github.longlinht.library.guava.Supplier;

import static io.github.longlinht.library.guava.Suppliers.memoize;
import static io.github.longlinht.library.guava.Suppliers.synchronizedSupplier;

/**
 * 全局对象
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
@ThreadSafe
public final class GlobalContext {

    private GlobalContext() {
        //no instance
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 这两个是应用全局对象，和进程生命周期一致，所以静态持有是没有问题的
     */
    @SuppressLint("StaticFieldLeak")
    private volatile static Application application;

    @SuppressLint("StaticFieldLeak")
    private volatile static Context appContext;

    public static void setAppContext(Context appContext) {
        GlobalContext.appContext = appContext;
    }

    public static void setApplication(Application application) {
        GlobalContext.application = application;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    public static Context getAppContext() {
        return appContext;
    }

    @NonNull
    public static Application getApplication() {
        return application;
    }

    public static String getPackageName() {
        return appContext.getPackageName();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 
    ///////////////////////////////////////////////////////////////////////////

    public static ContentResolver getContentResolver() {
        return appContext.getContentResolver();
    }

    @NonNull
    public static Resources getResources() {
        return appContext.getResources();
    }

    public static WindowManager getWindowManager() {
        return ((WindowManager) getAppContext().getSystemService(Context.WINDOW_SERVICE));
    }

    @TargetApi(21)
    public static JobScheduler getJobScheduler() {
        return (JobScheduler) GlobalContext.getAppContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public static ActivityManager getActivityManager() {
        return ((ActivityManager) getAppContext().getSystemService(Context.ACTIVITY_SERVICE));
    }

    public static NotificationManager getNotificationManager() {
        return ((NotificationManager) getAppContext().getSystemService(Context.NOTIFICATION_SERVICE));
    }

    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @SuppressLint("WifiManagerLeak")
    public static WifiManager getWifiManager() {
        // 我们这里使用了AppContext 所以没有问题
        return (WifiManager) getAppContext().getSystemService(Context.WIFI_SERVICE);
    }

    public static PackageManager getPackageManager() {
        return getAppContext().getPackageManager();
    }

    public static AlarmManager getAlarmManager() {
        return ((AlarmManager) getAppContext().getSystemService(Context.ALARM_SERVICE));
    }

    public static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取res中的字符串.
     *
     * @param resID      the res id
     * @param formatArgs the format args
     * @return the string
     */
    public static String getString(@StringRes int resID, Object... formatArgs) {
        return getAppContext().getResources().getString(resID, formatArgs);
    }

    /**
     * 获取res中的字符串.
     *
     * @param resID the res id
     * @return the string
     */
    public static String getString(@StringRes int resID) {
        return getAppContext().getResources().getString(resID);
    }

    public static ApplicationInfo getApplicationInfo() {
        return appContext.getApplicationInfo();
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    private static final Supplier<Boolean> IS_DEBUG = synchronizedSupplier(memoize(new Supplier<Boolean>() {
        @Override
        public Boolean get() {
            final ApplicationInfo info = getApplicationInfo();
            return info != null && (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }));

    /**
     * 注意最好使用 BuildConfig.DEBUG, 因为BuildConfig.DEBUG 是常量，编译时可以消除没用分支。
     * release 版本不会带有被 BuildConfig 包裹的代码
     * <p>
     * 这个实现需要运行时信息，不能实现这个优化
     */
    public static boolean isDebug() {
        return isDebuggable && IS_DEBUG.get();
    }

    private static volatile boolean isDebuggable = true;

    public static void setIsDebuggable(boolean isDebuggable) {
        GlobalContext.isDebuggable = isDebuggable;
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    public static DisplayMetrics getDisplayMetrics() {
        return getAppContext().getResources().getDisplayMetrics();
    }

    @NotThreadSafe
    public static Size getScreenSize() {
        final DisplayMetrics metrics = getDisplayMetrics();
        return new Size(metrics.widthPixels, metrics.heightPixels);
    }
}

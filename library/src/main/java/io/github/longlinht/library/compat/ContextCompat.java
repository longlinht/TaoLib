package io.github.longlinht.library.compat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.github.longlinht.library.log.Logger;
import io.github.longlinht.library.utils.GlobalContext;

/**
 * Context 功能适配
 * <p>
 * 会针对国内机型的一些情况进行适配，
 * 例如oppo手机后台一段时间过后自启动会直接抛出异常，这里对这个情况进行了catch
 * <p>
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class ContextCompat {

    private ContextCompat() {
        //no instance
    }

    /**
     * 启动Service 并 swallow 所有异常
     * <p>
     */
    public static void startService(@NonNull Context context, @NonNull Intent intent) {
        startService(context, intent, true);
    }

    /**
     * 启动service，swallow 不能自动启动的异常，并可以指定是否swallow其它异常
     *
     * @param context              Context
     * @param intent               要启动的intent
     * @param swallowAllExceptions 是否Swallow 所有异常
     */
    public static void startService(@NonNull Context context, @NonNull Intent intent, boolean swallowAllExceptions) {

        try {
            context.startService(intent);
        } catch (Exception e) {

            if (shouldSwallow(e)) {
                Logger.e(e, "startService 过程中发生异常，intent：%s", intent);
                return;
            }

            if (swallowAllExceptions) {
                Logger.e(e, "startService 过程中发生异常，intent: %s", intent);
                return;
            }

            throw e;
        }
    }

    /**
     * 发送广播
     *
     * @param intent               广播intent
     * @param receiverPermission   receiver 需要的全县
     * @param swallowAllExceptions 是否swallow所有的异常
     */
    public static void sendBroadcast(@NonNull Intent intent,
                                     @Nullable String receiverPermission,
                                     boolean swallowAllExceptions) {
        try {
            GlobalContext.getAppContext().sendBroadcast(intent, receiverPermission);
        } catch (Exception e) {
            if (shouldSwallow(e)) {
                Logger.e(e, "sendBroadcast 发生异常 intent：%s, permission:%s", intent, receiverPermission);
                return;
            }

            if (swallowAllExceptions) {
                Logger.e(e, "sendBroadcast 发生异常 intent：%s, permission:%s", intent, receiverPermission);
                return;
            }

            throw e;
        }
    }

    /**
     * 发送广播
     *
     * @param intent               要发送的intent
     * @param swallowAllExceptions 是否吃掉所有的异常
     */
    public static void sendBroadcast(@NonNull Intent intent, boolean swallowAllExceptions) {
        try {
            GlobalContext.getAppContext().sendBroadcast(intent);
        } catch (Exception e) {
            if (shouldSwallow(e)) {
                return;
            }

            if (swallowAllExceptions) {
                Logger.e(e, "sendBroadcast 发生异常 intent：%s", intent);
                return;
            }

            throw e;
        }
    }

    /**
     * 发送广播，并swallow全部异常
     *
     * @param intent 要发送的广播Intent
     */
    public static void sendBroadcast(@NonNull Intent intent) {
        sendBroadcast(intent, true);
    }

    private static boolean shouldSwallow(Exception e) {
        /*
         * 由于coloros的OPPO手机自动熄屏一段时间后，
         * 会启用系统自带的电量优化管理，
         * 禁止一切自启动的APP（用户设置的自启动白名单除外），需要try catch
         */
        return (Rom.IS_OPPO && e instanceof SecurityException);
    }

}

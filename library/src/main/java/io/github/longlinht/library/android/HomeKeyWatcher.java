package io.github.longlinht.library.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

import io.github.longlinht.library.annotation.GuardedBy;
import io.github.longlinht.library.annotation.ThreadSafe;
import io.github.longlinht.library.annotation.Thunk;
import io.github.longlinht.library.guava.Supplier;
import io.github.longlinht.library.utils.GlobalContext;

import static io.github.longlinht.library.guava.Preconditions.checkNotNull;
import static io.github.longlinht.library.guava.Suppliers.memoize;


/**
 * home键监听
 * <p>
 * http://blog.csdn.net/way_ping_li/article/details/8953622
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 */
@ThreadSafe
public class HomeKeyWatcher {

    private HomeKeyWatcher() {
        //no instance
    }

    private static final IntentFilter homeKeyFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

    @GuardedBy("homeKeyListeners")
    private static final Supplier<BroadcastReceiver> receiverSupplier = memoize(new Supplier<BroadcastReceiver>() {
        @Override
        public BroadcastReceiver get() {
            return new InnerReceiver();
        }
    });

    @GuardedBy("homeKeyListeners")
    private static volatile boolean isWatchingHomeKey = false;

    @GuardedBy("homeKeyListeners")
    private static final Set<HomeKeyListener> homeKeyListeners = new HashSet<>();

    /**
     * home键 监听
     */
    public interface HomeKeyListener {

        /**
         * 点击home键
         */
        void onHomePressed();
    }

    /**
     * 注册Home键监听器，如果没有监听，自动开始监听
     */
    public static void registerHomeWatcher(@NonNull HomeKeyListener listener) {

        checkNotNull(listener);

        synchronized (homeKeyListeners) {

            homeKeyListeners.add(listener);

            if (!isWatchingHomeKey) {
                GlobalContext.getAppContext().registerReceiver(receiverSupplier.get(), homeKeyFilter);
                isWatchingHomeKey = true;
            }
        }
    }

    /**
     * 反注册Home键监听，当没有监听器的时候，会自动取消监听注册
     */
    public static void unregisterHomeWatcher(@NonNull HomeKeyListener listener) {

        checkNotNull(listener);

        synchronized (homeKeyListeners) {

            homeKeyListeners.remove(listener);

            if (homeKeyListeners.isEmpty() && isWatchingHomeKey) {
                GlobalContext.getAppContext().unregisterReceiver(receiverSupplier.get());
                isWatchingHomeKey = false;
            }
        }
    }

    /**
     * 是否正在监听 Home键点击
     *
     * 监听和取消监听是自动的，不需要关心
     */
    public static boolean isWatchingHomeKey() {
        synchronized (homeKeyListeners) {
            return isWatchingHomeKey;
        }
    }

    @Thunk
    static void dispatchHomeKeyPressed() {

        final Set<HomeKeyListener> listeners;

        synchronized (homeKeyListeners) {
            if (homeKeyListeners.isEmpty()) {
                return;
            }

            listeners = new HashSet<>(homeKeyListeners);
        }

        for (HomeKeyListener listener : listeners) {
            listener.onHomePressed();
        }
    }

    @Thunk
    static class InnerReceiver extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                return;
            }

            final String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            // home键监听
            if (reason != null && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                dispatchHomeKeyPressed();
            }
        }
    }
}

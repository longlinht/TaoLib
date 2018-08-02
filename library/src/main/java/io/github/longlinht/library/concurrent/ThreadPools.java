package io.github.longlinht.library.concurrent;

import android.os.HandlerThread;
import android.os.Process;

import io.github.longlinht.library.guava.Supplier;
import io.github.longlinht.library.guava.Suppliers;

/**
 * 项目中的统一线程池实现
 * 针对Android 平台进行优化
 * 针对IO 和 Computation 类型的线程池进行优化
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */

public class ThreadPools {

    /**
     * IO 线程池
     */
    public static final Supplier<TaoThreadPoolExecutor> IO_THREAD_POOL = Suppliers.synchronizedSupplier(Suppliers.memoize(new Supplier<TaoThreadPoolExecutor>() {
        @Override
        public TaoThreadPoolExecutor get() {
            return AndroidExecutors.newCacheThreadPool(new TaoThreadFactory("IoThreadPool-", Process.THREAD_PRIORITY_BACKGROUND, false));
        }
    }));

    /**
     * Computation 线程池
     */
    public static final Supplier<TaoThreadPoolExecutor> COMPUTATION_THREAD_POOL = Suppliers.synchronizedSupplier(Suppliers.memoize(new Supplier<TaoThreadPoolExecutor>() {
        @Override
        public TaoThreadPoolExecutor get() {
            return AndroidExecutors.newFixedThreadPool(AndroidExecutors.CPU_COUNT, new TaoThreadFactory("ComputationThreadPool-", Process.THREAD_PRIORITY_BACKGROUND, false));
        }
    }));

    /**
     * 轻量级的后台线程
     */
    public static final Supplier<HandlerThread> LIGHT_WORK_BG_THREAD = Suppliers.synchronizedSupplier(Suppliers.memoize(new Supplier<HandlerThread>() {
        @Override
        public HandlerThread get() {
            final HandlerThread handlerThread = new HandlerThread("LightWorkBgThread", Process.THREAD_PRIORITY_BACKGROUND);
            handlerThread.start();
            return handlerThread;
        }
    }));

    /**
     * 设置后台线程优先级
     */
    public static void setBgThreadPriority() {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        } catch (Throwable e) {
            // just for safe
        }
    }

    private ThreadPools() {
        //no instance
    }


}

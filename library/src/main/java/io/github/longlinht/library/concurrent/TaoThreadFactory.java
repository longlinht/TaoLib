package io.github.longlinht.library.concurrent;

import android.os.Process;
import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ThreadFactory
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class TaoThreadFactory extends AtomicLong implements ThreadFactory {

    public static final int THREAD_PRIORITY = 3;
    private final int mThreadPriority;
    private final String mPrefix;
    private final boolean mDaemon;

    /**
     * Creates a new InkeThreadFactory with a given priority.
     * <p>
     *
     * @param threadPriority This value should be set to a value compatible with
     *                       {@link android.os.Process#setThreadPriority}, not {@link Thread#setPriority}.
     * @param daemon         是否设置为daemon
     * @param prefix         threadName 前缀
     */
    public TaoThreadFactory(String prefix, int threadPriority, boolean daemon) {
        mThreadPriority = threadPriority;
        mPrefix = prefix;
        mDaemon = daemon;
    }

    @Override
    public Thread newThread(@NonNull final Runnable runnable) {
        Runnable wrapperRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Process.setThreadPriority(mThreadPriority);
                } catch (Throwable t) {
                    // just to be safe
                }

                runnable.run();
            }
        };

        final Thread thread = new Thread(wrapperRunnable, mPrefix + incrementAndGet());
        if (mDaemon) {
            thread.setDaemon(true);
        }

        /*
         java 的线程优先级：1-10, normal 是 5
         Android 平台上，主要通过Process.THREAD_PRIORITY_*设置
         Thread.MIN_PRIORITY == Process.THREAD_PRIORITY_LOWEST
         Thread.NORM_PRIORITY == Process.THREAD_PRIORITY_DEFAULT
         Thread.MAX_PRIORITY == Process.THREAD_PRIORITY_URGENT_DISPLAY

         对后台线程来说，3是一个比较合理的值
          */

        thread.setPriority(THREAD_PRIORITY);

        return thread;
    }
}

package io.github.longlinht.library.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 为Android优化的线程池工厂类
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class AndroidExecutors {

    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int MAX_CPU_COUNT = CPU_COUNT * 2 + 1;
    public static final long KEEP_ALIVE_TIME_IN_MINUTES = 30L;

    /*
    * 内存优化方案1：
    * 1. 保持核心线程始终存活
    * 2. 将非核心线程的存活时间延长，避免频繁创建新的线程
    *
    * 可以避免频繁创建线程的内存分配负担和内存碎片问题
    *
    * 内存优化方案2：
    * 1. 核心线程也可以回收
    * 2. 非核心线程存活时间变短，及时回收资源
    *
    * 可以避免内存峰值过高的问题，及时回落
    *
    */

    /**
     * Creates a proper fix Thread Pool. Tasks will reuse cached threads if available
     * or create new threads until the core pool is full. tasks will then be queued. If an
     * task cannot be queued, a new thread will be created unless this would exceed max pool
     * size, then the task will be rejected. Threads will time out after 1 second.
     * <p>
     * Core thread timeout is only available on android-9+.
     *
     * @param threadFactory the factory to use when creating new threads
     * @return the newly created thread pool
     */
    public static TaoThreadPoolExecutor newFixedThreadPool(int count, ThreadFactory threadFactory) {

//        executor.allowCoreThreadTimeOut(true);

        return new TaoThreadPoolExecutor(
                count,
                count,
                KEEP_ALIVE_TIME_IN_MINUTES, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);
    }

    /**
     * Creates a proper fix Thread Pool. Tasks will reuse cached threads if available
     * or create new threads until the core pool is full. tasks will then be queued. If an
     * task cannot be queued, a new thread will be created unless this would exceed max pool
     * size, then the task will be rejected. Threads will time out after 1 second.
     * <p>
     * Core thread timeout is only available on android-9+.
     *
     * @param threadFactory the factory to use when creating new threads
     * @return the newly created thread pool
     */
    public static TaoThreadPoolExecutor newCacheThreadPool(ThreadFactory threadFactory) {

//        executor.allowCoreThreadTimeOut(true);

        return new TaoThreadPoolExecutor(
                CPU_COUNT,
                MAX_CPU_COUNT,
                KEEP_ALIVE_TIME_IN_MINUTES, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);
    }
}

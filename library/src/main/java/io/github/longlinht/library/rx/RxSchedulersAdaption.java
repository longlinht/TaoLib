package io.github.longlinht.library.rx;

import io.github.longlinht.library.concurrent.ThreadPools;
import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;


/**
 * 对RxJava 线程池适配
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class RxSchedulersAdaption {

    private RxSchedulersAdaption() {
        //no instance
    }

    /**
     * 为Android 进行优化
     * 注意需要在进程启动的第一时间进行调用
     */
    public static void optimizeSchedulerForAndroid() {
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {

            @Override
            public Scheduler getIOScheduler() {
                /**
                 * RxJava 的computation Scheduler {@link rx.internal.schedulers.EventLoopsScheduler} 会创建固定为CPU数量个数的线程
                 * io scheduler {@link rx.internal.schedulers.CachedThreadScheduler} 是类似cache类型的线程池，数量没有上限，每个线程保持 空闲存活时间是 60S
                 *
                 * io scheduler 不太适合移动设备资源短缺的现状，极限情况下（短时间内大量的io请求）会造成线程数量飙升
                 *
                 */
                return Schedulers.from(ThreadPools.IO_THREAD_POOL.get());
            }

            @Override
            public Scheduler getComputationScheduler() {
                return Schedulers.from(ThreadPools.COMPUTATION_THREAD_POOL.get());
            }
        });
    }
}

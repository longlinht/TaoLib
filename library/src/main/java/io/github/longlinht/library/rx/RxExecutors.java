package io.github.longlinht.library.rx;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 针对RxJava Scheduler 封装的类似线程池的接口
 * 1. 方便现有线程，线程池API迁移
 * 2. 针对简单的异步任务提供便利
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public enum RxExecutors implements Executor {

    Io {
        @Override
        Scheduler.Worker createWorker() {
            return Schedulers.io().createWorker();
        }
    },

    Computation {
        @Override
        Scheduler.Worker createWorker() {
            return Schedulers.computation().createWorker();
        }
    };

    abstract Scheduler.Worker createWorker();

    public Subscription submit(@NonNull Action0 action) {
        return createWorker().schedule(action);
    }

    public Subscription submit(@NonNull Runnable action) {
        return submit(RxAdapters.call(action));
    }

    public Subscription delay(@NonNull Action0 action, int delay, TimeUnit unit) {
        return createWorker().schedule(action, delay, unit);
    }

    public Subscription delay(@NonNull Runnable action, int delay, TimeUnit unit) {
        return delay(RxAdapters.call(action), delay, unit);
    }

    public Subscription schedulePeriodically(@NonNull Action0 action0, int initialDelay, int period, TimeUnit unit) {
        return createWorker().schedulePeriodically(action0, initialDelay, period, unit);
    }

    public Subscription schedulePeriodically(@NonNull Runnable action0, int initialDelay, int period, TimeUnit unit) {
        return schedulePeriodically(RxAdapters.call(action0), initialDelay, period, unit);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        submit(RxAdapters.call(command));
    }
}

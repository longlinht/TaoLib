package io.github.longlinht.library.concurrent.timer;


import java.util.concurrent.TimeUnit;

import io.github.longlinht.library.rx.RxExecutors;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static io.github.longlinht.library.guava.Preconditions.checkArgument;

/**
 * 使用 RxExecutors 构造的Timer
 *
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */

public class Timer {

    public Timer() {
    }

    private static final long INT_MASK = 0xFF_FF_FF_FFL;

    private final Object lock = new Object();
    private volatile CompositeSubscription mSubscriptions;

    /**
     * Schedule a task for single execution after a specified delay.
     *
     * @param task  the task to schedule.
     * @param delay amount of time in milliseconds before execution.
     * @throws IllegalArgumentException if {@code delay < 0}.
     * @throws IllegalStateException    if the {@code Timer} has been canceled, or if the task has been
     *                                  scheduled or canceled.
     */
    public Subscription schedule(TimerTask task, long delay) {

        checkArgument((delay & INT_MASK) == delay);

        final Subscription delaySub = RxExecutors.Computation.delay(task, ((int) delay), TimeUnit.MILLISECONDS);

        CompositeSubscription subscriptions = mSubscriptions;
        if (subscriptions == null) {
            synchronized (lock) {
                mSubscriptions = subscriptions = new CompositeSubscription();
            }
        }

        subscriptions.add(delaySub);

        task.attach(subscriptions, delaySub);
        return delaySub;
    }

    /**
     * Schedule a task for repeated fixed-delay execution after a specific delay.
     *
     * @param task   the task to schedule.
     * @param delay  amount of time in milliseconds before first execution.
     * @param period amount of time in milliseconds between subsequent executions.
     * @throws IllegalArgumentException if {@code delay < 0} or {@code period <= 0}.
     * @throws IllegalStateException    if the {@code Timer} has been canceled, or if the task has been
     *                                  scheduled or canceled.
     */
    public Subscription schedule(TimerTask task, long delay, long period) {

        checkArgument((delay & INT_MASK) == delay);
        checkArgument((period & INT_MASK) == period);

        final Subscription periodSub = RxExecutors.Computation.schedulePeriodically(task, ((int) delay), ((int) period), TimeUnit.MILLISECONDS);

        CompositeSubscription subscriptions = mSubscriptions;
        if (subscriptions == null) {
            synchronized (lock) {
                mSubscriptions = subscriptions = new CompositeSubscription();
                subscriptions.add(periodSub);
            }
        }

        task.attach(subscriptions, periodSub);
        return periodSub;
    }

    /**
     * Cancels the {@code Timer} and all scheduled tasks. If there is a
     * currently running task it is not affected. No more tasks may be scheduled
     * on this {@code Timer}. Subsequent calls do nothing.
     */
    public void cancel() {

        if (mSubscriptions != null) {

            synchronized (lock) {
                if (mSubscriptions != null && !mSubscriptions.isUnsubscribed()) {
                    mSubscriptions.unsubscribe();
                    mSubscriptions = null;
                }
            }
        }

    }
}

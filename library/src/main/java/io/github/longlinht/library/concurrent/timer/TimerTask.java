package io.github.longlinht.library.concurrent.timer;

import android.support.annotation.NonNull;

import io.github.longlinht.library.annotation.GuardedBy;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static io.github.longlinht.library.guava.Preconditions.checkState;


/**
 * 和 Timer 配合使用
 *
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public abstract class TimerTask implements Runnable {

    @GuardedBy("this")
    private volatile Subscription subscription;
    @GuardedBy("this")
    private volatile CompositeSubscription compositeSubscription;

    void attach(@NonNull CompositeSubscription compositeSubscription, @NonNull Subscription subscription) {

        checkState(this.subscription == null);

        this.subscription = subscription;
        this.compositeSubscription = compositeSubscription;
    }

    /**
     * 取消TimerTask
     */
    public synchronized void cancel() {

        if (subscription != null) {
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();

                if (compositeSubscription != null) {
                    compositeSubscription.remove(subscription);
                }
            }
        }
    }
}

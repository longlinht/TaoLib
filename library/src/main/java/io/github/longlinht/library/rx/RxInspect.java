package io.github.longlinht.library.rx;

import io.github.longlinht.library.log.Logger;
import rx.Scheduler;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;

/**
 * 使用RxJavaHooks 做一些DEBUG情况下的检测，日志工作
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class RxInspect {

    private RxInspect() {
        //no instance
    }

    public static void inspect() {
        RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                Logger.i(new RuntimeException("NEW_THREAD"), "call: [调用了Schedulers.newThread(), 在Android 设备上，这不是一个好的选择，慎重]");
                return scheduler;
            }
        });

        RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                Logger.i("call: Schedulers.io called");
                return scheduler;
            }
        });

        RxJavaHooks.setOnComputationScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                Logger.i("call: Schedulers.computation called");
                return scheduler;
            }
        });
    }
}

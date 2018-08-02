package io.github.longlinht.library.rx;

import android.support.annotation.NonNull;

import rx.functions.Action0;

/**
 * RxJava 函数式接口转换工具类
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
class RxAdapters {

    private RxAdapters() {
        //no instance
    }

    public static Action0 call(@NonNull final Runnable action) {
        return new Action0() {
            @Override
            public void call() {
                action.run();
            }
        };
    }

}

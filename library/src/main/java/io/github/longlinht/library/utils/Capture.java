package io.github.longlinht.library.utils;

import android.support.annotation.NonNull;

/**
 * 捕获器，适合配合回调接口使用（回调接口只能访问final的局部变量, 因此需要间接改变引用）
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class Capture<T> {

    @NonNull
    public static <T> Capture<T> of(T value) {
        return new Capture<>(value);
    }

    private volatile T value;

    private Capture(T value) {
        this.value = value;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

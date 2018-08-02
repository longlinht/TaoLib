package io.github.longlinht.library.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为非线程安全，没有强制作用，仅仅用作API发布者标记API的线程安全特性为非线程安全
 *
 * 当用于标记class时，表明整个类的public API都是非线程安全的
 * 当用于标记field，method 和 constructor 时, 表明这些方法，构造器 和 field 是非线程安全的
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface NotThreadSafe {
}

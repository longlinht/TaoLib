package io.github.longlinht.library.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记某个成员变量应该由哪把锁进程保护
 * <p>
 * 同一个变量应该使用由同一把锁进行保护
 * <p>
 * 注意这个注解没有任何运行时效果，仅仅用于coder 表明自己的多线程设计意图
 * <p>
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface GuardedBy {

    String value();
}

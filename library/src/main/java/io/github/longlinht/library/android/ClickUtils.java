package io.github.longlinht.library.android;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import static io.github.longlinht.library.guava.Preconditions.checkArgument;
import static io.github.longlinht.library.utils.GlobalContext.isDebug;

/**
 * 检查快速点击工具类
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class ClickUtils {


    private static final Map<Integer, Long> viewLastClickMaps = new HashMap<>();

    /**
     * 是否是快速点击
     * <p>
     * 注意快速点击是相对于全局的
     */
    public static boolean isFastDoubleClick(@NonNull Object view) {
        return isFastDoubleClick(500L, view);
    }

    /**
     * 是否是正常点击（非快速双击）
     * <p>
     * 注意快速点击是相对于全局的
     */
    public static boolean isCommonClick(@NonNull Object view) {
        return !isFastDoubleClick(500L, view);
    }

    /**
     * 根据指定的时间间隔判断 是否是快速点击
     *
     * @param gap 时间间隔
     */
    public static boolean isFastDoubleClick(@IntRange(from = 1) long gap, @NonNull Object view) {

        // 防止错误调用
        if (isDebug()) {
            checkArgument(!(view instanceof Long));
            checkArgument(!(view instanceof Integer));
        }

        final long current = System.currentTimeMillis();

        final int viewId = getViewId(view);

        final long lastClickTime = viewLastClickMaps.containsKey(viewId) ? viewLastClickMaps.get(viewId) : 0L;
        final long span = current - lastClickTime;

        if (span >= 0 && span <= gap) {
            return true;
        }
        viewLastClickMaps.put(viewId, current);
        return false;
    }

    private static int getViewId(Object view) {
        return System.identityHashCode(view);
    }
}

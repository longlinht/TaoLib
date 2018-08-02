package io.github.longlinht.library.compat;

import android.os.Build;

/**
 * 硬件加速适配
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */

public class HardwareAcceleratorCompat {

    private HardwareAcceleratorCompat() {
        //no instance
    }

    /**
     * 是否应该在WebView中禁止硬件加速
     *
     * https://www.coder4.com/archives/4981
     */
    public static boolean shouldDisableHardwareRenderInWebView() {
        // case 1: samsung GS4 on android 4.3 is know to cause crashes at libPowerStretch.so:0x2d4c
        // use GT-I95xx to match more GS4 series devices though GT-I9500 is the typical device
        final boolean isSamsungGs4 = android.os.Build.MODEL != null && android.os.Build.MODEL.contains("GT-I95")
                && android.os.Build.MANUFACTURER != null && android.os.Build.MANUFACTURER.equals("samsung");
        final boolean isJbMr2 = Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2;
        if (isSamsungGs4 && isJbMr2) {
            return true;
        }
        return false;
    }
}

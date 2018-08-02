package io.github.longlinht.library.compat;

import android.os.Build;

/**
 * 根据Build.BRAND 字段判断Rom 类型
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class Rom {

    private Rom() {
        //no instance
    }

    /**
     * 是否是Oppo
     */
    public static final boolean IS_OPPO;
    /**
     * 是否是Vivo
     */
    public static final boolean IS_VIVO;
    /**
     * 是否是华为，注意不包括华为荣耀
     */
    public static final boolean IS_HUAWEI;
    /**
     * 是否是华为荣耀
     */
    public static final boolean IS_HUAWEI_HONOR;
    /**
     * 是否是三星
     */
    public static final boolean IS_SAMSUNG;
    /**
     * 是否是努比亚
     */
    public static final boolean IS_NUBIA;

    static {

        final String brand = Build.BRAND.toUpperCase();

        IS_OPPO = brand.equalsIgnoreCase("OPPO");
        IS_VIVO = brand.equalsIgnoreCase("VIVO");
        IS_HUAWEI = brand.equalsIgnoreCase("HUAWEI");

        IS_HUAWEI_HONOR = brand.contains("HONOR");
        IS_SAMSUNG = brand.contains("SAMSUNG");

        IS_NUBIA = brand.contains("NUBIA");
    }

}

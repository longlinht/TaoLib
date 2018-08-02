package io.github.longlinht.library.utils.constants;

/**
 * byte工具类，常量定义
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class Bytes {

    private Bytes() {
        //no instance
    }

    /**
     * empty object
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * KB 单位
     */
    public static final int KB = 1024;

    /**
     * MB 单位
     */
    public static final int MB = KB * KB;

    /**
     * GB 单位
     */
    public static final int GB = KB * KB * KB;

}

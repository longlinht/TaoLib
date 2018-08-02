package io.github.longlinht.library.utils.constants;

import java.nio.charset.Charset;

/**
 * char 工具类，常量定义
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class Chars {

    private Chars() {
        //no instance
    }

    /**
     * utf-8 字符集
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * 空字符数组
     */
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
}

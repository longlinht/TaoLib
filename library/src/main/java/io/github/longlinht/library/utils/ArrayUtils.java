package io.github.longlinht.library.utils;

/**
 * 数组工具类
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class ArrayUtils {

    /**
     * 在数组中查找某个元素第一次出现的问题
     *
     * @param array 要查找的array
     * @param find  要查找的元素
     * @return 第一次出现的下表，如果不存在，返回-1
     */
    public static <T> int indexOf(T[] array, T find) {
        if (array == null) return -1;

        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], find)) {
                return i;
            }
        }

        return -1;
    }

}

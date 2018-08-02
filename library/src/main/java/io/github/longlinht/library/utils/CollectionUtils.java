package io.github.longlinht.library.utils;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.github.longlinht.library.guava.Preconditions.checkNotNull;

/**
 * CollectionUtils
 *
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class CollectionUtils {

    private CollectionUtils() {
        throw new AssertionError();
    }

    /**
     * is null or its size is 0
     * <p/>
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1})    =   false;
     * </pre>
     *
     * @return if collection is null or its size is 0, return true, else return false.
     */
    public static <V> boolean isEmpty(Collection<V> c) {
        return (c == null || c.isEmpty());
    }


    /**
     * 使用二元组构建map
     *
     * @param kvPair 二元组序列
     */
    @SafeVarargs
    public static <K, V> Map<K, V> dict(@NonNull Pair<K, V>... kvPair) {
        final LinkedHashMap<K, V> result = new LinkedHashMap<>();
        for (Pair<K, V> pair : kvPair) {
            result.put(checkNotNull(pair.first), pair.second);
        }

        return Collections.unmodifiableMap(result);
    }

    /**
     * 构建二元组
     */
    public static <K, V> Pair<K, V> pair(@NonNull K first, V second) {
        return Pair.create(checkNotNull(first), second);
    }
}

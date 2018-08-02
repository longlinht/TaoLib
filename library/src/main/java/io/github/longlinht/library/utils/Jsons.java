package io.github.longlinht.library.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.longlinht.library.log.Logger;

/**
 * 基于 GSON 的 json 解析，序列化工具类
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class Jsons {

    private Jsons() {
        //no instance
    }

    private static final Gson gson = new Gson();

    /**
     * 将一个对象转换为json
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * 将json解析为对象
     *
     * @param json  json数据
     * @param clazz 要解析的对象的数据类型
     */
    @Nullable
    public static <E> E parseJson(@NonNull String json, @NonNull Class<E> clazz) {

        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {

            Logger.e(e, "解析json过程中发生异常: json: %s, class: %s", json, clazz);

            return null;
        }
    }

    /**
     * 将json转换为list
     *
     * @param json  json数据
     * @param clazz 要转换的元素的类型
     */
    @Nullable
    public static <E> List<E> parseJson2List(@NonNull String json, Class<E> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return gson.fromJson(json, new ListParameterizedType(clazz));
        } catch (Exception e) {
            Logger.e(e, "解析json过程中发生异常 json : %s, class : %s", json, clazz);
            return null;
        }
    }

    private static final class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{type};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}

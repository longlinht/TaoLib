package io.github.longlinht.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Set;

import io.github.longlinht.library.guava.Supplier;
import io.github.longlinht.library.guava.Suppliers;

/**
 *
 * SharedPreference Store：
 *
 * 用于简化SharedPreference 模板代码
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class PreferenceStore {

    private PreferenceStore() {
        //no instance
    }

    public static final Supplier<SharedPreferences> PREFERENCES_SUPPLIER = Suppliers.memoize(new Supplier<SharedPreferences>() {
        @Override
        public SharedPreferences get() {
            return GlobalContext.getAppContext().getSharedPreferences("PreferenceStore", Context.MODE_PRIVATE);
        }
    });

    /**
     * 获取一个存贮int类型的Preference存贮器
     */
    public static IntStore ofInt(SharedPreferences preferences, String key, int defaultValue) {
        return new IntStore(preferences, key, defaultValue);
    }

    /**
     * 获取一个存贮Long类型的Preference存贮器
     */
    public static LongStore ofLong(SharedPreferences preferences, String key, long defaultValue) {
        return new LongStore(preferences, key, defaultValue);
    }

    /**
     * 获取一个存贮boolean类型的Preference存贮器
     */
    public static BooleanStore ofBoolean(SharedPreferences preferences, String key, boolean defaultValue) {
        return new BooleanStore(preferences, key, defaultValue);
    }

    /**
     * 获取一个存贮String类型的Preference存贮器
     */
    public static StringStore ofString(SharedPreferences preferences, String key, String defaultValue) {
        return new StringStore(preferences, key, defaultValue);
    }

    /**
     * 获取一个存贮StringSet类型的Preference存贮器
     */
    public static StringSetStore ofStringSet(SharedPreferences preferences, String key, Set<String> defaultValue) {
        return new StringSetStore(preferences, key, defaultValue);
    }

    /**
     * 获取一个存贮float类型的Preference存贮器
     */
    public static FloatStore ofFloat(SharedPreferences preferences, String key, float defaultValue) {
        return new FloatStore(preferences, key, defaultValue);
    }

    /**
     * 获取一个存贮int类型的Preference存贮器
     */
    public static IntStore ofInt(String key, int defaultValue) {
        return new IntStore(PREFERENCES_SUPPLIER.get(), key, defaultValue);
    }

    /**
     * 获取一个存贮Long类型的Preference存贮器
     */
    public static LongStore ofLong(String key, long defaultValue) {
        return new LongStore(PREFERENCES_SUPPLIER.get(), key, defaultValue);
    }

    /**
     * 获取一个存贮boolean类型的Preference存贮器
     */
    public static BooleanStore ofBoolean(String key, boolean defaultValue) {
        return new BooleanStore(PREFERENCES_SUPPLIER.get(), key, defaultValue);
    }

    /**
     * 获取一个存贮String类型的Preference存贮器
     */
    public static StringStore ofString(String key, String defaultValue) {
        return new StringStore(PREFERENCES_SUPPLIER.get(), key, defaultValue);
    }

    /**
     * 获取一个存贮StringSet类型的Preference存贮器
     */
    public static StringSetStore ofStringSet(String key, Set<String> defaultValue) {
        return new StringSetStore(PREFERENCES_SUPPLIER.get(), key, defaultValue);
    }

    /**
     * 获取一个存贮float类型的Preference存贮器
     */
    public static FloatStore ofFloat(String key, float defaultValue) {
        return new FloatStore(PREFERENCES_SUPPLIER.get(), key, defaultValue);
    }

    public static class IntStore {

        final int defaultValue;
        final SharedPreferences pref;
        final String key;

        public IntStore(SharedPreferences pref, String key, int defaultValue) {
            this.pref = pref;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public void set(int value) {

            if (value == get()) {
                return;
            }

            pref.edit().putInt(key, value).apply();
        }

        public void add(int step) {
            set(get() + step);
        }

        public int get() {
            return pref.getInt(key, defaultValue);
        }

        public void clear() {
            pref.edit().remove(key).apply();
        }

        @Override
        public String toString() {
            return "IntStore{" +
                    "defaultValue=" + defaultValue +
                    ", pref=" + pref +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    public static class LongStore {

        final long defaultValue;
        final SharedPreferences pref;
        final String key;

        public LongStore(SharedPreferences pref, String key, long defaultValue) {
            this.pref = pref;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public void set(long value) {

            if (value == get()) {
                return;
            }

            pref.edit().putLong(key, value).apply();
        }

        public long get() {
            return pref.getLong(key, defaultValue);
        }

        public void add(int step) {
            set(get() + step);
        }

        public void clear() {
            pref.edit().remove(key).apply();
        }

        @Override
        public String toString() {
            return "LongStore{" +
                    "defaultValue=" + defaultValue +
                    ", pref=" + pref +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    public static class BooleanStore {

        final boolean defaultValue;
        final SharedPreferences pref;
        final String key;

        public BooleanStore(SharedPreferences pref, String key, boolean defaultValue) {
            this.pref = pref;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public void set(boolean value) {

            if (value == get()) {
                return;
            }

            pref.edit().putBoolean(key, value).apply();
        }

        public boolean get() {
            return pref.getBoolean(key, defaultValue);
        }

        public void reverse() {
            set(!get());
        }

        public void clear() {
            pref.edit().remove(key).apply();
        }

        @Override
        public String toString() {
            return "BooleanStore{" +
                    "defaultValue=" + defaultValue +
                    ", pref=" + pref +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    public static class StringStore {

        final String defaultValue;
        final SharedPreferences pref;
        final String key;

        public StringStore(SharedPreferences pref, String key, String defaultValue) {
            this.pref = pref;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public void set(String value) {

            if (TextUtils.equals(value, get())) {
                return;
            }

            pref.edit().putString(key, value).apply();
        }

        public String get() {
            return pref.getString(key, defaultValue);
        }

        public void clear() {
            pref.edit().remove(key).apply();
        }

        @Override
        public String toString() {
            return "StringStore{" +
                    "defaultValue='" + defaultValue + '\'' +
                    ", pref=" + pref +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    public static class StringSetStore {

        final Set<String> defaultValue;
        final SharedPreferences pref;
        final String key;

        public StringSetStore(SharedPreferences pref, String key, Set<String> defaultValue) {
            this.pref = pref;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public void set(@NonNull Set<String> stringSet) {

            if (Objects.equals(stringSet, get())) {
                return;
            }

            pref.edit().putStringSet(key, stringSet).apply();
        }

        public Set<String> get() {
            return pref.getStringSet(key, defaultValue);
        }

        public void clear() {
            pref.edit().remove(key).apply();
        }

        @Override
        public String toString() {
            return "StringSetStore{" +
                    "defaultValue=" + defaultValue +
                    ", pref=" + pref +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    public static class FloatStore {

        private final String key;
        private final SharedPreferences preferences;
        private final float defaultValue;

        public FloatStore(SharedPreferences preferences, String key, float defaultValue) {
            this.key = key;
            this.preferences = preferences;
            this.defaultValue = defaultValue;
        }

        public void set(float value) {

            if (value == get()) {
                return;
            }

            preferences.edit().putFloat(key, value).apply();
        }

        public float get() {
            return preferences.getFloat(key, defaultValue);
        }

        public void clear() {
            preferences.edit().remove(key).apply();
        }

        @Override
        public String toString() {
            return "FloatStore{" +
                    "key='" + key + '\'' +
                    ", preferences=" + preferences +
                    ", defaultValue=" + defaultValue +
                    '}';
        }
    }
}

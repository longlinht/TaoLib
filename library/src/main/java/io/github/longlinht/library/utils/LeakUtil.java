package io.github.longlinht.library.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import java.lang.reflect.Field;

/**
 * Created by Tao He on 18-4-29.
 * hetaoof@gmail.com
 */
public class LeakUtil {

    /**
     * 修复华为手机GestureBoostManager内存的泄露
     */
    public static void fixHuaWeiGestureBoostManagerLeak(Object object) {
        if (!"HUAWEI".equalsIgnoreCase(Build.MANUFACTURER)) {
            return;
        }
        try {
            Class<?> GestureBoostManagerClass = Class.forName("android.gestureboost.GestureBoostManager");
            Field sGestureBoostManagerField = GestureBoostManagerClass.getDeclaredField("sGestureBoostManager");
            sGestureBoostManagerField.setAccessible(true);
            Object gestureBoostManager = sGestureBoostManagerField.get(GestureBoostManagerClass);
            Field contextField = GestureBoostManagerClass.getDeclaredField("mContext");
            contextField.setAccessible(true);
            if (contextField.get(gestureBoostManager) == object) {
                contextField.set(gestureBoostManager, null);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 修复华为FastgrabConfigReader手机内存的泄露
     */
    public static void fixHuaWeiFastgrabConfigReaderLeak(Object object) {
        if (!"HUAWEI".equalsIgnoreCase(Build.MANUFACTURER)) {
            return;
        }

        try {
            Class<?> FastgrabConfigReaderClass = Class.forName("android.rms.iaware.FastgrabConfigReader");
            Field mFastgrabConfigReader = FastgrabConfigReaderClass.getDeclaredField("mFastgrabConfigReader");
            mFastgrabConfigReader.setAccessible(true);

            Object fastgrabConfigReader = mFastgrabConfigReader.get(FastgrabConfigReaderClass);
            Field contextField = FastgrabConfigReaderClass.getDeclaredField("mContext");
            contextField.setAccessible(true);

            if (contextField.get(fastgrabConfigReader) == object) {
                contextField.set(fastgrabConfigReader, null);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * fixInputMethod
     * <p>
     * Fix for https://code.google.com/p/android/issues/detail?id=171190
     *
     * @param context Context
     */
    public static void fixInputMethod(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager inputMethodManager = null;
        try {
            inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (inputMethodManager == null) {
            return;
        }
        String[] strArr = new String[]{"mCurRootView", "mServedView", "mNextServedView", "mLastSrvView"};
        for (int i = 0; i < strArr.length; i++) {
            try {
                Field declaredField = inputMethodManager.getClass().getDeclaredField(strArr[i]);
                if (declaredField == null) {
                    continue;
                }
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                Object obj = declaredField.get(inputMethodManager);
                if (obj == null || !(obj instanceof View)) {
                    continue;
                }
                View view = (View) obj;
                if (isSameContext(view, context)) {
                    declaredField.set(inputMethodManager, null);
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private static boolean isSameContext(View view, Context context) {
        if (view.getContext() == context) {
            return true;
        }

        if ( view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup.getChildCount() > 0) {
                View child = viewGroup.getChildAt(0);
                if ( child.getContext() == context) {
                    return true;
                }
            }
        }

        return false;
    }
}

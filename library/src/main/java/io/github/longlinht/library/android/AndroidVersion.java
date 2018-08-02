package io.github.longlinht.library.android;

import android.os.Build;

/**
 * Android API版本判断
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */

public class AndroidVersion {

    private AndroidVersion() {
        //no instance
    }

    public static final boolean AT_LEASET_FROYO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    public static final boolean AT_LEASET_8 = Build.VERSION.SDK_INT >= 8;
    public static final boolean AT_LEASET_GINGERBREAD = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    public static final boolean AT_LEASET_9 = Build.VERSION.SDK_INT >= 9;
    public static final boolean AT_LEASET_GINGERBREAD_MR1 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
    public static final boolean AT_LEASET_10 = Build.VERSION.SDK_INT >= 10;
    public static final boolean AT_LEASET_HONEYCOMB = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    public static final boolean AT_LEASET_11 = Build.VERSION.SDK_INT >= 11;
    public static final boolean AT_LEASET_HONEYCOMB_MR1 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    public static final boolean AT_LEASET_12 = Build.VERSION.SDK_INT >= 12;
    public static final boolean AT_LEASET_HONEYCOMB_MR2 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    public static final boolean AT_LEASET_13 = Build.VERSION.SDK_INT >= 13;
    public static final boolean AT_LEASET_ICE_CREAM_SANDWICH = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    public static final boolean AT_LEASET_14 = Build.VERSION.SDK_INT >= 14;
    public static final boolean AT_LEASET_ICE_CREAM_SANDWICH_MR1 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    public static final boolean AT_LEASET_15 = Build.VERSION.SDK_INT >= 15;
    public static final boolean AT_LEASET_JELLY_BEAN = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    public static final boolean AT_LEASET_16 = Build.VERSION.SDK_INT >= 16;
    public static final boolean AT_LEASET_JELLY_BEAN_MR1 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    public static final boolean AT_LEASET_17 = Build.VERSION.SDK_INT >= 17;
    public static final boolean AT_LEASET_JELLY_BEAN_MR2 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    public static final boolean AT_LEASET_18 = Build.VERSION.SDK_INT >= 18;
    public static final boolean AT_LEASET_KITKAT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    public static final boolean AT_LEASET_19 = Build.VERSION.SDK_INT >= 19;
    public static final boolean AT_LEASET_KITKAT_WATCH = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH;
    public static final boolean AT_LEASET_20 = Build.VERSION.SDK_INT >= 20;
    public static final boolean AT_LEASET_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    public static final boolean AT_LEASET_21 = Build.VERSION.SDK_INT >= 21;
    public static final boolean AT_LEASET_LOLLIPOP_MR1 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    public static final boolean AT_LEASET_22 = Build.VERSION.SDK_INT >= 22;
    public static final boolean AT_LEASET_M = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    public static final boolean AT_LEASET_23 = Build.VERSION.SDK_INT >= 23;
    public static final boolean AT_LEASET_24 = Build.VERSION.SDK_INT >= 24;
    public static final boolean AT_LEASET_25 = Build.VERSION.SDK_INT >= 25;
}

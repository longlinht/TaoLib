package io.github.longlinht.library.android;

/**
 * Created by Tao He on 18-4-29.
 * hetaoof@gmail.com
 */
public class PhoneInfoConfig {
    /**
     * 手机号.
     */
    public static String phoneNumber = "";
    /**
     * 设备号.暂留
     */
    public static String devi = "";

    /**
     * 手机名
     */
    public static String devm = "";
    /**
     * 手机 imei.
     */
    public static String imei = "";
    /**
     * 手机卡 imsi.
     */
    public static String imsi = "";
    /**
     * 手机机型.
     */
    public static String userAgent = "";
    /**
     * 这里使用了cpu序列号.
     */
    public static String mechineid = "";
    /**
     * 基站定位字符串（经纬度）.
     */
    public static String cls = "";
    /**
     * GPS定位字符串（经纬度）.
     */
    public static String gls = "";
    /**
     * 网络类型. #ITypeDef, default is WIFI
     */
    public static volatile int netType = 6;
    /**
     * 是否双卡双待机.
     */
    public static boolean isDoubleSimcard = false;
    public static boolean isInHandSet = false;
    /**
     * 从启动开始移动网络读的图片字节数
     */
    public static long readWifiImageBytes = 0;
    /**
     * 从启动开始wifi读的图片字节数
     */
    public static long readMobileImageBytes = 0;
    /**
     * 手机sim卡的序号
     */
    public static String iccid = "";
    /**
     * 手机屏幕是否打开
     */
    public static boolean isScreenOFF = false;
    /**
     * 是否是模拟器
     */
    public static volatile boolean isEmulator = false;
    /**
     * 数盟设备Id
     */
    public static String smid = "";
    /**
     * 运营商
     */
    public static String carrname = "";
    /**
     * 国家代码，ISOCountryCode
     */
    public static String isoCountryCode = "";
    /**
     * 移动手机代码,MobilCountryCode
     */
    public static String mobilCountryCode = "";
    /**
     * 手机网络代码
     */
    public static String mobilNetCode = "";

    /**
     * cpu型号
     */
    public static String cpuInfo = "";

    /**
     * cpu型号，【没有经过url encode的】
     */
    public static String cpuInfoWithoutUrlEncode = "";
    /**
     * 手机的运行内存
     */
    public static long totalMemorySize = 0;
}

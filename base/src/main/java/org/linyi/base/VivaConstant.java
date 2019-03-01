package org.linyi.base;

import org.linyi.base.utils.CommUtils;
import org.linyi.base.utils.UIUtils;

public class VivaConstant {

    //是否debug模式 很重要【发布一定置为 false】否则影响性能
    public static final boolean isDebug = true;
    //本地
    public static final String TEST_HOST_KS = "http://192.168.0.56:8080/";
    public static final String TEST_HOST_YBY = "http://192.168.0.198:8080";
    //线上
    public static final String MAIN_HOST = "http://106.14.106.240:3000";

    public static String baseUrl = TEST_HOST_KS;
    //是否显示日志
    public static boolean showLog = isDebug;
    //包名
    public static String PackageName = CommUtils.getPackageName(VivaSdk.getContext());
    //应用名
    public static String AppName = CommUtils.getAppName(VivaSdk.getContext());
    //屏幕宽度
    public static int ScreenWidth = UIUtils.getScreenWidth(VivaSdk.getContext());
    //屏幕高度
    public static int ScreenHeight = UIUtils.getScreenHeight(VivaSdk.getContext());
    //渠道名
    public static String ChannelName = UIUtils.getChannel();


}

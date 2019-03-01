package org.linyi.base.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.linyi.base.VivaSdk;

import java.lang.reflect.Method;

/**
 * @fun: 获取屏幕高度（适配刘海屏）
 * @developer: rwz
 * @date: 2019/2/18 0018 上午 11:25
 * 参考：https://blog.csdn.net/xu20082100226/article/details/80076351
 */
public class ScreenUtils {

    private static final int PORTRAIT = 0;
    private static final int LANDSCAPE = 1;
    @NonNull
    private volatile static Point[] mRealSizes = new Point[2];

    public static int getFullActivityHeight(@Nullable Context context) {
        if (!isAllScreenDevice()) {
            return getScreenHeight(context);
        }
        return getScreenRealHeight(context);
    }

    public static int getScreenRealHeight(@Nullable Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getScreenHeight(context);
        }

        int orientation = context != null
                ? context.getResources().getConfiguration().orientation
                : VivaSdk.getContext().getResources().getConfiguration().orientation;
        orientation = orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;

        if (mRealSizes[orientation] == null) {
            WindowManager windowManager = context != null
                    ? (WindowManager) context.getSystemService(Context.WINDOW_SERVICE)
                    : (WindowManager) VivaSdk.getContext().getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return getScreenHeight(context);
            }
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            mRealSizes[orientation] = point;
        }
        return mRealSizes[orientation].y;
    }

    public static int getScreenHeight(@Nullable Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

    private volatile static boolean mHasCheckAllScreen;
    private volatile static boolean mIsAllScreenDevice;

    public static boolean isAllScreenDevice() {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        // 低于 API 21的，都不会是全面屏。。。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) VivaSdk.getContext().getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (width != 0 && height / width >= 1.97f) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }


    public static boolean hasNotchInHuaweiScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    public static int getNavigationBarHeight() {
        Context context = VivaSdk.getContext();
        boolean result = checkDeviceHasNavigationBar(context);
        if (!result || miuiNavigationGestureEnabled(context)) {
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    /**
     * 判断设备是否存在NavigationBar
     * @return true 存在, false 不存在
     * http://lastwarmth.win/2018/09/13/phone-adapt/
     */
    public static boolean deviceHasNavigationBar() {
        boolean haveNav = false;
        try {
            Class<?> windowManagerGlobalClass = Class.forName("android.view.WindowManagerGlobal");
            Method getWmServiceMethod = windowManagerGlobalClass.getDeclaredMethod("getWindowManagerService");
            getWmServiceMethod.setAccessible(true);
            Object iWindowManager = getWmServiceMethod.invoke(null);

            Class<?> iWindowManagerClass = iWindowManager.getClass();
            Method hasNavBarMethod = iWindowManagerClass.getDeclaredMethod("hasNavigationBar");
            hasNavBarMethod.setAccessible(true);
            haveNav = (Boolean) hasNavBarMethod.invoke(iWindowManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return haveNav;
    }

    /**
     * http://lastwarmth.win/2018/09/13/phone-adapt/
     */
    public static boolean miuiNavigationGestureEnabled(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar") != 0;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


}

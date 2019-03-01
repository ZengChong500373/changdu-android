package com.linyi.reader.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import org.linyi.base.utils.LogUtil;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/15 0015 上午 11:33
 */
public class ReaderUtils {

    /**
     * 设置亮度(手动设置亮度，需要关闭自动设置亮度的开关)
     *
     * @param brightness 0 ~ 1f
     */
    public static void setWindowBrightness(Activity activity, float brightness) {
        if(activity == null)
            return;
        //背光的亮度的设置
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = brightness;
        activity.getWindow().setAttributes(lp);
        /*stopAutoBrightness(activity);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = brightness;
        activity.getWindow().setAttributes(lp);*/
    }

    /**
     * 停止自动亮度调节
     */
    public static void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * 开启亮度自动调节
     */
    public static void startAutoWindowBrightness(Activity activity) {
        Window window = activity.getWindow();
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.screenBrightness = -1.0f;//-1代表使用系统亮度
        window.setAttributes(attrs);
       /* Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);*/
    }

    /**
     * @return 获取屏幕的亮度( 0 ~ 1f)
     */
    public static float getScreenBrightness(Activity activity) {
        int             nowBrightnessValue = 0;
        ContentResolver resolver           = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.d("nowBrightnessValue = " + nowBrightnessValue);
        return nowBrightnessValue / 255f;
    }


}

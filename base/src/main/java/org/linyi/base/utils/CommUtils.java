package org.linyi.base.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import org.linyi.base.R;
import org.linyi.base.VivaSdk;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/23 0023 下午 3:00
 */
public class CommUtils {

    /**
     * 获取版本信息
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo pkg = null;
        if (context == null) {
            return null;
        }
        try {
            pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pkg;
    }

    /**
     * 获取包名
     */
    public static String getPackageName(Context context) {
        return getPackageInfo(context).packageName;
    }

    /**
     * 获取应用名
     */
    public static String getAppName(Context context) {
        try {
            return context.getString(context.getApplicationInfo().labelRes);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 返回版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 返回版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static int parseInt(String text, int defaultValue) {
        if(TextUtils.isEmpty(text))
            return defaultValue;
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * 复制文字到剪切板
     */
    public static void copyText(CharSequence text) {
        ClipboardManager cmb = (ClipboardManager) VivaSdk.getContext().getSystemService(CLIPBOARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            String label = UIUtils.getString(R.string.copy_text);
            cmb.setPrimaryClip(ClipData.newPlainText(label, text));
            ToastUtils.getInstance().showShort(R.string.copy_text_completed);
        } else {
            ToastUtils.getInstance().showShort(R.string.not_support_copy);
        }
    }

    /**
     * 判断intent是否可以安全跳转(主要是隐式跳转)
     */
    public static boolean canTurn(Context context, Intent intent) {
        return context != null && intent != null && intent.resolveActivity(context.getPackageManager()) != null;
    }


}

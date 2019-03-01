package org.linyi.base.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import org.linyi.base.VivaSdk;


/**
 * Created by sunxx on 2016/8/3.
 */
public class ToastUtils {
    private static ToastUtils toastUtils;
    private Toast mToast;
    private ToastUtils() {
    }
    public static ToastUtils getInstance() {
        if (toastUtils==null){
            toastUtils=new ToastUtils();
        }
        return toastUtils;
    }

    public void show(String str) {
        showShortSingle(str);
    }

    public void showShortSingle(final @StringRes int stringRes) {
        showShortSingle(UIUtils.getString(stringRes));
    }

    public void showShortSingle(final String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if (isMainThread()) {
            getToast().setText(string);
            mToast.show();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getToast().setText(string);
                    //已在主线程中，可以更新UI
                    mToast.show();
                }
            });
        }
    }

    private Toast getToast() {
        if (mToast == null) {
            mToast = Toast.makeText(VivaSdk.getContext(), "", Toast.LENGTH_SHORT);
        }
        return mToast;
    }

    /**
     * 一定在主线程显示
     * @param stringRes
     */
    public static void showShort(@StringRes int stringRes) {
        if (isMainThread()) {
            showText(UIUtils.getString(stringRes), false);
        } else {
            showTextOnBackgroundThread(UIUtils.getString(stringRes), false);
        }
    }
    public static void showLong(@StringRes int stringRes) {
        if (isMainThread()) {
            showText(UIUtils.getString(stringRes), true);
        } else {
            showTextOnBackgroundThread(UIUtils.getString(stringRes), true);
        }
    }

    public static void showShort(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (isMainThread()) {
            showText(text, false);
        } else {
            showTextOnBackgroundThread(text, false);
        }
    }

    public static void showLong(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (isMainThread()) {
            showText(text, true);
        } else {
            showTextOnBackgroundThread(text, true);
        }
    }



    private static void showTextOnBackgroundThread(final String text, final boolean isLong) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //已在主线程中，可以更新UI
                showText(text, isLong);
            }
        });
    }

    private static void showText(String text, boolean isLong) {
        if (isLong) {
            Toast.makeText(VivaSdk.getContext(), text, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(VivaSdk.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 是否在主线程
     * @return
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
    

}

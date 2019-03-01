package org.linyi.base.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.linyi.base.VivaSdk;

import io.reactivex.Observable;


/**
 * Created by rwz on 2017/7/25.
 */

public class PermissionUtils {

    private static Context getContext() {
        return VivaSdk.getContext();
    }

    //范围只能在0 ~ 65536
    public static int REQUEST_WRITE_SETTING_PERMISSION = 1000;

    private static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否有读写权限
     * @return
     */
    public static boolean hasWritePermission() {
        return !hasM() || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否有录音权限
     * @return
     */
    public static boolean hasRecordPermission() {
        return  !hasM() || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 拍照权限
     * @return
     */
    public static boolean hasCameraPermission() {
        return  !hasM() || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否用系统设置权限
     */
    public static boolean hasSettingPermission(Activity aty) {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(aty));
    }

    public static boolean hasMulPermission(String... permission) {
        if (permission == null) {
            return true;
        }
        int length = permission.length;
        for (int i = 0; i < length; i++) {
            if (ContextCompat.checkSelfPermission(getContext(), permission[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private static Observable<Boolean> getRequestObservable(Boolean result){
        return Observable.just(result);
    }

    /**
     * 读写权限
     * @param aty
     * @return
     */
    public static Observable<Boolean> requestWrite(Activity aty) {
        if (aty == null) {
            return getRequestObservable(false);
        } else {
            return new RxPermissions(aty).request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * 录音权限
     * @param aty
     * @return
     */
    public static Observable<Boolean> requestRecord(Activity aty) {
        if (aty == null) {
            return getRequestObservable(false);
        } else {
            return new RxPermissions(aty).request(Manifest.permission.RECORD_AUDIO);
        }
    }

    /**
     * 系统设置（屏幕亮度权限）
     */
    public static void requestSetting(Activity aty) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + aty.getPackageName()));
        aty.startActivityForResult(intent, REQUEST_WRITE_SETTING_PERMISSION);
    }

    /**
     * 请求多个
     * @param aty
     * @param allPermissions
     * @return
     */
    public static Observable<Boolean> requestMulPermissions(Activity aty, final String... allPermissions) {
        if (aty == null) {
            return getRequestObservable(false);
        } else {
            return new RxPermissions(aty).request(allPermissions);
        }
    }



    /**
     * 访问照相机
     * @param aty
     * @return
     */
    public static Observable<Boolean> requestCamera(Activity aty) {
        if (hasM()) {
            if (aty == null) {
                return getRequestObservable(false);
            } else {
                RxPermissions permissions = new RxPermissions(aty);
                Observable<Boolean> request = permissions.request(Manifest.permission.CAMERA);
                return request;
            }
        } else {
            return getRequestObservable(true);
        }
    }

    /**
     * 获取相机和文件读写权限
     * @param aty
     * @return
     */
    public static Observable<Boolean> requestCameraAndWrite(Activity aty) {
        return requestMulPermissions(aty, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    /** 检查是否开启了通知栏权限 **/
    public static boolean hasNotificationPermission() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(getContext());
        return manager.areNotificationsEnabled();
    }

}

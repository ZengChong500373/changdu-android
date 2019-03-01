package org.linyi.base.constant;

import android.content.Context;
import android.os.Environment;

import org.linyi.base.VivaConstant;
import org.linyi.base.VivaSdk;

import java.io.File;


/**
 * Created by rwz on 2017/7/18.
 */

public interface Path {

    Context CONTEXT = VivaSdk.getContext();
    String SEPARATOR = File.separator;

    /** 外置目录 **/
    interface External{
        //外置SD卡目录
        String BASE_DIR = Environment.getExternalStorageDirectory().getPath() + SEPARATOR + VivaConstant.PackageName + SEPARATOR;
        //外置SD卡临时目录(用完即删)
        String TEMP_DIR = BASE_DIR + "temp" + SEPARATOR;
        //外置SD卡缓存目录(离线文件，删除不影响程序)
        String CACHE_DIR = BASE_DIR + "cache" + SEPARATOR;
        //外置SD卡下载目录
        String DOWNLOAD_DIR = BASE_DIR + "download" + SEPARATOR;
        //头像待上传缓存文件
        String TEMP_AVATAR_PATH = TEMP_DIR + "avatar.png";
        //外置SD卡下载图片目录
        String IMG_DIR = DOWNLOAD_DIR + "小说图片" + SEPARATOR;
        //apk下载文件名
        String NEW_APK_NAME = VivaConstant.AppName + ".apk";
    }

    /** 内置目录 **/
    interface Inner{
        //内置文件目录
        String BASE_DIR = CONTEXT.getFilesDir().getAbsolutePath();
        //内置缓存目录
        String CACHE_DIR = CONTEXT.getCacheDir().getAbsolutePath();
        //内置下载目录
        String DOWNLOAD_DIR = BASE_DIR + "download" + SEPARATOR;
    }

}

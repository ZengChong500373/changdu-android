package com.linyi.viva.extra.utils;

import android.content.Context;
import android.text.TextUtils;

import org.linyi.base.VivaSdk;
import org.linyi.base.constant.Path;
import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;

import java.io.File;
import java.math.BigDecimal;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/21 0021 下午 6:21
 */
public class CacheUtils {

    /**
     * 格式化文件大小
     * @param size file.length() 获取文件大小
     * @return
     */
    public static String formatFileSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 清除缓存
     */
    public static void cleanCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageLoaderUtil.getInstance().clearCache(VivaSdk.getContext());
                //删除缓存目录
                deleteDir(new File(Path.External.CACHE_DIR));
            }
        }).start();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return 删除成功返回true，否则返回false,如果文件是空，那么永远返回true
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return true;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 获取缓存大小
     */
    public static long getCacheSize() {
        Context context = VivaSdk.getContext();
        return getDirSize(ImageLoaderUtil.getInstance().getCacheDir(context)) + getDirSize(Path.External.CACHE_DIR);
    }

    /**
     * 获取文件夹大小
     */
    public static long getDirSize(String filePath) {
        if(TextUtils.isEmpty(filePath))
            return 0;
        long size = 0;
        File f = new File(filePath);
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    size += getDirSize(file.getPath());
                    continue;
                }
                size += file.length();
            }
        } else {
            size += f.length();
        }
        return size;
    }


}

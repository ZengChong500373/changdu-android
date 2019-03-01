package com.linyi.reader.utils;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.PermissionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/23 0023 下午 3:10
 */
public class FileUtils {

    /**
     * 创建文件, 需要读写权限
     */
    private static File createNewFile(String filePath) {
        if(TextUtils.isEmpty(filePath))
            return null;
        boolean result = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!result) {
            LogUtil.e("createNewFile", "state : " + Environment.getExternalStorageState());
            return null;
        } else if (!PermissionUtils.hasWritePermission()) {
            LogUtil.e("no permission");
            return null;
        }
        File file = new File(filePath);
        if(file.isDirectory() && !file.mkdirs())
            return null;
        else if(file.exists()){
            return file;
        }
        File parentFile = file.getParentFile();
        if(!parentFile.exists()) {
            result = parentFile.mkdirs();
            LogUtil.d("result = " + result);
        }
        try {
            boolean createResult = file.createNewFile();
            return createResult ? file : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件后缀名
     */
    public static String getFileExtensionName(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";
        int endP = fileName.lastIndexOf(".");
        return endP > -1 ? fileName.substring(endP + 1, fileName.length()) : "";
    }

    /**
     * 将指定文本写入文件
     * @param charsetName "UTF-16LE"
     */
    public static boolean writeText(String fileName, String text, String charsetName) {
        File file = createNewFile(fileName);
        if(file == null || !file.exists())
            return false;
        Writer fos = null;
        try {
            long startTime = System.currentTimeMillis();
            fos = new OutputStreamWriter(new FileOutputStream(file, false), charsetName);
            fos.write(text);
            fos.flush();
            LogUtil.d("writeText dt = " + (System.currentTimeMillis() - startTime) + "ms");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 从自定文件读取文本
     */
    public static String readText(File file, String charsetName) {
        if (file == null || !file.exists()) {
            LogUtil.e( "readText", "file is unavailable" , file);
            return null;
        }
        try {
            return readTextByInputStream(new FileInputStream(file), charsetName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static String readTextByInputStream(InputStream is, String charsetName) {
        Reader fis = null;
        try {
            long startTime = System.currentTimeMillis();
            fis = new InputStreamReader(is, charsetName);
            StringBuilder sb = new StringBuilder();
            char[] buff = new char[1024 * 4];
            int length;
            while ((length = fis.read(buff)) != -1) {
                sb.append(buff, 0, length);
            }
            LogUtil.d( "readText dt = " + (System.currentTimeMillis() - startTime) + "ms");
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

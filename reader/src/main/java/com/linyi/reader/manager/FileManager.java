package com.linyi.reader.manager;

import android.text.TextUtils;

import com.api.changdu.proto.BookApi;
import com.linyi.reader.ReaderConstant;
import com.linyi.reader.utils.FileUtils;

import org.linyi.base.VivaConstant;
import org.linyi.base.VivaSdk;
import org.linyi.base.manager.UserManager;
import org.linyi.base.utils.safety.EncryptionUtil;

import java.io.File;

/**
 * @fun: 文件管理
 * @developer: rwz
 * @date: 2019/1/23 0023 下午 3:11
 */
public class FileManager {

    private static FileManager instance;
    private final String SEPARATOR = File.separator;

    private FileManager() {
    }

    public static FileManager getInstance() {
        if(instance == null)
            synchronized (FileManager.class) {
                if(instance == null)
                    instance = new FileManager();
            }
        return instance;
    }

    /**
     * 获取小说章节保存目录
     * @return ${包名}/cache/novel/${用户ID}/${小说ID}/${源ID}/${章节ID}.temp
     * 用户分目录，是为了免费用户查看付费用户的书籍，故分开缓存
     */
    public String getNovelPath(BookApi.ChapterMsg entity) {
        if(entity == null)
            return null;
        String userID  = UserManager.getInstance().getUserID();
        if(TextUtils.isEmpty(userID))
            userID = "default";
        String sourceID = "";
        sourceID = TextUtils.isEmpty(sourceID) ? ReaderConstant.DefaultSourceID : sourceID;
        return ReaderConstant.CacheNovel + SEPARATOR + userID + SEPARATOR + getEncodeFileName(entity.getBookID()) +
                SEPARATOR + getEncodeFileName(sourceID) + SEPARATOR + getEncodeFileName(String.valueOf(entity.getChapterID()))
                + ".temp";
    }

    private String getEncodeFileName(String fileName) {
        return ReaderConstant.isEncodeFileName ? EncryptionUtil.encodeMD5ToString(fileName) : fileName;
    }

    public String readText(String filePath) {
        if(TextUtils.isEmpty(filePath))
            return null;
        return FileUtils.readText(new File(filePath), ReaderConstant.CharsetName);
    }

    public boolean writeText(String filePath, String text) {
        return FileUtils.writeText(filePath, text, ReaderConstant.CharsetName);
    }


}

package com.linyi.reader.utils;

import android.text.TextUtils;

import com.api.changdu.proto.BookApi;
import com.google.protobuf.InvalidProtocolBufferException;
import com.linyi.reader.R;
import com.linyi.reader.ReaderConstant;
import com.linyi.reader.entity.ReaderConfig;

import org.linyi.base.VivaSdk;
import org.linyi.base.constant.Key;
import org.linyi.base.constant.SystemConstant;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.UIUtils;

import java.io.UnsupportedEncodingException;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/12 0012 下午 3:47
 */
public class NovelUtils {

    /**
     * 创建一个章节的唯一标识
     */
    public static String createChapterUniqueID(BookApi.ChapterMsg msg) {
        if(msg == null)
            return "";
        return msg.getBookID() + "/" + ReaderConstant.DefaultSourceID + "/" + msg.getChapterID();
    }

    public static BookApi.AckChapter.Data parseChapterData(String data) {
        if(TextUtils.isEmpty(data))
            return null;
        try {
            return BookApi.AckChapter.Data.parseFrom(data.getBytes(SystemConstant.CHARSET_NAME));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNightMode() {
        int[] readBg = UIUtils.getIntArr(R.array.read_bg);
        int nightType = readBg != null ? readBg.length - 1: 0;
        ReaderConfig config = SharePreUtil.getObject(Key.READER_CONFIG, ReaderConfig.class);
        boolean isNightMode = config != null && config.getSkinType() == nightType;
        return isNightMode;
    }

}

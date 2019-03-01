package com.linyi.reader;

import org.linyi.base.constant.Path;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/23 0023 下午 3:40
 */
public interface ReaderConstant {

    //本地编码方式
    String CharsetName = "UTF-8";
//    String CharsetName = "ISO-8859-1";
//    String CharsetName = "UTF-16LE";
    //小说缓存目录
    String CacheNovel = Path.External.CACHE_DIR + Path.SEPARATOR + "novel" + Path.SEPARATOR;
    //默认源ID
    String DefaultSourceID = "DEFAULT_SOURCE_ID";
    //自动获取附近章节
    String AUTO_GET_NEAR_CHAPTER = "AUTO_GET_NEAR_CHAPTER";
    //是否对文件夹加密
    boolean isEncodeFileName = false;

    /**
     * 字体
     */
    interface Typeface{
        //简体
        int SIMPLE = 0;
        //繁体
        int COMPLEX = 1;
    }

}

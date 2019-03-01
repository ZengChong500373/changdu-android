package com.linyi.reader.entity;

import com.api.changdu.proto.BookApi;

import java.util.List;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/23 0023 下午 5:06
 */
public class DirectoryData {

    //章节数
    private int count;
    //目录-章节列表
    private List<BookApi.ChapterMsg> list;

    public DirectoryData(int count, List<BookApi.ChapterMsg> list) {
        this.count = count;
        this.list = list;
    }

    public List<BookApi.ChapterMsg> getList() {
        return list;
    }

    public int getCount() {
        return count;
    }
}

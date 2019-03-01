package com.linyi.reader.entity.event;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/21 0021 上午 11:51
 */
public class LoadChapterEvent {

    private int chapterSort;

    public LoadChapterEvent(int chapterSort) {
        this.chapterSort = chapterSort;
    }

    public int getChapterSort() {
        return chapterSort;
    }

    public void setChapterSort(int chapterSort) {
        this.chapterSort = chapterSort;
    }
}

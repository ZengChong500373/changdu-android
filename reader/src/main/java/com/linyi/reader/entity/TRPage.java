package com.linyi.reader.entity;

import com.linyi.reader.manager.ChapterManager;
import com.linyi.reader.manager.NovelManager;

import org.linyi.base.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class TRPage {
    private int chapterSort;//章节序号
    private int page;   //当前页
    private List<String> lines;

    public TRPage(int page, List<String> lines, int chapterSort) {
        this.page = page;
        this.lines = lines;
        this.chapterSort = chapterSort;
    }

    public List<String> getLines() {
        return lines;
    }

    public int getPage() {
        return page;
    }

    public int getChapterSort() {
        return chapterSort;
    }

    @Override
    public String toString() {
        return "TRPage{" +
                "chapterSort=" + chapterSort +
                ", page=" + page +
                '}';
    }

    /**
     * @return 是否第一页
     */
    public boolean isFirstPage() {
        return page == 0;
    }

    /**
     * @return 是否最后一页
     */
    public boolean isLastPage() {
        ChapterManager manager = NovelManager.getInstance().getChapterManager(chapterSort);
        LogUtil.d("isLastPage", "chapterSort = " + chapterSort, "manager = " + manager, (manager != null ? manager.getPageCount() : 0));
        return manager != null && (page == manager.getPageCount() - 1);
    }

}

package com.linyi.reader.manager;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.api.changdu.proto.BookApi;
import com.linyi.reader.ReaderConstant;
import com.linyi.reader.entity.DirectoryData;

import org.linyi.base.entity.db.BaseNovelDbEntity;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.SharePreUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rwz on 2018/8/6.
 *   chapterSort 连续
 */

public class NovelManager {

    private static final String TAG = "NovelManager";
    //无效章节
    public static final int INVALID_CHAPTER_SORT = -1;
    //书名
    private String bookName;
    //小说id
    private String mBookID;
    //章节集合
    private SparseArray<ChapterManager> mChapterData;
    //当前章节
    private ChapterManager mCurrChapterManager;
    //章节数
    private int mChapterCount;
    //章节进度( 0 ~ 1f)
    private float progress;
    //整本书章节目录、源
    private DirectoryData mDirectoryData;
    private static NovelManager instance;
    //是否自动加载附近章节数据
    private boolean isAutoLoadNearChapter;
    //书籍实体类
    private BaseNovelDbEntity mEntity;

    private NovelManager() {
    }

    public static NovelManager getInstance() {
        if(instance == null)
            synchronized (NovelManager.class) {
                if(instance == null)
                    instance = new NovelManager();
            }
        return instance;
    }

    public NovelManager createNovel(BaseNovelDbEntity entity, String bookName) {
        mChapterData = new SparseArray<>();
        if (entity != null) {
            this.mEntity = entity;
            this.bookName = bookName;
            this.mBookID = entity.getBookID();
        }
        isAutoLoadNearChapter = SharePreUtil.getBoolean(ReaderConstant.AUTO_GET_NEAR_CHAPTER, true);
        return getInstance();
    }

    public BaseNovelDbEntity getNovelEntity() {
        return mEntity;
    }

    /** 清空数据、防止内存泄露 **/
    public void clear() {
        if(mChapterData != null)
            mChapterData.clear();
        mCurrChapterManager = null;
        mDirectoryData = null;
        mEntity = null;
        mChapterCount = 0;
        progress = 0f;
        instance = null;
    }

    public DirectoryData getDirectoryData() {
        return mDirectoryData;
    }

    public void setDirectoryData(DirectoryData bookDetailData, int initChapterSort) {
        this.mDirectoryData = bookDetailData;
        if (bookDetailData != null) {
            mChapterCount = bookDetailData.getCount();
            if(mCurrChapterManager == null)
                mCurrChapterManager = createChapterManager(initChapterSort);
        }
    }

    public void addChapter(BookApi.AckChapter.Data entity) {
        if (entity != null) {
            int chapterSortIndex = entity.getChapterSortIndex();
            ChapterManager chapterManager = NovelManager.getInstance().getChapterManager(chapterSortIndex);
            if (chapterManager != null) {
                chapterManager.setEntity(entity);
            } else {
                chapterManager = new ChapterManager(entity);
            }
            mChapterData.put(chapterSortIndex, chapterManager);
        }
    }

    /**
     * 跳转到指定章节, isLoadingNearChapter : 是否加载前后章节
     * @param chapterSort 章节序号
     * @return 是否已加载到数据
     */
    public boolean turnChapter(int chapterSort) {
        if(chapterSort == INVALID_CHAPTER_SORT)
            return false;
        ChapterManager manager = getChapterManager(chapterSort);
        LogUtil.d(TAG, "turnChapter, chapterSort = " + chapterSort, "manager = " + manager);
        if (manager == null)
            return false;
        Set<Integer> chapterSortSet = new HashSet<>();
        boolean result = !manager.isEmpty();
        if (!result) {
            chapterSortSet.add(chapterSort);
        }
        mCurrChapterManager = manager;
        int index = getChapterIndex(chapterSort);
        progress = mChapterCount == 0 ? 0 : (1000 * (index + 1) / mChapterCount) / 1000f;
        if (isAutoLoadNearChapter) {
            //判断是否还有下一章的数据, 没有需要加载数据
            ChapterManager nextChapterManager = getNextChapterManager();
            if ((nextChapterManager == null || nextChapterManager.isEmpty()) && !isLastChapter()) {
                chapterSortSet.add(getNextChapterSort(chapterSort));
            }
            //判断是否还有下一章的数据, 没有需要加载数据
            ChapterManager prevChapterManager = getPrevChapterManager();
            if ((prevChapterManager == null || prevChapterManager.isEmpty()) && !isFirstChapter()) {
                chapterSortSet.add(getPrevChapterSort(chapterSort));
            }
        }
        loadChapter(chapterSortSet);
        updateReadProgress(index + 1);
        return result;
    }

    /** 跳转到下一章节 **/
    public boolean turnNextChapter() {
        return turnChapter(getNextChapterSort());
    }

    /** 跳转到上一章节 **/
    public boolean turnPrevChapter() {
        return turnChapter(getPrevChapterSort());
    }

    /** 通过下标获取chapterSort **/
    public int getChapterSortByIndex(int index) {
        return index;
    }

    /** 是否存在上一章节数据 **/
    public boolean hasPrevChapter() {
        return getPrevChapterManager() != null;
    }

    /** 是否存在下一章节数据 **/
    public boolean hasNextChapter() {
        return getNextChapterManager() != null;
    }

    /** 获取下一章节序号 **/
    public int getNextChapterSort(){
        return getNextChapterSort(getCurrChapterSort());
    }
    public int getNextChapterSort(int currChapterSort) {
        return currChapterSort < mChapterCount - 1 ? currChapterSort + 1 : INVALID_CHAPTER_SORT;
       /* if(mDirectoryData != null) {
            List<BookApi.ChapterMsg> list = mDirectoryData.getList();
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    BookApi.ChapterMsg entity = list.get(i);
                    //找到当前的chapterSort
                    if (entity != null && entity.getChapterID() == currChapterSort && i < size -1) {
                        BookApi.ChapterMsg dbEntity = list.get(i + 1);
                        if(dbEntity != null)
                            return dbEntity.getChapterID();
                    }
                }
            }
        }
        return INVALID_CHAPTER_SORT;*/
    }

    /** 获取下一章节序号 **/
    public int getPrevChapterSort() {
        return getPrevChapterSort(getCurrChapterSort());
    }
    public int getPrevChapterSort(int currChapterSort) {
        return currChapterSort > 0 ? currChapterSort - 1 : INVALID_CHAPTER_SORT;
        /*if(mDirectoryData != null) {
            List<BookApi.ChapterMsg> list = mDirectoryData.getList();
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    BookApi.ChapterMsg entity = list.get(i);
                    //找到当前的chapterSort
                    if (entity != null && entity.getChapterID() == currChapterSort && i > 0) {
                        BookApi.ChapterMsg dbEntity = list.get(i - 1);
                        if(dbEntity != null)
                            return dbEntity.getChapterID();
                    }
                }
            }
        }
        return INVALID_CHAPTER_SORT;*/
    }

    private BookApi.ChapterMsg getEntity(int index) {
        if(mDirectoryData == null)
            return null;
        List<BookApi.ChapterMsg> list = mDirectoryData.getList();
        return (list != null && list.size() > index && index >= 0) ? list.get(index) : null;
    }

    /** 是否最后一章 **/
    public boolean isLastChapter() {
        return getCurrChapterSort() == mChapterCount - 1;
    }

    /** 是否第一章 **/
    public boolean isFirstChapter() {
        return getCurrChapterSort() == 0;
    }

    /** 是否存在该章节 **/
    public boolean isExistChapter(int chapterSort){
        return mChapterData != null && mChapterData.get(chapterSort) != null;
    }

    public float getProgress() {
        return progress;
    }

    public int getCurrChapterSort() {
        return mCurrChapterManager != null ? mCurrChapterManager.getChapterSort() : INVALID_CHAPTER_SORT;
    }

    public boolean isCurrChapter(BookApi.AckChapter.Data data) {
        return data != null && data.getChapterSortIndex() == getCurrChapterSort();
    }

    /** 获取当前章节 **/
    public ChapterManager getCurrChapterManager() {
        return mCurrChapterManager;
    }

    /** 获取下一章节 **/
    public ChapterManager getNextChapterManager() {
        return getChapterManager(getNextChapterSort());
    }

    @Nullable
    private ChapterManager createChapterManager(int chapterSort) {
        if(chapterSort == INVALID_CHAPTER_SORT || mChapterData == null)
            return null;
        ChapterManager manager = mChapterData.get(chapterSort);
        if (manager == null) {
            synchronized (mChapterData) {
                if (manager == null) {
                    int chapterIndex = getChapterIndex(chapterSort);
                    BookApi.ChapterMsg entity = getEntity(chapterIndex);
                    if (entity != null) {
                        BookApi.AckChapter.Data chapterEntity = BookApi.AckChapter.Data.newBuilder()
                                .setContent("")
                                .setChapterMsg(entity)
                                .setChapterSortIndex(chapterIndex)
                                .build();
                        manager = new ChapterManager(chapterEntity);
                        mChapterData.put(chapterSort, manager);
                    }
                }
            }
        }
        return manager;
    }

    /** 获取上一章节 **/
    public ChapterManager getPrevChapterManager() {
        return getChapterManager(getPrevChapterSort());
    }

    /**
     * 获取目录的章节
     */
    public BookApi.ChapterMsg getDirectoryEntity(int chapterSort) {
        if(chapterSort == INVALID_CHAPTER_SORT)
            return null;
        int index = getChapterIndex(chapterSort);
        return getEntity(index);
    }

    public int getChapterIndex(int chapterSort) {
        return chapterSort;
    }

    public ChapterManager getChapterManager(int chapterSort) {
        if (mChapterData == null)
            return null;
        return createChapterManager(chapterSort);
    }

    public int current() {
        return mCurrChapterManager == null ? 0 : mCurrChapterManager.current();
    }

    public int next(boolean back) {
        return mCurrChapterManager == null ? -1 : mCurrChapterManager.next(back);
    }

    public int prev(boolean back) {
        return mCurrChapterManager == null ? -1 : mCurrChapterManager.pre(back);
    }

    public char[] preLine(){
        return mCurrChapterManager == null ? null : mCurrChapterManager.preLine();
    }

    public char[] nextLine(){
        return mCurrChapterManager == null ? null : mCurrChapterManager.nextLine();
    }

    public void setPosition(int position) {
        if(mCurrChapterManager != null)
            mCurrChapterManager.setPosition(position);
    }

    public int getPosition() {
        return mCurrChapterManager == null ? -1 : mCurrChapterManager.getPosition();
    }

    public int getChapterLen() {
        return mCurrChapterManager == null ? 0 : mCurrChapterManager.getChapterLen();
    }

    public int getChapterCount() {
        return mChapterCount;
    }

    public String getCurrChapterName() {
        return mCurrChapterManager == null ? "" : mCurrChapterManager.getChapterName();
    }

    public String getNextChapterName() {
        ChapterManager manager = getNextChapterManager();
        return manager == null ? "" : manager.getChapterName();
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookID() {
        return mBookID;
    }

    //加载新的章节
    private ChapterEvent mChapterEvent;
    public interface ChapterEvent{
        //加载对应的章节
        void loadChapter(Set<Integer> chapterSortSet);

        void updateChapter(int position, int totalSize);
    }

    public void setChapterEvent(ChapterEvent mChapterEvent) {
        this.mChapterEvent = mChapterEvent;
    }

    private void loadChapter(Set<Integer> chapterSortSet) {
        if(mChapterEvent != null && !chapterSortSet.isEmpty())
            mChapterEvent.loadChapter(chapterSortSet);
    }

    /**
     * 更新阅读进度
     */
    private void updateReadProgress(int position) {
        if(mChapterEvent != null)
            mChapterEvent.updateChapter(position, mChapterCount);
    }

    public Set<Integer> getNearChapterSortSet(int chapterSort) {
        Set<Integer> set = new HashSet<>();
        if (chapterSort != INVALID_CHAPTER_SORT) {
            set.add(chapterSort);
            if (isAutoLoadNearChapter) {
                int nextChapterSort = getNextChapterSort(chapterSort);
                if(nextChapterSort != INVALID_CHAPTER_SORT)
                    set.add(nextChapterSort);
                int prevChapterSort = getPrevChapterSort(chapterSort);
                if(prevChapterSort != INVALID_CHAPTER_SORT)
                    set.add(prevChapterSort);
            }
        }
        return set;
    }

}


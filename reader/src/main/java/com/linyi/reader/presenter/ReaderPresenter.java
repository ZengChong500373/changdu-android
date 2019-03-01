package com.linyi.reader.presenter;

import android.text.TextUtils;

import com.api.changdu.proto.BookApi;
import com.linyi.reader.entity.DirectoryData;
import com.linyi.reader.manager.ChapterManager;
import com.linyi.reader.manager.FileManager;
import com.linyi.reader.manager.NovelManager;
import com.linyi.reader.net.module.BookModule;
import com.linyi.reader.net.module.ChapterModule;
import com.linyi.reader.ui.PageFactory2;
import com.linyi.reader.ui.ReaderActivity;
import com.linyi.reader.utils.NovelUtils;

import org.linyi.base.constant.Key;
import org.linyi.base.constant.SystemConstant;
import org.linyi.base.entity.db.BaseChapterDbEntity;
import org.linyi.base.entity.db.BaseNovelDbEntity;
import org.linyi.base.entity.db.DirectoryDbData;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.module.DirectoryModule;
import org.linyi.base.mvp.module.HistoryRecordModule;
import org.linyi.base.mvp.module.NovelModule;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.SharePreUtil;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/25 0025 下午 3:46
 */
public class ReaderPresenter implements BasePresenter {

    private static final String TAG = "ReaderPresenter";
    //当前请求章节序号
    private int mCurrChapterSort;
    //初次请求章节序号
    private int mInitChapterSort;
    //目录
    private DirectoryData mDirectoryData;
    //Activity
    private ReaderActivity mView;
    //书籍id
    private String mBookID;
    //是否添加到书架
    private boolean isAddBookshelf;

    public ReaderPresenter(ReaderActivity activity, String mBookID, int initChapterSort) {
        this.mView = activity;
        this.mBookID = mBookID;
        if (initChapterSort != NovelManager.INVALID_CHAPTER_SORT) {
            this.mCurrChapterSort = initChapterSort;
            this.mInitChapterSort = initChapterSort;
        }
    }

    @Override
    public void detachView() {
        mView = null;
        mDirectoryObserver.destroy();
        mChapterObserver.destroy();
    }

    private final CommonObserver<BookApi.AckChapterIndex> mDirectoryObserver = new CommonObserver<BookApi.AckChapterIndex>() {
        @Override
        public void onNext(BookApi.AckChapterIndex ackChapterIndex) {
            //本地，网络请求都可能会调用
            boolean hasRequestedDirectoryData = mDirectoryData != null;
            LogUtil.d(TAG, "hasRequestedDirectoryData = " + hasRequestedDirectoryData);
            mDirectoryData = new DirectoryData(ackChapterIndex.getChapterMsgCount(), ackChapterIndex.getChapterMsgList());
            NovelManager.getInstance().setDirectoryData(mDirectoryData, mCurrChapterSort);
            if(!hasRequestedDirectoryData)
                requestChapter(NovelManager.getInstance().getNearChapterSortSet(mCurrChapterSort), false);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            LogUtil.d(TAG, "mView = " + mView);
            if(mDirectoryData == null && mView != null)
                mView.onRequestError();
        }
    };

    private final CommonObserver<BookApi.AckChapter> mChapterObserver = new CommonObserver<BookApi.AckChapter>() {
        @Override
        public void onNext(BookApi.AckChapter ackChapter) {
            List<BookApi.AckChapter.Data> chapterDataList = ackChapter.getDataList();
            LogUtil.d("下载结果：" + chapterDataList);
            if (chapterDataList != null && !chapterDataList.isEmpty()) {
                for (BookApi.AckChapter.Data data : chapterDataList) {
                    NovelManager.getInstance().addChapter(data);
                    String content = data.getContent();
                    //写入到本地
                    if (!TextUtils.isEmpty(content)) {
                        String filePath = FileManager.getInstance().getNovelPath(data.getChapterMsg());
                        boolean result = FileManager.getInstance().writeText(filePath, content);
                        LogUtil.d("mChapterObserver", "filePath = " + filePath, "result = " + result, "data = " + data);
                        if (result) {
                            ChapterModule.save(data, filePath, 0);
                        }
                    }
                }
            }
        }

        @Override
        public void onComplete() {
            super.onComplete();
            if(mView != null)
                mView.onRequestCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            if (!NovelManager.getInstance().isExistChapter(mInitChapterSort) && mView != null) {
                mView.onRequestError();
            }
        }
    };

    public void requestData() {
        String bookID = mBookID;
        if (mDirectoryData == null) {
            //从本地获取目录，在网络请求后更新本地目录
            Observable.concat(getDirectoryDataFromDb(bookID), BookModule.directory(bookID))
                    .switchIfEmpty(new ObservableSource<BookApi.AckChapterIndex>() {
                        @Override
                        public void subscribe(Observer<? super BookApi.AckChapterIndex> observer) {
                            observer.onError(new NoSuchElementException());
                        }
                    })
                    .subscribe(mDirectoryObserver);
        } else {
            requestChapter(NovelManager.getInstance().getNearChapterSortSet(mCurrChapterSort), false);
        }
    }

    private Observable<BookApi.AckChapterIndex> getDirectoryDataFromDb(String bookID) {
        return Observable.just(bookID)
                .subscribeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckChapterIndex>() {
                    @Override
                    public BookApi.AckChapterIndex apply(String s) throws Exception {
                        DirectoryDbData data = DirectoryModule.getDirectoryDbData(s);
                        return BookApi.AckChapterIndex.parseFrom(data.getDirectoryData().getBytes(SystemConstant.CHARSET_NAME));
                    }
                })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends BookApi.AckChapterIndex>>() {
                    @Override
                    public ObservableSource<? extends BookApi.AckChapterIndex> apply(Throwable throwable) throws Exception {
                        LogUtil.d("获取本地目录失败");
                        return Observable.empty();
                    }
                });
    }

    /**
     * 请求章节数据
     * @param chapterSortSet 章节序号的集合 非章节ID
     * @param isBuy 是否需要购买
     */
    public void requestChapter(Set<Integer> chapterSortSet, final boolean isBuy) {
        LogUtil.d("requestChapter : chapterSortSet = " + chapterSortSet);
        if(mDirectoryData == null) return;
        //流程：获取小说目录(本地/网络) -> 找到当前章节及下一章、上一章序号 -> 根据章节序号获取内容(本地/网络) -> 缓存下载的章节
        Observable.just(chapterSortSet)
                .observeOn(Schedulers.newThread())
                .map(new Function<Set<Integer>, Set<Integer>>() {
                    @Override
                    public Set<Integer> apply(Set<Integer> mCurrChapterSort) throws Exception {
                        return getChapterIDArrData(mCurrChapterSort);//获取需要网络加载的章节ID
                    }
                })
                .subscribe(new CommonObserver<Set<Integer>>() {
                    @Override
                    public void onNext(Set<Integer> chapterSortsNet) {
                        getChapterDataByNet(NovelManager.getInstance().getBookID(), chapterSortsNet, isBuy);
                    }
                });
    }

    /**
     * 获取该章节及前后章节序号的数据(若本地没有, 就从网络获取, 之后再缓存到本地)
     * @param chapterSortsSet : 需要加载的章节序号集合
     * @return : 需要网络请求的章节ID集合
     */
    private Set<Integer> getChapterIDArrData(Set<Integer> chapterSortsSet) {
        //需要网络请求的章节ID
        Set<Integer> chapterSortsNet = new HashSet<>();
        if(chapterSortsSet == null || chapterSortsSet.isEmpty())
            return chapterSortsNet;
        for (Integer chapterSort : chapterSortsSet){
            BookApi.ChapterMsg entity = NovelManager.getInstance().getDirectoryEntity(chapterSort);
            if (entity != null) {
                //获取当前章节的本地内容
                boolean result = getChapterDataByDb(NovelUtils.createChapterUniqueID(entity));
                if(!result)
                    chapterSortsNet.add(entity.getChapterID());
            }
        }
        return chapterSortsNet;
    }

    private void getChapterDataByNet(String bookID, Set<Integer> chapterSortsNet, boolean isBuy) {
        //是否自动购买
        boolean isBuyBool = isBuy || SharePreUtil.getBoolean(Key.AUTO_SUBSCRIBE, false);
        BookModule.chapter(bookID, chapterSortsNet, isBuyBool).subscribe(mChapterObserver);
    }

    /** 通过本地获取章节内容 **/
    private boolean getChapterDataByDb(String uniqueID) {
        BaseChapterDbEntity dbEntity = ChapterModule.getChapterDbEntity(uniqueID);
        LogUtil.d(TAG, "getChapterDataByDb", "uniqueID = " + uniqueID, dbEntity == null ? " is null" : dbEntity.getFilePath());
        if (dbEntity != null && !TextUtils.isEmpty(dbEntity.getFilePath())) {
            String content = FileManager.getInstance().readText(dbEntity.getFilePath());
            String detailStr = dbEntity.getDetailStr();
            if (content != null && !TextUtils.isEmpty(detailStr)) {
                LogUtil.d("getChapterDataByDb", "[从本地获得缓存章节]", "uniqueID = " + uniqueID);
                BookApi.AckChapter.Data data = NovelUtils.parseChapterData(detailStr);
                BookApi.AckChapter.Data entity = data.toBuilder().setContent(content).build();
                NovelManager.getInstance().addChapter(entity);
                return true;
            }
        }
        LogUtil.d("getChapterDataByDb", "本地无缓存章节内容", "uniqueID = " + uniqueID);
        return false;
    }

    public NovelManager.ChapterEvent chapterEvent = new NovelManager.ChapterEvent() {

        /** 加载章节 **/
        @Override
        public void loadChapter(Set<Integer> chapterSortSet) {
            ReaderPresenter.this.requestChapter(chapterSortSet, false);
        }

        /** 更新章节 **/
        @Override
        public void updateChapter(int position, int totalSize) {

        }
    };

    public PageFactory2.PageEvent pageEvent = new PageFactory2.PageEvent() {
        @Override
        public void changeProgress(float progress, int page) {
            //更新章节进度(小说进度)
            BaseNovelDbEntity novelEntity = NovelManager.getInstance().getNovelEntity();
            if (novelEntity != null) {
                novelEntity.setLastChapterSort(NovelManager.getInstance().getCurrChapterSort());
                ChapterManager manager = NovelManager.getInstance().getCurrChapterManager();
                if (manager != null)
                    novelEntity.setPosition(manager.getPageBeginPos(page));
                NovelModule.updateRead(novelEntity);
            }
        }
    };

    public void saveHistoryRecord() {
        final String bookID = NovelManager.getInstance().getBookID();
        Observable.concat(NovelModule.getNovelData(bookID), BookModule.bookDetail(bookID))
                .switchIfEmpty(new ObservableSource<BookApi.BookOverView>() {
                    @Override
                    public void subscribe(Observer<? super BookApi.BookOverView> observer) {
                        observer.onError(new NoSuchElementException());
                    }
                })
                .subscribe(new CommonObserver<BookApi.BookOverView>() {
                    @Override
                    public void onNext(BookApi.BookOverView bookOverView) {
                        HistoryRecordModule.save(bookOverView);
                        isAddBookshelf = bookOverView.getHasBook();
                    }
                });
    }

    public boolean isAddBookshelf() {
        return isAddBookshelf;
    }
}

package com.linyi.reader.net.module;

import com.api.changdu.proto.BookApi;
import com.linyi.reader.net.api.BookAPI;

import org.linyi.base.http.RetrofitManager;
import org.linyi.base.manager.UserManager;
import org.linyi.base.mvp.module.DirectoryModule;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.help.ModuleHelp;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/1 0001 下午 5:18
 */
public class BookModule {

    private static BookAPI getService() {
        return RetrofitManager.getInstance().getService(BookAPI.class);
    }

    /**
     * 目录
     */
    public static Observable<BookApi.AckChapterIndex> directory(final String bookID) {
        return getService()
                .directory(ModuleHelp.geneRequestBody(BookApi.ReqChapterIndex
                        .newBuilder()
                        .setBookID(bookID)
                        .build()))
                .compose(ModuleHelp.commTrans(BookApi.AckChapterIndex.class))
                .doOnNext(new Consumer<BookApi.AckChapterIndex>() {
                    @Override
                    public void accept(BookApi.AckChapterIndex ackChapterIndex) throws Exception { //网络请求后保存到数据库
                        DirectoryModule.save(bookID, ackChapterIndex);
                    }
                })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends BookApi.AckChapterIndex>>() {
                    @Override
                    public ObservableSource<? extends BookApi.AckChapterIndex> apply(Throwable throwable) throws Exception {
                        return Observable.empty(); //出错是不走onError方法
                    }
                });
    }

    /**
     * 章节
     */
    public static Observable<BookApi.AckChapter> chapter(String bookID, Collection<Integer> chapterIDs, boolean isBuy) {
        if(chapterIDs == null || chapterIDs.isEmpty())
            return Observable.empty();
        StringBuilder sb = new StringBuilder();
        for (Integer chapterID : chapterIDs) {
            sb.append(",").append(chapterID);
        }
        LogUtil.d("bookID = " + bookID, "需要下载的章节 = " + sb.substring(1), "isBuy = " + isBuy);
        return getService()
                .chapter(ModuleHelp.geneRequestBody(BookApi.ReqChapter
                        .newBuilder()
                        .setBookID(bookID)
                        .setPurchase(isBuy ? 2 : 1)
                        .setChapterIds(sb.substring(1))
                        .build()))
                .compose(ModuleHelp.commTrans(BookApi.AckChapter.class))
                .doOnNext(new Consumer<BookApi.AckChapter>() {
                    @Override
                    public void accept(BookApi.AckChapter ackChapter) throws Exception {
                        UserManager.getInstance().saveScore(ackChapter.getScore());
                    }
                });
    }

    /**
     * 管理书架
     */
    public static Observable<BookApi.AckBookShelf> managerBookshelf(String bookID, boolean isAdd) {
        return getService().managerBookshelf(ModuleHelp.geneRequestBody(BookApi.ReqBookShelf.newBuilder()
                .setBookIds(bookID)
                .setIsAdd(isAdd ? 2 : 1)
                .build()))
                .compose(ModuleHelp.commTrans(BookApi.AckBookShelf.class));
    }


    /**
     * 详情页书籍
     */
    public static Observable<BookApi.BookOverView> bookDetail(String bookID) {
        return getService().bookDetail(ModuleHelp.geneRequestBody(BookApi.ReqBookDetail.newBuilder().setBookID(bookID).build()))
                .compose(ModuleHelp.commTrans(BookApi.AckBookDetail.class))
                .map(new Function<BookApi.AckBookDetail, BookApi.BookOverView>() {
                    @Override
                    public BookApi.BookOverView apply(BookApi.AckBookDetail ackBookDetail) throws Exception {
                        return ackBookDetail.getBookOverView();
                    }
                });
    }




}

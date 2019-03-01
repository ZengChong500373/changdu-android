package org.linyi.base.mvp.module;

import android.text.TextUtils;

import com.api.changdu.proto.BookApi;

import org.linyi.base.BaseApp;
import org.linyi.base.constant.SystemConstant;
import org.linyi.base.entity.db.BaseNovelDbEntity;
import org.linyi.base.entity.db.BookshelfDbEntity;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.utils.LogUtil;
import org.linyi.viva.db.BookshelfDbEntityDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/26 0026 上午 11:56
 */
public class BookshelfModule {

    private static BookshelfDbEntityDao getDao(){
        return BaseApp.getDaoSession().getBookshelfDbEntityDao();
    }

    /**
     * 保存/添加到书架
     * @param isTop 是否置顶
     */
    public static void save(BookApi.BookOverView view, boolean isTop) {
        if(view == null)
            return;
        String bookID = view.getBookID();
        BookshelfDbEntity dbEntity = getBookshelfEntity(bookID);
        if (dbEntity == null) {
            BaseNovelDbEntity entity = NovelModule.saveDetail(view);
            if (entity != null) {
                getDao().save(new BookshelfDbEntity(bookID, entity.getKeyID(), isTop, entity.getLastTime()));
            }
        } else {
            dbEntity.setIsTop(isTop);
            getDao().save(dbEntity);
        }
    }

    /**
     * 移出书架
     */
    public static boolean remove(String bookID) {
        BookshelfDbEntity entity = getBookshelfEntity(bookID);
        boolean result = entity != null;
        if(result)
            getDao().delete(entity);
        return result;
    }

    /**
     * 移出书架
     */
    public static void removeAll(Collection<String> list) {
        if(list == null || list.isEmpty())
            return;
        Observable.fromIterable(list)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        remove(s);
                    }
                });
    }

    public static BookshelfDbEntity getBookshelfEntity(String bookID) {
        if(TextUtils.isEmpty(bookID))
            return null;
        return getDao().queryBuilder().where(BookshelfDbEntityDao.Properties.BookID.eq(bookID)).unique();
    }

    /**
     * 是否加入到书架
     */
    public static boolean isAddBookshelf(String bookID) {
        return getBookshelfEntity(bookID) != null;
    }

    /**
     * 书架的数量
     */
    public static long getBookshelfCount() {
        return getDao().count();
    }

    public static void clear() {
        getDao().deleteAll();
    }

    /**
     * 获取历史记录列表
     * @param page 页数：从0开始
     * @param count 每页条数
     */
    private static Observable<List<BookshelfDbEntity>> getBookshelfList(final int page, final int count) {
        if(page < 0 || count < 0)
            return Observable.empty();
        return Observable.just(page)
                .subscribeOn(Schedulers.newThread())
                .map(new Function<Integer, List<BookshelfDbEntity>>() {
                    @Override
                    public List<BookshelfDbEntity> apply(Integer integer) throws Exception {
                        //先去置顶部分
                        List<BookshelfDbEntity> topList = getDao()
                                .queryBuilder()
                                .where(BookshelfDbEntityDao.Properties.IsTop.eq(true))
                                .limit(count)
                                .offset(page * count)
                                .list();
                        if(topList == null)
                            topList = new ArrayList<>();
                        int topCount = topList.size();
                        //之后按照时间排序
                        if (topCount < count) {
                            long topTotalCount = getDao()
                                    .queryBuilder()
                                    .where(BookshelfDbEntityDao.Properties.IsTop.eq(true))
                                    .count();
                            //置顶的页数
                            int topPage = (int) (topTotalCount / count);
                            int listCount = count - topCount;
                            LogUtil.d("BookshelfModule, getBookshelfList", "page = " + page, "count = " + count, "topTotalCount = " + topTotalCount, "topCount = " + topCount, "topPage = " + topPage);
                            List<BookshelfDbEntity> list = getDao()
                                    .queryBuilder()
                                    .where(BookshelfDbEntityDao.Properties.IsTop.eq(false))
                                    //按时间逆序
                                    .orderDesc(BookshelfDbEntityDao.Properties.Time)
                                    .limit(listCount)
                                    .offset((page - topPage)* listCount)
                                    .list();
                            topList.addAll(list);
                        }
                        return topList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<BookApi.AckMyBookShelf> getBookshelfData(final int page, final int count) {
        return getBookshelfList(page, count)
                .observeOn(Schedulers.newThread())
                .map(new Function<List<BookshelfDbEntity>, BookApi.AckMyBookShelf>() {
                    @Override
                    public BookApi.AckMyBookShelf apply(List<BookshelfDbEntity> list) throws Exception {
                        List<BookApi.AckMyBookShelf.Data> arr = new ArrayList<>();
                        for (BookshelfDbEntity entity : list) {
                            BaseNovelDbEntity bookEntity = entity.getBookEntity();
                            String detailStr = bookEntity.getDetailStr();
                            if (!TextUtils.isEmpty(detailStr)) {
                                BookApi.BookOverView bookOverView = BookApi.BookOverView.parseFrom(detailStr.getBytes(SystemConstant.CHARSET_NAME));
                                BookApi.AckMyBookShelf.Data data = BookApi.AckMyBookShelf.Data.newBuilder()
                                        .setBookOverView(bookOverView)
                                        .setIsTop(entity.getIsTop())
                                        .build();
                                arr.add(data);
                            }
                        }
                        long bookshelfCount = getBookshelfCount();
                        LogUtil.d("BookshelfModule, bookshelfCount = " + bookshelfCount);
                        return BookApi.AckMyBookShelf.newBuilder()
                                .addAllData(arr)
                                .setTotalElement(bookshelfCount)
                                .build();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 异步更新书架列表
     */
    public static void updateAllAsyn(BookApi.AckMyBookShelf data) {
        if(data == null)
            return;
        Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Function<BookApi.AckMyBookShelf, ObservableSource<BookApi.AckMyBookShelf.Data>>() {
                    @Override
                    public ObservableSource<BookApi.AckMyBookShelf.Data> apply(BookApi.AckMyBookShelf data) throws Exception {
                        return Observable.fromIterable(data.getDataList());
                    }
                })
                .subscribe(new CommonObserver<BookApi.AckMyBookShelf.Data>() {
                    @Override
                    public void onNext(BookApi.AckMyBookShelf.Data data) {
                        save(data.getBookOverView(), data.getIsTop());
                    }
                });
    }

    /**
     * 设置置顶
     */
    public static void setTop(String bookID, boolean isTop) {
        BookshelfDbEntity entity = getBookshelfEntity(bookID);
        if (entity != null) {
            entity.setIsTop(isTop);
            getDao().save(entity);
        }
    }

    /**
     * 是否置顶
     */
    public static boolean isTop(String bookID) {
        BookshelfDbEntity entity = getBookshelfEntity(bookID);
        return entity == null ? false : entity.getIsTop();
    }

}

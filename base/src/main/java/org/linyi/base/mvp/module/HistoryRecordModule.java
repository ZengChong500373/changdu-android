package org.linyi.base.mvp.module;

import android.text.TextUtils;

import com.api.changdu.proto.BookApi;

import org.linyi.base.BaseApp;
import org.linyi.base.entity.db.BaseNovelDbEntity;
import org.linyi.base.entity.db.HistoryRecordDbEntity;
import org.linyi.viva.db.HistoryRecordDbEntityDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/26 0026 上午 9:42
 */
public class HistoryRecordModule {

    private static HistoryRecordDbEntityDao getDao(){
        return BaseApp.getDaoSession().getHistoryRecordDbEntityDao();
    }

    public static void save(BookApi.BookOverView view) {
        if(view == null)
            return;
        String bookID = view.getBookID();
        HistoryRecordDbEntity dbEntity = getHistoryRecordEntity(bookID);
        if (dbEntity == null) {
            BaseNovelDbEntity entity = NovelModule.saveDetail(view);
            if (entity != null) {
                getDao().save(new HistoryRecordDbEntity(bookID, entity.getKeyID(), entity.getLastTime()));
            }
        }
    }

    public static HistoryRecordDbEntity getHistoryRecordEntity(String bookID) {
        if(TextUtils.isEmpty(bookID))
            return null;
        HistoryRecordDbEntityDao dao = getDao();
        return dao.queryBuilder().where(HistoryRecordDbEntityDao.Properties.BookID.eq(bookID)).unique();
    }

    /**
     * 获取历史记录列表
     * @param page 页数：从0开始
     * @param count 每页条数
     */
    public static Observable<List<HistoryRecordDbEntity>> getHistoryList(final int page, final int count) {
        if(page < 0 || count < 0)
            return Observable.empty();
        return Observable.just(page)
                .subscribeOn(Schedulers.newThread())
                .map(new Function<Integer, List<HistoryRecordDbEntity>>() {
                    @Override
                    public List<HistoryRecordDbEntity> apply(Integer integer) throws Exception {
                        List<HistoryRecordDbEntity> list = getDao()
                                .queryBuilder()
                                //按时间逆序
                                .orderDesc(HistoryRecordDbEntityDao.Properties.Time)
                                .limit(count)
                                .offset(page * count)
                                .list();
                        return list == null ? new ArrayList<HistoryRecordDbEntity>() : list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return 获取历史记录总条数
     */
    public static long getHistoryCount() {
        return getDao().count();
    }

    /**
     * 清空列表
     */
    public static void clear() {
        getDao().deleteAll();
    }

}

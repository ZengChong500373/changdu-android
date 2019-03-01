package org.linyi.base.mvp.module;

import android.text.TextUtils;

import com.api.changdu.proto.BookApi;

import org.linyi.base.BaseApp;
import org.linyi.base.constant.SystemConstant;
import org.linyi.base.entity.db.BaseChapterDbEntity;
import org.linyi.base.entity.db.BaseNovelDbEntity;
import org.linyi.base.entity.db.BookDetailType;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.db.BaseChapterDbEntityDao;
import org.linyi.viva.db.BaseNovelDbEntityDao;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/18 0018 下午 4:30
 */
public class NovelModule {

    private static BaseNovelDbEntityDao getDao() {
        return BaseApp.getDaoSession().getBaseNovelDbEntityDao();
    }

    /**
     * 保存书籍详情
     */
    public static BaseNovelDbEntity saveDetail(BookApi.BookOverView entity) {
        BaseNovelDbEntity dbEntity = null;
        if (entity != null) {
            BaseNovelDbEntityDao novelDao = getDao();
            dbEntity = getNovelDbEntity(entity.getBookID());
            if (dbEntity == null) {
                dbEntity = new BaseNovelDbEntity(entity.getBookID(), ModuleHelp.parseString(entity));
                dbEntity.setLastTime(System.currentTimeMillis());
                novelDao.save(dbEntity);
                LogUtil.d("插入新书籍：" + dbEntity.getBookID());
            } else {
                dbEntity.setDetailStr(ModuleHelp.parseString(entity));
                dbEntity.setLastTime(System.currentTimeMillis());
                novelDao.update(dbEntity);
                LogUtil.d("更新书籍详情：" + dbEntity.getBookID());
            }
        }
        return dbEntity;
    }

    /**
     * 更新阅读数据
     */
    public static void updateRead(BaseNovelDbEntity entity) {
        if (entity != null) {
            LogUtil.d("updateRead", entity.getBookID(), entity.getLastChapterSort(), entity.getPosition());
            BaseNovelDbEntityDao novelDao = getDao();
            BaseNovelDbEntity dbEntity = getNovelDbEntity(entity.getBookID());
            if (dbEntity == null) {
                novelDao.save(entity);
            } else {
                dbEntity.setLastTime(System.currentTimeMillis());
                if(entity.getLastChapterSort() >= 0)
                    dbEntity.setLastChapterSort(entity.getLastChapterSort());
                if(entity.getPosition() >= 0)
                    dbEntity.setPosition(entity.getPosition());
                novelDao.update(dbEntity);
            }
        }
    }

    public static void clear() {
        BaseNovelDbEntityDao novelDbEntityDao = getDao();
        novelDbEntityDao.deleteAll();
    }

    public static Observable<BookApi.BookOverView> getNovelData(String bookID) {
        if(TextUtils.isEmpty(bookID))
            return Observable.empty();
        return Observable.just(bookID)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BaseNovelDbEntity>() {
                    @Override
                    public BaseNovelDbEntity apply(String value) throws Exception {
                        return getNovelDbEntity(value);
                    }
                })
                .filter(new Predicate<BaseNovelDbEntity>() {
                    @Override
                    public boolean test(BaseNovelDbEntity entity) throws Exception {
                        return entity != null && !TextUtils.isEmpty(entity.getDetailStr());
                    }
                })
                .map(new Function<BaseNovelDbEntity, BookApi.BookOverView>() {
                    @Override
                    public BookApi.BookOverView apply(BaseNovelDbEntity entity) throws Exception {
                        return BookApi.BookOverView.parseFrom(entity.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                    }
                })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends BookApi.BookOverView>>() {
                    @Override
                    public ObservableSource<? extends BookApi.BookOverView> apply(Throwable throwable) throws Exception {
                        return Observable.empty();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static BaseNovelDbEntity getNovelDbEntity(String bookID) {
        if(TextUtils.isEmpty(bookID))
            return null;
        BaseNovelDbEntityDao novelDbEntityDao = getDao();
        return novelDbEntityDao.queryBuilder().where(BaseNovelDbEntityDao.Properties.BookID.eq(bookID)).unique();
    }


}

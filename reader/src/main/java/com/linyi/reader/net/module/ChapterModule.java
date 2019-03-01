package com.linyi.reader.net.module;

import android.text.TextUtils;

import com.api.changdu.proto.BookApi;
import com.linyi.reader.manager.FileManager;
import com.linyi.reader.utils.NovelUtils;

import org.linyi.base.BaseApp;
import org.linyi.base.entity.db.BaseChapterDbEntity;
import org.linyi.base.utils.LogUtil;
import org.linyi.viva.db.BaseChapterDbEntityDao;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/12 0012 下午 4:19
 */
public class ChapterModule {

    /**
     * 添加章节
     */
    public static void save(BookApi.AckChapter.Data entity, String filePath, long position) {
        if (entity != null) {
            BaseChapterDbEntityDao chapterDao = BaseApp.getDaoSession().getBaseChapterDbEntityDao();
            String uniqueID = NovelUtils.createChapterUniqueID(entity.getChapterMsg());
            BaseChapterDbEntity dbEntity = getChapterDbEntity(uniqueID);
            if (dbEntity == null) {
                dbEntity = new BaseChapterDbEntity(uniqueID, entity);
                dbEntity.setFilePath(filePath);
                if(position >= 0)
                    dbEntity.setPosition(position);
                chapterDao.save(dbEntity);
                LogUtil.d("插入新章节：" + uniqueID);
            } else {
                String savedFilePath = dbEntity.getFilePath();
                if (!TextUtils.isEmpty(filePath) && !TextUtils.equals(filePath, savedFilePath)) {
                    if (!TextUtils.isEmpty(savedFilePath)) {
                        File file = new File(savedFilePath);
                        //删除之前保存的文件
                        file.delete();
                    }
                    dbEntity.setFilePath(filePath);
                }
                if(position >= 0)
                    dbEntity.setPosition(position);
                chapterDao.update(dbEntity);
                LogUtil.d("更新章节：" + uniqueID);
            }
        }
    }

    public static void clear() {
        BaseChapterDbEntityDao chapterDao = BaseApp.getDaoSession().getBaseChapterDbEntityDao();
        chapterDao.deleteAll();
    }

    public static Observable<BookApi.AckChapter.Data> getChapterData(String uniqueID) {
        return Observable.just(uniqueID)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BaseChapterDbEntity>() {
                    @Override
                    public BaseChapterDbEntity apply(String value) throws Exception {
                        return getChapterDbEntity(value);
                    }
                })
                .filter(new Predicate<BaseChapterDbEntity>() {
                    @Override
                    public boolean test(BaseChapterDbEntity entity) throws Exception {
                        return entity != null && !TextUtils.isEmpty(entity.getFilePath());
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<BaseChapterDbEntity, BookApi.AckChapter.Data>() {
                    @Override
                    public BookApi.AckChapter.Data apply(BaseChapterDbEntity entity) throws Exception {
                        String content = FileManager.getInstance().readText(entity.getFilePath());
                        BookApi.AckChapter.Data data = NovelUtils.parseChapterData(entity.getDetailStr());
                        return data.newBuilderForType().setContent(content).build();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static BaseChapterDbEntity getChapterDbEntity(String uniqueID) {
        if(TextUtils.isEmpty(uniqueID))
            return null;
        BaseChapterDbEntityDao chapterDao = BaseApp.getDaoSession().getBaseChapterDbEntityDao();
        return chapterDao.queryBuilder().where(BaseChapterDbEntityDao.Properties.UniqueID.eq(uniqueID)).unique();
    }


}

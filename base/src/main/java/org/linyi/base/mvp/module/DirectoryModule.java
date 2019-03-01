package org.linyi.base.mvp.module;

import android.text.TextUtils;

import com.api.changdu.proto.BookApi;

import org.linyi.base.BaseApp;
import org.linyi.base.constant.SystemConstant;
import org.linyi.base.entity.db.DirectoryDbData;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.db.DirectoryDbDataDao;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/18 0018 下午 7:42
 */
public class DirectoryModule {

    public static void save(String bookID, BookApi.AckChapterIndex ackChapterIndex) {
        if (ackChapterIndex != null) {
            DirectoryDbDataDao dataDao = BaseApp.getDaoSession().getDirectoryDbDataDao();
            DirectoryDbData dbEntity = getDirectoryDbData(bookID);
            if (dbEntity == null) {
                dbEntity = new DirectoryDbData(bookID, ModuleHelp.parseString(ackChapterIndex));
                dataDao.save(dbEntity);
            } else {
                dbEntity.setDirectoryData(ModuleHelp.parseString(ackChapterIndex));
                dataDao.update(dbEntity);
            }
        }
    }

    public static DirectoryDbData getDirectoryDbData(String bookID) {
        if(TextUtils.isEmpty(bookID))
            return null;
        DirectoryDbDataDao dataDao = BaseApp.getDaoSession().getDirectoryDbDataDao();
        return dataDao.queryBuilder().where(DirectoryDbDataDao.Properties.BookID.eq(bookID)).unique();
    }

    public static void clear() {
        DirectoryDbDataDao dataDao = BaseApp.getDaoSession().getDirectoryDbDataDao();
        dataDao.deleteAll();
    }

public static Observable<BookApi.AckChapterIndex> getChapterIndexCache(final String bookID){
        return Observable.just(bookID)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckChapterIndex>() {
                    @Override
                    public BookApi.AckChapterIndex apply(String s) throws Exception {
                        DirectoryDbData data=getDirectoryDbData(bookID);
                        BookApi.AckChapterIndex ackChapterIndex=null;
                        if (data != null && !TextUtils.isEmpty(data.getDirectoryData())){
                            ackChapterIndex= BookApi.AckChapterIndex.parseFrom(data.getDirectoryData().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ackChapterIndex;
                    }
                }).observeOn(AndroidSchedulers.mainThread());

}
}

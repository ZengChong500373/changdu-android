package org.linyi.base.mvp.module;

import org.linyi.base.BaseApp;
import org.linyi.base.entity.db.HistoryRecordEntity;
import org.linyi.viva.db.HistoryRecordEntityDao;


import java.util.List;

public class HistoryModule {
    public static HistoryRecordEntityDao getDao(){
        return BaseApp.getDaoSession().getHistoryRecordEntityDao();
    }
    public static List<HistoryRecordEntity> getAllHistory(){
           return getDao().loadAll();
    }
}

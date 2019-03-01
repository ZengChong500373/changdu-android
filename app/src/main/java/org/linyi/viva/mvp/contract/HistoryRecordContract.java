package org.linyi.viva.mvp.contract;


import org.linyi.base.entity.db.HistoryRecordDbEntity;
import org.linyi.base.entity.db.HistoryRecordEntity;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;

import java.util.List;

public interface HistoryRecordContract {
    interface Presenter extends BasePresenter {
        void init();
        void delete();
    }
    interface View extends BaseView {
        void onLoadSuccess(List<HistoryRecordDbEntity> list);
        void isEmpty(boolean isEmpty);
        void onDeleteSuccess();
    }


}

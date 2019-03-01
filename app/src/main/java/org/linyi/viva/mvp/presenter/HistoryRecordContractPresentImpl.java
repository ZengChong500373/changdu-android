package org.linyi.viva.mvp.presenter;

import org.linyi.base.entity.db.HistoryRecordDbEntity;
import org.linyi.base.mvp.module.HistoryRecordModule;
import org.linyi.viva.mvp.contract.HistoryRecordContract;
import java.util.List;

import io.reactivex.functions.Consumer;

public class HistoryRecordContractPresentImpl implements HistoryRecordContract.Presenter {
    public HistoryRecordContractPresentImpl(HistoryRecordContract.View view) {
        this.view = view;
    }
    HistoryRecordContract.View view;
    @Override
    public void init() {
        HistoryRecordModule.getHistoryList(0,1000).subscribe(new Consumer<List<HistoryRecordDbEntity>>() {
            @Override
            public void accept(List<HistoryRecordDbEntity> list) throws Exception {
                if (list==null||list.size()==0){
                    view.isEmpty(true);
                }else {
                    view.isEmpty(false);

                }
                view.onLoadSuccess(list);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    @Override
    public void delete() {

    }

    @Override
    public void detachView() {

    }
}

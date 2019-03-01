package org.linyi.viva.mvp.contract;

import com.api.changdu.proto.BookApi;

import org.linyi.base.mvp.BaseInteractor;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;


public interface MainStackContract {
    interface Presenter extends BasePresenter {
        void init();

    }
    interface View extends BaseView {
       void onLoadSuccess(BookApi.AckDomainRecommend data);
       void showEmpty();
    }

    interface Interactor extends BaseInteractor {
        void init();
        void loadCache();
    }

}

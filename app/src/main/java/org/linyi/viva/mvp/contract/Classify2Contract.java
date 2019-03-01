package org.linyi.viva.mvp.contract;





import com.api.changdu.proto.BookApi;

import org.linyi.base.mvp.BaseInteractor;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;



public interface Classify2Contract {
    interface Presenter extends BasePresenter {
             void titleList(String name);
    }
    interface View extends BaseView {
        void onLoadSuccess(BookApi.AckRankOption data);
    }

    interface Interactor extends BaseInteractor {
        void  titleList(String name);
        void loadCache(String name);

    }
}

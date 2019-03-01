package org.linyi.viva.mvp.contract;


import com.api.changdu.proto.BookApi;

import org.linyi.base.mvp.BaseInteractor;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;

import java.util.List;

public interface SearchContract {
    interface Presenter extends BasePresenter {
        void init();

        void searchData(String str);

        void forAnother();

        void loadingHead();

        void loadingFoot();
        void searchHistory();
        void cleanSearchHistory();
    }

    interface View extends BaseView {
        void onShowHotWords(List<String> data);

        void onShowHistoryWords(List<String> data);

        void searchSuccess(BookApi.AckSearch data, int page);

        void setTemp(int type);

        void showRefresh();
    }

    interface Interactor extends BaseInteractor {
        void searchHot(int type);

        void searchHistory();

        void searchData(String str, int page, int type);

    }

    interface CallBack {
        void onHotSuccess(List<String> list);

        void searchSuccess(BookApi.AckSearch data, int page);
        void searchHistoryList(List<String> list);
        void onError(String error);

    }
}

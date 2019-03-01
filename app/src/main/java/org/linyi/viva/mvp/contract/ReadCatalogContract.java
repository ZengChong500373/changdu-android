package org.linyi.viva.mvp.contract;

import com.api.changdu.proto.BookApi;

import org.linyi.base.mvp.BaseInteractor;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;

import java.util.List;


public interface ReadCatalogContract {
    interface Presenter extends BasePresenter {

        void loadData(String id);
        void sortData(String id);

    }
    interface View extends BaseView {
        void onLoadSuccess(List<BookApi.ChapterMsg> list,int type);
        void setTemp(int type);
        List<BookApi.ChapterMsg> getAdapterData();
        String getSort();
        void sortData(int type);

    }

    interface Interactor extends BaseInteractor {
        void loadData(String id);
        void loadCache(String id);
    }
    interface CallBack {
        void onLoadSuccess(BookApi.AckChapterIndex data);
        void onError(String error);

    }
}

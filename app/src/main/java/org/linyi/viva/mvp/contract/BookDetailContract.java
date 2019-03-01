package org.linyi.viva.mvp.contract;

import com.api.changdu.proto.BookApi;

import org.linyi.base.mvp.BaseInteractor;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;

import java.util.List;

public interface BookDetailContract {
    interface Presenter extends BasePresenter {
        void init(String id);

        void addOrRemoveBook();
void onResume(String id);

    }

    interface View extends BaseView {
        void onInitSuccess(BookApi.AckBookDetail t);

        String getId();

        String getShelfText();

        void setShelfText(String str,boolean show);

        void setTemp(int type);
    }

    interface Interactor extends BaseInteractor {
        void init(String id);

        void addOrRemoveBook(String id, int isAdd);
        void loadCache(String id);
    }

    interface CallBack {
        void onInitSuccess(BookApi.AckBookDetail detail);

        void onLoadFaile();

        void onAddOrRemoveSuccess(BookApi.AckBookShelf data,boolean show);
    }
}

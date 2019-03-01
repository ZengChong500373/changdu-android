package org.linyi.viva.mvp.contract;

import android.content.Context;

import com.api.changdu.proto.BookApi;

import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;

import java.util.List;
public interface MainShelfContract {
    interface Presenter extends BasePresenter {
        void loadHead();
        void loadMore();
        void onClick(Context mContext, BookApi.AckMyBookShelf.Data data);
        void onVisible();
        void delete(List<String> list);
        void top(BookApi.AckMyBookShelf.Data data);
    }
    interface View extends BaseView {
        void onLoadHeadSuccess(BookApi.AckMyBookShelf data);
        void onLoadMoreSuccess(BookApi.AckMyBookShelf data);
        void showEmpty();
        void showData();
        void removeSuccess(BookApi.AckBookShelf data);
        void showRefresh();
        void finishRefresh();
        List<BookApi.AckMyBookShelf.Data> getAdapterData();
        void topSuccess(BookApi.AckTopBookShelf data);
    }

    interface Interactor {
        void removeBook(List<String> list);
        void loadData(int page,int type);
        void destroy();
        void topBookShelf(String id,int isTop);
    }

    interface CallBack {
        void onLoadSuccess(int page,BookApi.AckMyBookShelf data);
        void onError(String str);
        void removeSuccess(BookApi.AckBookShelf data);
        void topSuccess(BookApi.AckTopBookShelf data);
    }
}


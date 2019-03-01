package org.linyi.viva.mvp.contract;

import com.api.changdu.proto.BookApi;

import org.linyi.base.mvp.BaseInteractor;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;

import java.util.List;

public interface RankContract {
      interface Presenter extends BasePresenter {
        void init();
        void loadOnClick();
        void loadingHead();
        void loadingFoot();
    }
    interface View<T> extends BaseView {
        void onInitSuccess(BookApi.AckIndexRankingList  data);
        /**
         * 加载头部成功
         * */
        void onloadHeadSuccess(T data);
        /**
         * 加载头部数据失败
         * */
        void onloadError(String error);
        /**
         * 加载更多成功
         * */
        void onloadMoreSuccess(T data);
        void showRefresh();
        void finishRefresh();

        String getRankName();
        String getSiteName();

        void switchAdapter(String site, String rank);

    }

    interface Interactor extends BaseInteractor {
        void init();
        void loadRankDetail(String siteName,String rankName,int currentPage);
        void loadInitCache();
        void loadRankDetailCache(String siteName,String rankName,int currentPage);
    }
    interface CallBack<T> {
        void onLoadHeadSuccess(T data);
        void onloadMoreSuccess(T data);
        void onInitSuccess(BookApi.AckIndexRankingList data);
        void onError(String error);

    }
}

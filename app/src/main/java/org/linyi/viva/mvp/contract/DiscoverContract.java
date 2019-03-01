package org.linyi.viva.mvp.contract;

import org.linyi.base.mvp.BaseInteractor;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;



public interface DiscoverContract {
    interface Presenter extends BasePresenter {
        void loadingHead();
        void loadingFoot();
    }
    interface View<T> extends BaseView {
        /**
         * 加载头部成功
         * */
        void onloadSuccess(T data,int page);
        /**
         * 加载头部数据失败
         * */
        void onloadError(String error);
        void setTemp(int type);
    }

    interface Interactor extends BaseInteractor {
        void loadData(int page,int type);
    }
    interface CallBack<T> {
        void onLoadSuccess(T data,int page);
        void onError(String error);

    }
}

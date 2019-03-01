package org.linyi.viva.mvp.contract;
import org.linyi.base.mvp.BaseInteractor;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;
import java.util.List;


public interface Classify2ListContract {
    interface Presenter extends BasePresenter {
        void init(String category,String title);
        void loadingHead();
        void loadingFoot();
    }
    interface View<T> extends BaseView {
        /**
         * 加载头部成功
         * */
        void onloadHeadSuccess(List<T> data);
        /**
         * 加载头部数据失败
         * */
        void onloadError(String error);
        /**
         * 加载更多成功
         * */
        void onloadMoreSuccess(List<T> data);
    }

    interface Interactor extends BaseInteractor {
        void loadData(String category,String title,int position,int type);
    }
    interface CallBack<T> {
        void onLoadHeadSuccess(T data);
        void onloadMoreSuccess(T data);

        void onError(String error);

    }
}

package org.linyi.viva.mvp.contract;
import org.linyi.base.mvp.BaseInteractor;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;

public interface CommonContract {
    interface Presenter<T> extends BasePresenter {
             void init(T t);
    }
    interface View<T> extends BaseView {
        void onLoadSuccess(T t);
        void setTemp(int type);
    }

    interface Interactor<T> extends BaseInteractor {
        void  init(T t);
    }
}

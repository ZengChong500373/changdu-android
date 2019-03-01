package org.linyi.viva.mvp.presenter;

import com.api.changdu.proto.BookApi;

import org.linyi.base.listener.M2PListener;
import org.linyi.base.utils.NetUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.viva.mvp.contract.ClassifyContract;
import org.linyi.viva.mvp.interactor.ClassifyInteractorImpl;

public class ClassifyPresenterImpl implements ClassifyContract.Presenter, M2PListener<BookApi.AckCategoryList> {
    ClassifyContract.View view;
    ClassifyContract.Interactor interactor;

    public ClassifyPresenterImpl(ClassifyContract.View view) {
        this.view = view;
        interactor = new ClassifyInteractorImpl(this);
    }


    @Override
    public void init() {
        view.showLoading();
        if (NetUtil.isNetworkAvailable()) {
            interactor.init();
        } else {
            interactor.loadCache();
        }
    }

    @Override
    public void detachView() {
        view = null;
        interactor.onDestroy();
    }

    @Override
    public void onLoadSuccess(BookApi.AckCategoryList ackCategoryList) {
        view.hideLoading();
        if (ackCategoryList != null) {
            view.onLoadSuccess(ackCategoryList);
        } else {
            view.showMsg(UIUtils.getString(org.linyi.base.R.string.load_error));
        }
    }

    @Override
    public void onLoadFaile() {
        view.hideLoading();
        view.showMsg(UIUtils.getString(org.linyi.base.R.string.load_error));
    }
}

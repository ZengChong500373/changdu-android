package org.linyi.viva.mvp.presenter;


import com.api.changdu.proto.BookApi;

import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.NetUtil;

import org.linyi.base.utils.UIUtils;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.DiscoverContract;
import org.linyi.viva.mvp.interactor.DiscoverInteractorImpl;

public class DiscoverPresenterImpl implements DiscoverContract.Presenter, DiscoverContract.CallBack<BookApi.AckBookThemeIntro> {
  private   DiscoverContract.View<BookApi.AckBookThemeIntro> view;
  private   DiscoverContract.Interactor interactor;
  private   int currentPage = 1;
  private int maxPage=1;
    public DiscoverPresenterImpl(DiscoverContract.View<BookApi.AckBookThemeIntro> view) {
        this.view = view;
        interactor = new DiscoverInteractorImpl(this);
    }

    @Override
    public void loadingHead() {
        view.showLoading();
        if (NetUtil.isNetworkAvailable()) {
            currentPage = 1;
            interactor.loadData(1, 0);
        } else {
            interactor.loadData(1, 1);
        }
    }

    @Override
    public void loadingFoot() {
        if (currentPage>maxPage){
            view.showMsg(UIUtils.getString(R.string.this_is_max_page));
            view.hideLoading();
            return;
        }
        if (NetUtil.isNetworkAvailable()) {
            interactor.loadData(currentPage, 0);
        } else {
            interactor.loadData(currentPage, 1);
        }
    }


    @Override
    public void onLoadSuccess(BookApi.AckBookThemeIntro data, int page) {
        view.hideLoading();
        currentPage++;
        maxPage=   data.getMaxPage();
        if (data != null && data.getDataCount() > 0) {
            view.onloadSuccess(data, page);
        } else if (data != null && data.getDataCount() == 0) {
            view.setTemp(TempView.STATUS_NULL);
        } else {
            view.setTemp(TempView.STATUS_ERROR);
        }
    }

    @Override
    public void onError(String error) {
        view.setTemp(TempView.STATUS_ERROR);
    }

    @Override
    public void detachView() {
        interactor.onDestroy();
        view=null;
    }
}

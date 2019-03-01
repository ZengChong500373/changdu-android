package org.linyi.viva.mvp.presenter;


import com.api.changdu.proto.BookApi;
import com.linyi.reader.manager.NovelManager;

import org.linyi.base.inf.CommonObserver;
import org.linyi.base.mvp.module.NovelModule;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.NetUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.BookDetailContract;
import org.linyi.viva.mvp.interactor.BookDetailInteractorImpl;


public class BookDetailPresentImpl implements BookDetailContract.Presenter, BookDetailContract.CallBack {
    private BookDetailContract.View view;
    private BookDetailContract.Interactor interactor;

    public BookDetailPresentImpl(BookDetailContract.View view) {
        this.view = view;
        interactor = new BookDetailInteractorImpl(this);
    }

    @Override
    public void init(String id) {
        view.showLoading();
        if (NetUtil.isNetworkAvailable()) {
            interactor.init(id);
        } else {
            interactor.loadCache(id);
        }
    }

    @Override
    public void addOrRemoveBook() {
        String txt = view.getShelfText();
        if (txt.equals(UIUtils.getString(R.string.add_shelf))) {
            interactor.addOrRemoveBook(view.getId(), 2);
        } else {
            interactor.addOrRemoveBook(view.getId(), 1);
        }
    }

    @Override
    public void onResume(String id) {
        NovelModule.getNovelData(id)
                .subscribe(new CommonObserver<BookApi.BookOverView>() {
                    @Override
                    public void onNext(BookApi.BookOverView bookOverView) {
                        if (bookOverView != null && bookOverView.getHasBook()) {
                            view.setShelfText(UIUtils.getString(R.string.remove_shelf),false);
                        } else {
                            view.setShelfText(UIUtils.getString(R.string.add_shelf),false);
                        }
                    }
                });
    }


    @Override
    public void onInitSuccess(BookApi.AckBookDetail data) {
        view.hideLoading();
        if (data != null) {
            view.onInitSuccess(data);
        } else {
            view.setTemp(TempView.STATUS_ERROR);
        }
    }

    @Override
    public void onLoadFaile() {
        view.hideLoading();
        view.setTemp(TempView.STATUS_ERROR);
    }

    @Override
    public void onAddOrRemoveSuccess(BookApi.AckBookShelf data,boolean show) {
        BookApi.BookOverView bookOverView = data.getBookOverView(0);
        if (bookOverView != null && bookOverView.getHasBook()) {
            view.setShelfText(UIUtils.getString(R.string.remove_shelf),show);
        } else {
            view.setShelfText(UIUtils.getString(R.string.add_shelf),show);
        }
    }


    @Override
    public void detachView() {
        interactor.onDestroy();
        view = null;
    }


}

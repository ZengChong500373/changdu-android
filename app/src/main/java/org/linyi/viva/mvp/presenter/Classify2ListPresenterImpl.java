package org.linyi.viva.mvp.presenter;

import com.api.changdu.proto.BookApi;

import org.linyi.base.utils.NetUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.viva.App;
import org.linyi.viva.R;
import org.linyi.viva.entity.BookListEntity;
import org.linyi.viva.mvp.contract.Classify2ListContract;
import org.linyi.viva.mvp.interactor.Classify2ListInteractorImpl;

import java.util.HashMap;
import java.util.Map;

public class Classify2ListPresenterImpl implements Classify2ListContract.Presenter, Classify2ListContract.CallBack<BookApi.AckBookList> {
    /**
     * 当前加载的角标
     * 初始化是加载头
     * 角标为0
     * 每次下来一下角标+1
     */
    private int current_position = 1;
    private int max_page=-1;
    private Boolean isFrist = true;
    private  String category;
    private   String title;
    private  Classify2ListContract.View<BookApi.BookOverView> view;
    private Classify2ListContract.Interactor interactor;

    public Classify2ListPresenterImpl(Classify2ListContract.View view) {
        this.view = view;
        interactor = new Classify2ListInteractorImpl(this);
    }


    @Override
    public void init(String category, String title) {
        this.category = category;
        this.title = title;
    }

    @Override
    public void loadingHead() {
        if (isFrist) {
            view.showLoading();
        }
        current_position = 1;
        if (NetUtil.isNetworkAvailable()){
            interactor.loadData(category, title, current_position,0);
        }else {
            interactor.loadData(category, title, current_position,1);
        }
    }

    @Override
    public void loadingFoot() {
        if (current_position>max_page){
            view.showMsg(UIUtils.getString(R.string.this_is_max_page));
            view.hideLoading();
            return;
        }
        if (NetUtil.isNetworkAvailable()){
            interactor.loadData(category, title, current_position,0);
        }else {
            interactor.loadData(category, title, current_position,1);
        }
    }


    @Override
    public void onLoadHeadSuccess(BookApi.AckBookList data) {
        view.hideLoading();
        if (data != null) {
            view.onloadHeadSuccess(data.getBookOverViewList());
        } else {
            view.onloadError(App.getContext().getString(R.string.load_faile));
            return;
        }
        max_page=data.getMaxPage();
        current_position++;
        if (isFrist) {
            isFrist = false;
        }
    }

    @Override
    public void onloadMoreSuccess(BookApi.AckBookList data) {
        view.hideLoading();
        if (data != null) {
            view.onloadMoreSuccess(data.getBookOverViewList());
        } else {
            view.onloadError(App.getContext().getString(R.string.load_faile));
        }
        current_position++;
    }

    @Override
    public void onError(String error) {
        view.hideLoading();
        view.onloadError(App.getContext().getString(R.string.load_faile));
    }


    @Override
    public void detachView() {
        interactor.onDestroy();
        view=null;

    }
}

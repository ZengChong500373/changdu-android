package org.linyi.viva.mvp.presenter;


import com.api.changdu.proto.BookApi;

import org.linyi.base.entity.db.VariousEntity;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.NetUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.SearchContract;
import org.linyi.viva.mvp.interactor.SearchContractInteractorImpl;

import java.util.List;

public class SearchContractPresentImpl implements SearchContract.Presenter, SearchContract.CallBack {
    private  SearchContract.View view;
    private SearchContract.Interactor interactor;
    private String currentStr;
    private int currentPage;
    private int maxPage;
    public SearchContractPresentImpl(SearchContract.View view) {
        this.view = view;
        this.interactor=new SearchContractInteractorImpl(this);
    }
    @Override
    public void init() {
        view.showLoading();
        if (NetUtil.isNetworkAvailable()){
            interactor.searchHot(0);
        }else {
            interactor.searchHot(1);
        }
        interactor.searchHistory();
    }
    @Override
    public void searchData(String str) {
        view.showLoading();
        this.currentStr=str;
        currentPage=1;
        if (NetUtil.isNetworkAvailable()){
            interactor.searchData(str,currentPage,0);
        }else {
            interactor.searchData(str,currentPage,1);
        }
    }

    @Override
    public void forAnother() {
        view.showLoading();
        if (NetUtil.isNetworkAvailable()){
            interactor.searchHot(0);
        }else {
            interactor.searchHot(1);
        }
    }

    @Override
    public void loadingHead() {
        currentPage=1;
        view.showRefresh();
        if (NetUtil.isNetworkAvailable()){
            interactor.searchData(currentStr,currentPage ,0);
        }else {
            interactor.searchData(currentStr,currentPage,1);
        }
    }

    @Override
    public void loadingFoot() {
        if (maxPage<currentPage){
            view.showMsg(UIUtils.getString(R.string.this_is_max_page));
            return;
        }
        if (NetUtil.isNetworkAvailable()){
            interactor.searchData(currentStr,currentPage,0);
        }else {
            interactor.searchData(currentStr,currentPage,1);
        }
    }

    @Override
    public void searchHistory() {
        interactor.searchHistory();
    }

    @Override
    public void cleanSearchHistory() {
        VariousModule.cleanSearchTag();
    }

    @Override
    public void onHotSuccess(List<String> data) {
        view.hideLoading();
        if (data!=null){
            view.onShowHotWords(data);
        }else {

        }
    }

    @Override
    public void searchSuccess(BookApi.AckSearch data,int page) {
        view.hideLoading();
        maxPage=  data.getMaxPage();
        if (data==null||data.getBookOverViewCount()==0){
            view.setTemp(TempView.STATUS_DISMISS);
            view.showMsg(UIUtils.getString(R.string.dont_have_data));
        }else {
            currentPage++;
            view.searchSuccess(data,page);
        }
    }

    @Override
    public void searchHistoryList(List<String> list) {
        view.hideLoading();
        if (list!=null){
            view.onShowHistoryWords(list);
        }else {

        }
    }

    @Override
    public void onError(String error) {
        view.setTemp(TempView.STATUS_ERROR);
    }

    @Override
    public void detachView() {

    }
}

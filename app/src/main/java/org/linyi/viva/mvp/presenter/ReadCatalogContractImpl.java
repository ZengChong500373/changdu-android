package org.linyi.viva.mvp.presenter;


import com.api.changdu.proto.BookApi;

import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.NetUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.ReadCatalogContract;
import org.linyi.viva.mvp.interactor.ReadCatalogContractInteractorImpl;

public class ReadCatalogContractImpl implements ReadCatalogContract.Presenter,ReadCatalogContract.CallBack{
    public ReadCatalogContractImpl(ReadCatalogContract.View view) {
        this.view = view;
        this.interactor=new ReadCatalogContractInteractorImpl(this);
    }
    private ReadCatalogContract.View view;
    private  ReadCatalogContract.Interactor interactor;
    @Override
    public void loadData(String id) {
        view.showLoading();
        if (NetUtil.isNetworkAvailable()){
            interactor.loadData(id);
        }else {
            interactor.loadCache(id);
        }

    }

    @Override
    public void sortData(String id) {
        String sort= view.getSort();
        if (view.getAdapterData().size()>0&&sort.equals(UIUtils.getString(R.string.positive_order))){
           view.sortData(1);
        }else if (view.getAdapterData().size()>0&&sort.equals(UIUtils.getString(R.string.inverted_order))){
            view.sortData(0);
        }else {
            interactor.loadData(id);
        }
    }


    @Override
    public void onLoadSuccess(BookApi.AckChapterIndex data) {
        view.hideLoading();
        if (data!=null&&data.getChapterMsgCount()>0){
            int type=UIUtils.getString(R.string.positive_order).equals(view.getSort())?0:1;
            view.onLoadSuccess(data.getChapterMsgList(),type);
        }else if (data!=null&&data.getChapterMsgCount()==0){
            view.setTemp(TempView.STATUS_NULL);
        }else {
            view.setTemp(TempView.STATUS_ERROR);
        }
    }

    @Override
    public void onError(String error) {
        view.hideLoading();
        if (view.getAdapterData()==null||view.getAdapterData().size()==0){
            view.setTemp(TempView.STATUS_ERROR);
        }
    }
    @Override
    public void detachView() {

    }

}

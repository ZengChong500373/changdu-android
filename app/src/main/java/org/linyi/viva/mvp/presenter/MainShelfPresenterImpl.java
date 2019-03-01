package org.linyi.viva.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;
import com.api.changdu.proto.BookApi;

import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.NetUtil;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.help.TurnHelp;
import org.linyi.viva.App;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.MainShelfContract;
import org.linyi.viva.mvp.interactor.MainShelfInteractorImpl;
import org.linyi.viva.ui.activity.MainActivity;

import java.util.List;

public class MainShelfPresenterImpl implements MainShelfContract.Presenter,MainShelfContract.CallBack {
    MainShelfContract.View view;
    MainShelfContract.Interactor interactor;
    private int currentPage=1;
    public MainShelfPresenterImpl(MainShelfContract.View view) {
        this.view = view;
        interactor=new MainShelfInteractorImpl(this);
    }
    @Override
    public void loadHead() {
        LogUtil.stackTraces();
        view.showRefresh();
         if (NetUtil.isNetworkAvailable()){
              interactor.loadData(1,0);
         }else {
             interactor.loadData(1,1);
         }
    }

    @Override
    public void loadMore() {
        if (NetUtil.isNetworkAvailable()){
            interactor.loadData(currentPage,0);
        }else {
           interactor.loadData(currentPage,1);
        }
    }

    @Override
    public void onClick(Context mContext, BookApi.AckMyBookShelf.Data data) {
        if (data==null){
            MainActivity.rg_main_parent.check(R.id.rg_stack);
            MainActivity.vp_main.setCurrentItem(1,false);
        }else {
            SharePreUtil.putString(data.getBookOverView().getBookID()+"_LastChapterTitle",data.getBookOverView().getLastChapterTitle());
            TurnHelp.reader(mContext,data.getBookOverView().getBookID(),data.getBookOverView().getTitle());
        }
    }

    @Override
    public void onVisible() {
        if (App.getIsNeedRefreshShelf()){
            loadHead();
        }
    }

    @Override
    public void delete(List<String> list) {
        if (list==null){
            view.showMsg("数据异常");
            return;
        }
        if (NetUtil.isNetworkAvailable()){
            view.showLoading();
            interactor.removeBook(list);
        }else {
            view.showMsg("清检查网络");
        }
    }

    @Override
    public void top(BookApi.AckMyBookShelf.Data data) {
        if (NetUtil.isNetworkAvailable()){
            view.showLoading();
          String id =data.getBookOverView().getBookID();
          int isTop=-1;
           if (data.getIsTop()|| SharePreUtil.getBoolean(id,false)){
               isTop=1;
           }else {
               isTop=2;
           }
           interactor.topBookShelf(id,isTop);
        }else {
            view.showMsg("清检查网络");
        }
    }

    @Override
    public void detachView() {
        interactor.destroy();
    }


    @Override
    public void onLoadSuccess(int page,BookApi.AckMyBookShelf data) {
        view.finishRefresh();
        App.setIsNeedRefreshShelf(false);
        if (data==null){
             return;
        }
        if (data.getDataCount()==0&&page==1){
            view.showEmpty();
            return;
        }else {
            view.showData();
        }
        currentPage++;
        if (page==1){
            view.onLoadHeadSuccess(data);
        }else {
            view.onLoadMoreSuccess(data);
        }
    }

    @Override
    public void onError(String str) {
        view.hideLoading();
        if (view.getAdapterData()==null||view.getAdapterData().size()==0){
            view.showEmpty();
        }
        view.showMsg(" ...");
    }

    @Override
    public void removeSuccess(BookApi.AckBookShelf data) {
          view.hideLoading();
          view.finishRefresh();
         if (data!=null){
             view.removeSuccess(data);
         }else {
             view.showMsg("...");
         }
    }

    @Override
    public void topSuccess(BookApi.AckTopBookShelf data) {
        view.hideLoading();
        view.finishRefresh();
        if (data!=null&&NetUtil.isNetworkAvailable()){
          interactor.loadData(1,0);
        }else if(data!=null&&!NetUtil.isNetworkAvailable()){
            view.topSuccess(data);
        }else {
            view.showMsg("...");
        }
    }

}

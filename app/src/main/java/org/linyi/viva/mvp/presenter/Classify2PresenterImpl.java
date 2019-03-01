package org.linyi.viva.mvp.presenter;


import com.api.changdu.proto.BookApi;
import org.linyi.base.listener.M2PListener;
import org.linyi.base.utils.NetUtil;
import org.linyi.viva.mvp.contract.Classify2Contract;
import org.linyi.viva.mvp.interactor.Classify2InteractorImpl;

public class Classify2PresenterImpl implements Classify2Contract.Presenter , M2PListener<BookApi.AckRankOption> {
    Classify2Contract.View view;
    Classify2Contract.Interactor interactor;
    public Classify2PresenterImpl(Classify2Contract.View view) {
        this.view = view;
        interactor=new Classify2InteractorImpl(this);
    }


    @Override
    public void titleList(String name) {
      view.showLoading();
        if (NetUtil.isNetworkAvailable()) {
            interactor.titleList(name);
        } else {
            interactor.loadCache(name);
        }
    }

    @Override
    public void detachView() {
        interactor.onDestroy();
        view=null;
    }


    @Override
    public void onLoadSuccess(BookApi.AckRankOption data) {
        view.hideLoading();
         if (data!=null){
             view.onLoadSuccess(data);
         }else {
             view.hideLoading();
         }
    }

    @Override
    public void onLoadFaile() {
      view.showMsg("onLoadFaile");
      view.hideLoading();
    }
}

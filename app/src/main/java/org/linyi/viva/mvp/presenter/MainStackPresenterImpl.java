package org.linyi.viva.mvp.presenter;



import com.api.changdu.proto.BookApi;

import org.linyi.base.listener.M2PListener;
import org.linyi.base.utils.NetUtil;
import org.linyi.viva.App;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.MainStackContract;
import org.linyi.viva.mvp.interactor.MainStackInteractorImpl;


public class MainStackPresenterImpl implements MainStackContract.Presenter, M2PListener<BookApi.AckDomainRecommend> {
   private MainStackContract.View view ;
    private  MainStackContract.Interactor interactor;
    public MainStackPresenterImpl(MainStackContract.View view) {
        this.view = view;
        interactor=new MainStackInteractorImpl(this);
    }

    @Override
    public void init() {
        view.showLoading();
        if (NetUtil.isNetworkAvailable()){
            interactor.init();
        }else {
            interactor.loadCache();
        }
    }


    @Override
    public void onLoadSuccess(BookApi.AckDomainRecommend data) {
        view.hideLoading();
          if (data!=null){
              view.onLoadSuccess(data);
          }else {
              view.showEmpty();
          }
    }

    @Override
    public void onLoadFaile() {
        view.hideLoading();
        view.showMsg(App.getContext().getString(R.string.load_faile));
    }
    @Override
    public void detachView() {
          if (interactor!=null){
              interactor.onDestroy();
          }

        if (view!=null){
            view=null;
        }
    }
}

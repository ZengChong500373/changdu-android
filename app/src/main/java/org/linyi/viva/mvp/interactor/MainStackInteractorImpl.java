package org.linyi.viva.mvp.interactor;

import com.api.changdu.proto.BookApi;

import org.linyi.base.http.RetrofitManager;
import org.linyi.base.listener.M2PListener;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.MainStackContract;
import org.linyi.base.http.BookHttp;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import io.reactivex.functions.Consumer;

public class MainStackInteractorImpl implements MainStackContract.Interactor {
    private  Disposable disposable,cacheDisposable;
    private M2PListener<BookApi.AckDomainRecommend> listener;
    public MainStackInteractorImpl(M2PListener<BookApi.AckDomainRecommend> listener) {
        this.listener = listener;
    }


    @Override
    public void init() {
        disposable= stackRecommend().subscribe(new Consumer<BookApi.AckDomainRecommend>() {
            @Override
            public void accept(BookApi.AckDomainRecommend ackDomainRecommend) throws Exception {
                 listener.onLoadSuccess(ackDomainRecommend);
                VariousModule.save("main_stack",ackDomainRecommend);
                if (disposable!=null){
                    disposable.dispose();
                    disposable=null;
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                         listener.onLoadFaile();
            }
        });
    }

    @Override
    public void loadCache() {
        cacheDisposable= VariousModule.getMainStackCache("main_stack").subscribe(new Consumer<BookApi.AckDomainRecommend>() {
            @Override
            public void accept(BookApi.AckDomainRecommend domainRecommend) throws Exception {
                listener.onLoadSuccess(domainRecommend);
                if (disposable!=null){
                    disposable.dispose();
                    disposable=null;
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                listener.onLoadFaile();
            }
        });
    }

    public Observable<BookApi.AckDomainRecommend> stackRecommend(){
        BookApi.ReqDomainRecommend data= BookApi.ReqDomainRecommend.newBuilder().build();
        return  RetrofitManager.getInstance().getService(BookHttp.class).stackRecommend(ModuleHelp.geneRequestBody(data))
                .compose(ModuleHelp.commTrans(BookApi.AckDomainRecommend.class));
    }

    @Override
    public void onDestroy() {
         if (disposable!=null){
             disposable.dispose();
             disposable=null;
         }
        if (cacheDisposable!=null){
            cacheDisposable.dispose();
            cacheDisposable=null;
        }
         if (listener!=null){
             listener=null;
         }
    }
}

package org.linyi.viva.mvp.interactor;



import com.api.changdu.proto.BookApi;

import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.listener.M2PListener;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.Classify2Contract;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class Classify2InteractorImpl implements Classify2Contract.Interactor {
    private  M2PListener<BookApi.AckRankOption> listener;
  private   Disposable disposable,cacheDisposable;
    public Classify2InteractorImpl(M2PListener<BookApi.AckRankOption> listener) {
        this.listener = listener;
    }
    @Override
    public void titleList(final String name) {
        disposable= titleObservable(name).subscribe(new Consumer<BookApi.AckRankOption>() {
            @Override
            public void accept(BookApi.AckRankOption list) throws Exception {
                VariousModule.save("classify2_"+name,list);
                listener.onLoadSuccess(list);
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
    public void loadCache(String name) {
        disposable= VariousModule.getClassifyTitleCache("classify2_"+name).subscribe(new Consumer<BookApi.AckRankOption>() {
            @Override
            public void accept(BookApi.AckRankOption list) throws Exception {
                listener.onLoadSuccess(list);
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

    public Observable<BookApi.AckRankOption> titleObservable(String name){
        BookApi.ReqRankOption data= BookApi.ReqRankOption.newBuilder().setCategoryName(name).build();
        return  RetrofitManager.getInstance().getService(BookHttp.class).rankOption(ModuleHelp.geneRequestBody(data))
                .compose(ModuleHelp.commTrans(BookApi.AckRankOption.class));
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

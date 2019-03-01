package org.linyi.viva.mvp.interactor;

import com.api.changdu.proto.BookApi;

import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.listener.M2PListener;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.ClassifyContract;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ClassifyInteractorImpl implements ClassifyContract.Interactor {
 private    Disposable disposable,cacheDisposable;
    private   M2PListener<BookApi.AckCategoryList> listener;
    public ClassifyInteractorImpl(M2PListener<BookApi.AckCategoryList> listener) {
        this.listener = listener;
    }
    @Override
    public void init() {
        disposable= categoryList().subscribe(new Consumer<BookApi.AckCategoryList>() {
            @Override
            public void accept(BookApi.AckCategoryList ackCategoryList) throws Exception {
                VariousModule.save("classify",ackCategoryList);
                listener.onLoadSuccess(ackCategoryList);
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
        cacheDisposable= VariousModule.getClassifyCache("classify").subscribe(new Consumer<BookApi.AckCategoryList>() {
            @Override
            public void accept(BookApi.AckCategoryList ackCategoryList) throws Exception {
                listener.onLoadSuccess(ackCategoryList);
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
    public Observable<BookApi.AckCategoryList> categoryList(){
        BookApi.ReqCategoryList data= BookApi.ReqCategoryList.newBuilder().build();
        return  RetrofitManager.getInstance().getService(BookHttp.class).categoryList(ModuleHelp.geneRequestBody(data))
                .compose(ModuleHelp.commTrans(BookApi.AckCategoryList.class));
    }
}

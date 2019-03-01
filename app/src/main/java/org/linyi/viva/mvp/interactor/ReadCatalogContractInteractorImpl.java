package org.linyi.viva.mvp.interactor;

import com.api.changdu.proto.BookApi;

import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.mvp.module.DirectoryModule;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.ReadCatalogContract;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ReadCatalogContractInteractorImpl implements ReadCatalogContract.Interactor {
    private Disposable disposable, cacheDisposable;
    private ReadCatalogContract.CallBack callBack;

    public ReadCatalogContractInteractorImpl(ReadCatalogContract.CallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public void loadData(final String id) {
        disposable = chapterIndex(id).subscribe(new Consumer<BookApi.AckChapterIndex>() {
            @Override
            public void accept(BookApi.AckChapterIndex data) throws Exception {
                DirectoryModule.save(id, data);
                callBack.onLoadSuccess(data);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callBack.onError(throwable.toString());
            }
        });
    }

    @Override
    public void loadCache(String id) {
        cacheDisposable = DirectoryModule.getChapterIndexCache(id).subscribe(new Consumer<BookApi.AckChapterIndex>() {
            @Override
            public void accept(BookApi.AckChapterIndex data) throws Exception {
                callBack.onLoadSuccess(data);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callBack.onError(throwable.toString());
            }
        });
    }

    public Observable<BookApi.AckChapterIndex> chapterIndex(String id) {
        BookApi.ReqChapterIndex data = BookApi.ReqChapterIndex.newBuilder().setBookID(id).build();
        return RetrofitManager.getInstance().getService(BookHttp.class).chapterIndex(ModuleHelp.geneRequestBody(data))
                .compose(ModuleHelp.commTrans(BookApi.AckChapterIndex.class));
    }

    @Override
    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        if (cacheDisposable != null) {
            cacheDisposable.dispose();
            cacheDisposable = null;
        }
        if (callBack != null) {
            callBack = null;
        }
    }
}

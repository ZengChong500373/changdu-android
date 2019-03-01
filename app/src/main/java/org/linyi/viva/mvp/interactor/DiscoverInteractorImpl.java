package org.linyi.viva.mvp.interactor;

import com.api.changdu.proto.BookApi;

import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.DiscoverContract;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DiscoverInteractorImpl implements DiscoverContract.Interactor {
    private Disposable disposable;

    public DiscoverInteractorImpl(DiscoverContract.CallBack<BookApi.AckBookThemeIntro> callBack) {
        this.callBack = callBack;
    }

    private DiscoverContract.CallBack<BookApi.AckBookThemeIntro> callBack;

    @Override
    public void loadData(final int page, final int type) {
        disposable = themeIntroObservable(page, type).subscribe(new Consumer<BookApi.AckBookThemeIntro>() {
            @Override
            public void accept(BookApi.AckBookThemeIntro data) throws Exception {
                if (type == 0) {
                    VariousModule.save("theme_intro",page,data);
                }
                callBack.onLoadSuccess(data,page);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (page == 1) {
                    callBack.onError("刷新失败");
                } else {
                    callBack.onError("加载更多失败");
                }
            }
        });
    }

    private Observable<BookApi.AckBookThemeIntro> themeIntroObservable(int page, int type) {
        if (type == 0) {
            BookApi.ReqBookThemeIntro data = BookApi.ReqBookThemeIntro.newBuilder().setPageSize(10).setCurrentPage(page).build();
            return RetrofitManager.getInstance().getService(BookHttp.class).themeIntro(ModuleHelp.geneRequestBody(data))
                    .compose(ModuleHelp.commTrans(BookApi.AckBookThemeIntro.class));
        } else {
            return VariousModule.getThemeIntro("theme_intro",page);
        }
    }

    @Override
    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        if (callBack != null) {
            callBack = null;
        }
    }
}

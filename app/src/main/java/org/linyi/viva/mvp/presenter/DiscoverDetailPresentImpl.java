package org.linyi.viva.mvp.presenter;




import com.api.changdu.proto.BookApi;

import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.NetUtil;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.CommonContract;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DiscoverDetailPresentImpl implements CommonContract.Presenter<Integer> {
    private Disposable disposable;

    public DiscoverDetailPresentImpl(CommonContract.View<BookApi.AckBookThemeDetail> view) {
        this.view = view;
    }

    private CommonContract.View<BookApi.AckBookThemeDetail> view;

    @Override
    public void init(final Integer  id) {
        disposable = themeDetailObservable(id).subscribe(new Consumer<BookApi.AckBookThemeDetail>() {
            @Override
            public void accept(BookApi.AckBookThemeDetail detail) throws Exception {
                if (detail != null) {
                    VariousModule.save("theme_detail_" + id, detail);
                }
                view.hideLoading();
                view.onLoadSuccess(detail);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                view.setTemp(TempView.STATUS_ERROR);
            }
        });

    }

    @Override
    public void detachView() {

    }

    private Observable<BookApi.AckBookThemeDetail> themeDetailObservable(int id) {
        int type = 0;
        if (!NetUtil.isNetworkAvailable()) {
            type = 1;
        }
        if (type == 0) {
            BookApi.ReqBookThemeDetail data = BookApi.ReqBookThemeDetail.newBuilder().setId(id).build();
            return RetrofitManager.getInstance().getService(BookHttp.class).themeDetail(ModuleHelp.geneRequestBody(data))
                    .compose(ModuleHelp.commTrans(BookApi.AckBookThemeDetail.class));
        } else {
            return VariousModule.getThemeDetail("theme_detail_" + id);
        }
    }



}

package org.linyi.viva.mvp.interactor;

import com.api.changdu.proto.BookApi;
import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.RankContract;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RankInteractorImpl implements RankContract.Interactor {
  private   Disposable disposableInit,disposableDetail;
    private   Disposable disposableCacheInit,disposableCacheDetail;
    RankContract.CallBack<BookApi.AckIndexRankingDetail> callBack;
    public RankInteractorImpl(RankContract.CallBack<BookApi.AckIndexRankingDetail> callBack) {
        this.callBack = callBack;
    }
    @Override
    public void init() {
        stopDisposable();
        disposableInit= rankList().subscribe(new Consumer<BookApi.AckIndexRankingList>() {
            @Override
            public void accept(BookApi.AckIndexRankingList data) throws Exception {
                VariousModule.save("rank",data);
                callBack.onInitSuccess(data);
                stopDisposable();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callBack.onError("error="+throwable.toString());
            }
        });
    }

    @Override
    public void loadRankDetail(final String siteName, final String rankName, final int currentPage) {
        disposableDetail=rankDetail(siteName,rankName,currentPage).subscribe(new Consumer<BookApi.AckIndexRankingDetail>() {
            @Override
            public void accept(BookApi.AckIndexRankingDetail data) throws Exception {
                VariousModule.save("rank_"+siteName+"_"+rankName,currentPage,data);
                      if (currentPage==1){
                          callBack.onLoadHeadSuccess(data);
                      }else {
                          callBack.onloadMoreSuccess(data);
                      }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callBack.onError("error="+throwable.toString());
            }
        });
    }

    @Override
    public void loadInitCache() {
        disposableCacheInit= VariousModule.getRankInitCache("rank").subscribe(new Consumer<BookApi.AckIndexRankingList>() {
            @Override
            public void accept(BookApi.AckIndexRankingList data) throws Exception {
                callBack.onInitSuccess(data);
                stopDisposable();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callBack.onError("error="+throwable.toString());
            }
        });
    }

    @Override
    public void loadRankDetailCache(final String siteName,final String rankName,final int currentPage) {
        disposableDetail=VariousModule.getRankInitDetail("rank_"+siteName+"_"+rankName,currentPage).subscribe(new Consumer<BookApi.AckIndexRankingDetail>() {
            @Override
            public void accept(BookApi.AckIndexRankingDetail data) throws Exception {
                if (currentPage==1){
                    callBack.onLoadHeadSuccess(data);
                }else {
                    callBack.onloadMoreSuccess(data);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callBack.onError("error="+throwable.toString());
            }
        });
    }

    public Observable<BookApi.AckIndexRankingList> rankList(){
        BookApi.ReqIndexRankingList data= BookApi.ReqIndexRankingList.newBuilder().build();
        return  RetrofitManager.getInstance().getService(BookHttp.class).rankList(ModuleHelp.geneRequestBody(data))
                .compose(ModuleHelp.commTrans(BookApi.AckIndexRankingList.class));
    }
    public Observable<BookApi.AckIndexRankingDetail> rankDetail(String siteName, String rankName, int currentPage){
        BookApi.ReqIndexRankingDetail data= BookApi.ReqIndexRankingDetail.newBuilder().setSiteName(siteName).setRankName(rankName).setCurrentPage(currentPage).setPageSize(10).build();
        return  RetrofitManager.getInstance().getService(BookHttp.class).rankingDetail(ModuleHelp.geneRequestBody(data))
                .compose(ModuleHelp.commTrans(BookApi.AckIndexRankingDetail.class));
    }
    public void stopDisposable(){
         if (disposableInit!=null){
             disposableInit.dispose();
             disposableInit=null;
         }
    }
    @Override
    public void onDestroy() {
        if (disposableInit!=null){
            disposableInit.dispose();
            disposableInit=null;
        }
        if (disposableDetail!=null){
            disposableDetail.dispose();
            disposableDetail=null;
        }
        if (disposableCacheInit!=null){
            disposableCacheInit.dispose();
            disposableCacheInit=null;
        }
        if (disposableCacheDetail!=null){
            disposableCacheDetail.dispose();
            disposableCacheDetail=null;
        }
        callBack=null;
    }
}

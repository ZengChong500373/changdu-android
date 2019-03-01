package org.linyi.viva.mvp.interactor;


import com.api.changdu.proto.BookApi;
import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.Classify2ListContract;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class Classify2ListInteractorImpl implements Classify2ListContract.Interactor {
  private   Disposable disposable,disposableCache;
 private    Classify2ListContract.CallBack<BookApi.AckBookList> callBack;
    public Classify2ListInteractorImpl(Classify2ListContract.CallBack<BookApi.AckBookList> callBack) {
       this.callBack=callBack;
    }

    @Override
    public void loadData(final String category, final String title, final int position, final int type) {
        disposable= bookListObservable(category,title,position,type).subscribe(new Consumer<BookApi.AckBookList>() {
            @Override
            public void accept(BookApi.AckBookList data) throws Exception {
                if (type==0){
                    VariousModule.save("classify2_list"+category+"_"+title,position,data);
                }
                if (position == 1) {
                    callBack.onLoadHeadSuccess(data);
                } else {
                    callBack.onloadMoreSuccess(data);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (position == 1) {
                    callBack.onError("刷新失败");
                } else {
                    callBack.onError("加载更多失败");
                }
            }
        });
    }

    private Observable<BookApi.AckBookList> bookListObservable(String category, String title, int position,int type){
       if (type==0){
           BookApi.ReqBookList data= BookApi.ReqBookList.newBuilder().setPageSize(10).setCurrentPage(position).setCategoryName(category).setRankOption(title).build();
           return  RetrofitManager.getInstance().getService(BookHttp.class).bookList(ModuleHelp.geneRequestBody(data))
                   .compose(ModuleHelp.commTrans(BookApi.AckBookList.class));
       }else {
           return VariousModule.getClassifyListCache("classify2_list"+category+"_"+title,position);
       }
    }
    @Override
    public void onDestroy() {
        if (disposable!=null){
            disposable.dispose();
            disposable=null;
        }
        if (disposableCache!=null){
            disposableCache.dispose();
            disposableCache=null;
        }
        if (callBack!=null){
            callBack=null;
        }
    }
}

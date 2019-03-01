package org.linyi.viva.mvp.interactor;



import com.api.changdu.proto.BookApi;

import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.mvp.module.BookshelfModule;
import org.linyi.base.mvp.module.NovelModule;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.BookDetailContract;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BookDetailInteractorImpl implements BookDetailContract.Interactor {
   private Disposable disposable,addOrRemoveDisposable;
    private Disposable disposableCache,addOrRemoveCacheDisposable;
    public BookDetailInteractorImpl(BookDetailContract.CallBack listener) {
        this.listener = listener;
    }
    BookDetailContract.CallBack listener;
    @Override
    public void init(final String id) {
        disposable= bookDetail(id).subscribe(new Consumer<BookApi.AckBookDetail>() {
            @Override
            public void accept(BookApi.AckBookDetail data) throws Exception {
                VariousModule.save("book_detail_"+id,data);
                listener.onInitSuccess(data);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
               listener.onLoadFaile();
            }
        });
    }

    @Override
    public void addOrRemoveBook(final String id, int isAdd) {
        addOrRemoveDisposable=addOrRemove(id,isAdd).subscribe(new Consumer<BookApi.AckBookShelf>() {
            @Override
            public void accept(BookApi.AckBookShelf data) throws Exception {
                VariousModule.save("book_detail_status_"+id,data);
                List<BookApi.BookOverView> list = data.getBookOverViewList();
                if (list != null && list.size() > 0) {
                    NovelModule.saveDetail(list.get(0));
                }
               if (list!=null&&list.size()>0){
                   BookshelfModule.save(list.get(0),false);
               }
                listener.onAddOrRemoveSuccess(data,true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                listener.onLoadFaile();
            }
        });
    }

    @Override
    public void loadCache(final String id) {
        disposableCache= VariousModule.getBookDetailCache("book_detail_"+id).subscribe(new Consumer<BookApi.AckBookDetail>() {
            @Override
            public void accept(BookApi.AckBookDetail data) throws Exception {
                listener.onInitSuccess(data);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                listener.onLoadFaile();
            }
        });
        addOrRemoveCacheDisposable= VariousModule.getBookDetailStatusCache("book_detail_status_"+id).subscribe(new Consumer<BookApi.AckBookShelf>() {
            @Override
            public void accept(BookApi.AckBookShelf data) throws Exception {
                listener.onAddOrRemoveSuccess(data,false);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                listener.onLoadFaile();
            }
        });
    }

    public Observable<BookApi.AckBookDetail> bookDetail(String id){
        BookApi.ReqBookDetail data= BookApi.ReqBookDetail.newBuilder().setBookID(id).build();
        return  RetrofitManager.getInstance().getService(BookHttp.class).bookDetail(ModuleHelp.geneRequestBody(data))
                .compose(ModuleHelp.commTrans(BookApi.AckBookDetail.class));
    }
    @Override
    public void onDestroy() {
        if (disposable!=null){
            disposable.dispose();
            disposable=null;
        }
        if (addOrRemoveDisposable!=null){
            addOrRemoveDisposable.dispose();
            addOrRemoveDisposable=null;
        }
        if (disposableCache!=null){
            disposableCache.dispose();
            disposableCache=null;
        }
        if (addOrRemoveCacheDisposable!=null){
            addOrRemoveCacheDisposable.dispose();
            addOrRemoveCacheDisposable=null;
        }
        if (listener!=null){
            listener=null;
        }
    }
    public Observable<BookApi.AckBookShelf> addOrRemove(String id,int isAdd){
        BookApi.ReqBookShelf reqBookShelf= BookApi.ReqBookShelf.newBuilder().setBookIds(id).setIsAdd(isAdd).build();
        return  RetrofitManager.getInstance().getService(BookHttp.class).addBookShelf(ModuleHelp.geneRequestBody(reqBookShelf))
                .compose(ModuleHelp.commTrans(BookApi.AckBookShelf.class));
    }

}

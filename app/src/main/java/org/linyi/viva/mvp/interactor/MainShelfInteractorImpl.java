package org.linyi.viva.mvp.interactor;

import com.api.changdu.proto.BookApi;

import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.mvp.module.BookshelfModule;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.MainShelfContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainShelfInteractorImpl implements MainShelfContract.Interactor {
    private MainShelfContract.CallBack callBack;
    private Disposable bookListDisposable,addOrRemoveDisposable,topDisposable;

    public MainShelfInteractorImpl(MainShelfContract.CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void removeBook(final List<String> list) {
        StringBuffer stringBuffer=new StringBuffer();
     for (int i=0;i<list.size();i++){
         stringBuffer.append(list.get(i)+",");
         if (i==list.size()-1){
             stringBuffer.append(list.get(i));
         }
     }
        addOrRemoveDisposable=addOrRemove(stringBuffer.toString(),1).subscribe(new Consumer<BookApi.AckBookShelf>() {
            @Override
            public void accept(BookApi.AckBookShelf data) throws Exception {
                for (String key: list) {
                    SharePreUtil.removeKey(key+"_LastChapterTitle");
                }
                    BookshelfModule.removeAll(list);
                   callBack.removeSuccess(data);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callBack.onError(throwable.toString());
            }
        });
    }

    @Override
    public void loadData(final int page, final int type) {
        bookListDisposable=bookShelfList(page,type).subscribe(new Consumer<BookApi.AckMyBookShelf>() {
            @Override
            public void accept(BookApi.AckMyBookShelf data) throws Exception {
               if (type==0){
                   BookshelfModule.updateAllAsyn(data);
               }
               callBack.onLoadSuccess(page,data);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                bookListDisposable.dispose();
                bookListDisposable=null;
                callBack.onError(throwable.toString());
            }
        });
    }
    @Override
    public void destroy() {
        if (bookListDisposable!=null){
            bookListDisposable.dispose();
        }
        if (addOrRemoveDisposable!=null){
            addOrRemoveDisposable.dispose();
        }
        if (topDisposable!=null){
            topDisposable.dispose();
        }
        callBack=null;
    }

    @Override
    public void topBookShelf(final String id, final int isTop) {
        topDisposable=topShelf(id,isTop).subscribe(new Consumer<BookApi.AckTopBookShelf>() {
            @Override
            public void accept(BookApi.AckTopBookShelf data) throws Exception {
                boolean top=data.getTop()==1?true:false;
                BookshelfModule.setTop(id,top);
               callBack.topSuccess(data);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                bookListDisposable.dispose();
                bookListDisposable=null;
                callBack.onError(throwable.toString());
            }
        });
    }

    public Observable<BookApi.AckMyBookShelf> bookShelfList(final int page ,int type){
        if (type==0){
            BookApi.ReqMyBookShelf data= BookApi.ReqMyBookShelf.newBuilder().setPageSize(10).setCurrentPage(page).build();
            return  RetrofitManager.getInstance().getService(BookHttp.class).myBookShelf(ModuleHelp.geneRequestBody(data))
                    .compose(ModuleHelp.commTrans(BookApi.AckMyBookShelf.class));
        }else {
         return    BookshelfModule.getBookshelfData(page-1,10);
        }

    }

    public Observable<BookApi.AckBookShelf> addOrRemove(String id, int isAdd){
        BookApi.ReqBookShelf reqBookShelf= BookApi.ReqBookShelf.newBuilder().setBookIds(id).setIsAdd(isAdd).build();
        return  RetrofitManager.getInstance().getService(BookHttp.class).addBookShelf(ModuleHelp.geneRequestBody(reqBookShelf))
                .compose(ModuleHelp.commTrans(BookApi.AckBookShelf.class));
    }
    public Observable<BookApi.AckTopBookShelf> topShelf(String id, int isTop){
        BookApi.ReqTopBookShelf reqBookShelf= BookApi.ReqTopBookShelf.newBuilder().setBookID(id).setTop(isTop).build();
        return  RetrofitManager.getInstance().getService(BookHttp.class).topBookShelf(ModuleHelp.geneRequestBody(reqBookShelf))
                .compose(ModuleHelp.commTrans(BookApi.AckTopBookShelf.class));
    }
}

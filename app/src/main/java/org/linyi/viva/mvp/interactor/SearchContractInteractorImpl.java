package org.linyi.viva.mvp.interactor;

import android.util.Log;

import com.api.changdu.proto.BookApi;

import org.linyi.base.entity.db.VariousEntity;
import org.linyi.base.http.BookHttp;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.mvp.module.VariousModule;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.mvp.contract.SearchContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SearchContractInteractorImpl implements SearchContract.Interactor {
    private Disposable disposableHot, disposableSearchList;

    public SearchContractInteractorImpl(SearchContract.CallBack callBack) {
        this.callBack = callBack;
    }

    SearchContract.CallBack callBack;

    @Override
    public void searchData(final String str, final int page, final int type) {
        stopListDispos();
        disposableSearchList = searchListObser(str, page, type).subscribe(new Consumer<BookApi.AckSearch>() {
            @Override
            public void accept(BookApi.AckSearch data) throws Exception {
                if (type == 0) {
                    VariousModule.save("seacrh_list_" + str, page, data);
                    VariousModule.saveSearchTag("seacrh_history" , str,System.currentTimeMillis());
                }
                callBack.searchSuccess(data, page);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callBack.onError("");
            }
        });
    }

    @Override
    public void searchHot(final int type) {
        stopHotDispos();
        disposableHot = HotObser(type).map(new Function<BookApi.AckHotKeyWord, List<String>>() {
            @Override
            public List<String> apply(BookApi.AckHotKeyWord data) throws Exception {
                if (type == 0) {
                    VariousModule.save("search_hot", data);
                }
                List<String> list = new ArrayList<>();
                if (data != null) {
                    for (int i = 0; i < data.getKeyWordCount(); i++) {
                        list.add(data.getKeyWord(i));
                    }
                }
                return list;
            }
        }).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                callBack.onHotSuccess(list);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callBack.onError("");
            }
        });
    }

    @Override
    public void searchHistory() {
        List<VariousEntity>  variousEntityList= VariousModule.getSearchHistoryList();
        List<String> searchList=new ArrayList<>();
        if (variousEntityList!=null&&variousEntityList.size()>0){
            for (int i=0;i<variousEntityList.size();i++){
                if (i==variousEntityList.size()-1){
                    searchList.add(variousEntityList.get(i).getDetailStr());
                    callBack.searchHistoryList(searchList);
                }else {
                    searchList.add(variousEntityList.get(i).getDetailStr());
                }
            }
        }
    }

    public Observable<BookApi.AckHotKeyWord> HotObser(int type) {
        if (type == 0) {
            BookApi.ReqHotKeyWord data = BookApi.ReqHotKeyWord.newBuilder().build();
            return RetrofitManager.getInstance().getService(BookHttp.class).searchInit(ModuleHelp.geneRequestBody(data))
                    .compose(ModuleHelp.commTrans(BookApi.AckHotKeyWord.class));
        } else {
            return VariousModule.getSearchInit("search_hot");
        }
    }

    public Observable<BookApi.AckSearch> searchListObser(String keyWord, int currentPage, int type) {
        if (type == 0) {
            BookApi.ReqSearch data = BookApi.ReqSearch.newBuilder().setKeyWord(keyWord).setCurrentPage(currentPage).setPageSize(10).build();
            return RetrofitManager.getInstance().getService(BookHttp.class).searchList(ModuleHelp.geneRequestBody(data))
                    .compose(ModuleHelp.commTrans(BookApi.AckSearch.class));
        } else {
            return VariousModule.getSearchList("seacrh_list_" + keyWord, currentPage);
        }

    }

    @Override
    public void onDestroy() {
        stopHotDispos();
        stopListDispos();
        callBack = null;
    }

    public void stopHotDispos() {
        if (disposableHot != null) {
            disposableHot.dispose();
            disposableHot = null;
        }
    }

    public void stopListDispos() {
        if (disposableSearchList != null) {
            disposableSearchList.dispose();
            disposableSearchList = null;
        }
    }
}

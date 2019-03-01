package org.linyi.viva.mvp.presenter;


import com.api.changdu.proto.BookApi;

import org.linyi.base.utils.NetUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.viva.App;
import org.linyi.viva.R;
import org.linyi.viva.entity.BookListEntity;
import org.linyi.viva.mvp.contract.RankContract;
import org.linyi.viva.mvp.interactor.RankInteractorImpl;
import org.linyi.viva.ui.adapter.RankAdapterFactory;


import java.util.HashMap;
import java.util.Map;

public class RankPresenterImpl implements RankContract.Presenter, RankContract.CallBack<BookApi.AckIndexRankingDetail> {
    private RankContract.View<BookApi.AckIndexRankingDetail> view;
    private static Map<String, BookListEntity> map = new HashMap<>();
    private RankInteractorImpl interactor;

    public RankPresenterImpl(RankContract.View<BookApi.AckIndexRankingDetail> view) {
        this.view = view;
        interactor = new RankInteractorImpl(this);
    }

    @Override
    public void init() {
        view.showLoading();
        if (NetUtil.isNetworkAvailable()) {
            interactor.init();
        } else {
            interactor.loadInitCache();
        }
    }

    @Override
    public void loadOnClick() {
        App.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                String site = view.getSiteName();
                String rank = view.getRankName();
                if (RankAdapterFactory.getAdapter(site, rank) == null) {
                    loadingHead();
                } else {
                    view.switchAdapter(site, rank);
                }
            }
        },200);

    }

    @Override
    public void loadingHead() {
        view.showRefresh();
        if (NetUtil.isNetworkAvailable()) {
            interactor.loadRankDetail(view.getSiteName(), view.getRankName(), 1);
        } else {
            interactor.loadRankDetailCache(view.getSiteName(), view.getRankName(), 1);
        }
        map.put(key(), new BookListEntity(1));
    }

    @Override
    public void loadingFoot() {
        BookListEntity entity = map.get(key());
        if (entity != null) {
            int max = entity.getMaxPage();
            if (entity.getCurrentPage() > max) {
                view.showMsg(UIUtils.getString(R.string.this_is_max_page));
                view.finishRefresh();
                return;
            }
            if (NetUtil.isNetworkAvailable()) {
                interactor.loadRankDetail(view.getSiteName(), view.getRankName(), entity.getCurrentPage() );
            } else {
                interactor.loadRankDetailCache(view.getSiteName(), view.getRankName(), entity.getCurrentPage() );
            }
        } else {
            view.showMsg(UIUtils.getString(org.linyi.base.R.string.load_error));
        }

    }

    @Override
    public void detachView() {
        interactor.onDestroy();
        map.clear();
        view = null;

    }

    @Override
    public void onLoadHeadSuccess(BookApi.AckIndexRankingDetail data) {
        view.finishRefresh();
        if (data != null) {
            view.onloadHeadSuccess(data);
            BookListEntity entity = map.get(key());
            if (entity != null) {
                entity.setMaxPage(data.getMaxPage());
                entity.setCurrentPage(entity.getCurrentPage() + 1);
            }
        } else {
            view.showMsg(UIUtils.getString(org.linyi.base.R.string.load_error));
        }

    }

    @Override
    public void onloadMoreSuccess(BookApi.AckIndexRankingDetail data) {
        view.finishRefresh();
        if (data != null) {
            view.onloadMoreSuccess(data);
            BookListEntity entity = map.get(key());
            if (entity != null) {
                entity.setMaxPage(data.getMaxPage());
                int currentPage=entity.getCurrentPage() + 1;
                entity.setCurrentPage(currentPage);
            }
        } else {
            view.showMsg(UIUtils.getString(org.linyi.base.R.string.load_error));
        }
    }

    @Override
    public void onInitSuccess(BookApi.AckIndexRankingList data) {
        view.hideLoading();
        view.onInitSuccess(data);
        if (data != null && data.getSite(0) != null) {
            BookApi.AckIndexRankingList.Site site = data.getSite(0);
            if (site.getRankList() != null && site.getRankList().size() != 0) {
                view.showRefresh();
                if (NetUtil.isNetworkAvailable()) {
                    interactor.loadRankDetail(site.getName(), site.getRank(0).getName(), 1);
                } else {
                    interactor.loadRankDetailCache(site.getName(), site.getRank(0).getName(), 1);
                }
            } else {
                view.onloadError("");
                view.showMsg("数据初始化失败");
            }

        }
    }

    @Override
    public void onError(String error) {
        view.hideLoading();
        view.finishRefresh();
    }

    public String key() {
        String key = view.getSiteName() + "," + view.getRankName();
        return key;
    }
}

package org.linyi.viva.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.api.changdu.proto.BookApi;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.linyi.base.ui.BaseActivity;
import org.linyi.ui.entity.TabEntity;
import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.ui.utils.ViewFindUtils;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.RankContract;
import org.linyi.viva.mvp.presenter.RankPresenterImpl;
import org.linyi.viva.ui.adapter.RankAdapterFactory;
import org.linyi.viva.ui.adapter.RankListAdapter;
import org.linyi.viva.ui.adapter.RankTypeAdapter;

import java.util.ArrayList;

public class RankActivity extends BaseActivity<RankPresenterImpl> implements OnRefreshListener, OnRefreshLoadMoreListener, RankContract.View<BookApi.AckIndexRankingDetail>, OnTabSelectListener {
    private Toolbar tool_bar;
    private View mDecorView;
    private CommonTabLayout tab_layout;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private SmartRefreshLayout refresh;
    public RecyclerView recycler_type, recycler_list;
    private RankTypeAdapter typeAdapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_rank;
    }

    @Override
    protected RankPresenterImpl initPresenter() {
        return new RankPresenterImpl(this);
    }

    @Override
    protected void initView() {
        tool_bar = findViewById(R.id.tool_bar);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setOnTabSelectListener(this);
        mDecorView = getWindow().getDecorView();
        /** indicator固定宽度 */
        tab_layout = ViewFindUtils.find(mDecorView, R.id.tab_layout);

        recycler_type = findViewById(R.id.recycler_type);
        recycler_type.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        typeAdapter = new RankTypeAdapter();
        recycler_type.setAdapter(typeAdapter);
        typeAdapter.setItemListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                typeAdapter.selected(postion);
                presenter.loadOnClick();
            }

            @Override
            public void onItemLongClick(View view, int postion) {

            }
        });
        refresh = mRootView.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        refresh.setOnRefreshLoadMoreListener(this);
        recycler_list = findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void initData() {
        presenter.init();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        presenter.loadingFoot();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        presenter.loadingHead();
    }


    @Override
    public void onInitSuccess(BookApi.AckIndexRankingList data) {
        for (BookApi.AckIndexRankingList.Site site :
                data.getSiteList()) {
            mTabEntities.add(new TabEntity(site.getName()));
        }
        tab_layout.setTabData(mTabEntities);
        typeAdapter.setData(data);
        typeAdapter.switchType(0);
        typeAdapter.selected(0);
    }
    private  RankListAdapter currentListAapter;
    @Override
    public void onloadHeadSuccess(BookApi.AckIndexRankingDetail data) {
        currentListAapter = RankAdapterFactory.getAdapter(data);
        currentListAapter.loadHead(data);
        recycler_list.setAdapter(currentListAapter);
    }

    @Override
    public void onloadError(String error) {

    }

    @Override
    public void onloadMoreSuccess(BookApi.AckIndexRankingDetail data) {
        RankListAdapter adapter = RankAdapterFactory.getAdapter(data);
        int lastPosition=adapter.getItemCount();
        adapter.loadMore(data);
        if (currentListAapter!=adapter){
            recycler_list.setAdapter(adapter);
            recycler_list.scrollToPosition(lastPosition-2);
        }

    }

    @Override
    public void showRefresh() {
        refresh.autoRefresh();
    }

    @Override
    public void finishRefresh() {
        refresh.finishLoadMore();
        refresh.finishRefresh();
    }

    @Override
    public String getRankName() {
        return typeAdapter.getSelectedName();
    }

    @Override
    public String getSiteName() {
        int currentPosition = tab_layout.getCurrentTab();
        return (String) tab_layout.getTitleView(currentPosition).getText();
    }

    @Override
    public void switchAdapter(String site, String rank) {
        RankListAdapter adapter = RankAdapterFactory.getAdapter(site, rank);
        recycler_list.setAdapter(adapter);
    }


    @Override
    public void onTabSelect(int position) {
        presenter.loadOnClick();
        typeAdapter.switchType(position);
    }

    @Override
    public void onTabReselect(int position) {

    }


}

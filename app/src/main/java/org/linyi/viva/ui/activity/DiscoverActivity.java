package org.linyi.viva.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.api.changdu.proto.BookApi;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.DiscoverContract;
import org.linyi.viva.mvp.presenter.DiscoverPresenterImpl;
import org.linyi.viva.ui.adapter.DiscoverAdapter;

public class DiscoverActivity extends BaseActivity<DiscoverPresenterImpl> implements OnRefreshListener, OnRefreshLoadMoreListener, DiscoverContract.View<BookApi.AckBookThemeIntro> {
    public Toolbar tool_bar;
    private TempView tempView;
    public SmartRefreshLayout refresh;
    public RecyclerView recycler_list;
    private DiscoverAdapter adapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_descover;
    }

    @Override
    protected DiscoverPresenterImpl initPresenter() {
        return new DiscoverPresenterImpl(this);
    }

    @Override
    protected void initData() {
        presenter.loadingHead();
    }

    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        refresh.setOnRefreshLoadMoreListener(this);
        tool_bar = findViewById(R.id.tool_bar);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tempView = findViewById(R.id.tempView);
        tempView.setOnRetryListener(mViewClickListener);
        adapter = new DiscoverAdapter();
        recycler_list = mRootView.findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(DiscoverActivity.this));
        recycler_list.setItemAnimator(new DefaultItemAnimator());
        recycler_list.setAdapter(adapter);

        adapter.setItemListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                BookApi.AckBookThemeIntro.Data data=adapter.getPositionData(postion);
                go2Detail(data);
            }

            @Override
            public void onItemLongClick(View view, int postion) {

            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onloadSuccess(BookApi.AckBookThemeIntro data, int page) {
        if (page == 1) {
            adapter.loadHead(data.getDataList());
        } else {
            adapter.loadMore(data.getDataList());
        }

    }

    @Override
    public void onloadError(String error) {

    }

    @Override
    public void setTemp(int type) {
        if (tempView != null)
            tempView.setType(type);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        setTemp(TempView.STATUS_DISMISS);
        refresh.finishRefresh();
        refresh.finishLoadMore();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        presenter.loadingFoot();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        presenter.loadingHead();
    }
    public void go2Detail(BookApi.AckBookThemeIntro.Data data){
        Intent intent=new Intent(DiscoverActivity.this,DiscoverDetailActivity.class);
        intent.putExtra("theme",data);
        startActivity(intent);
    }
}

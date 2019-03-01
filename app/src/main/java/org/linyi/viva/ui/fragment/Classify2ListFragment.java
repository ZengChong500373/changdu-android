package org.linyi.viva.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.api.changdu.proto.BookApi;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.utils.ToastUtils;
import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.Classify2ListContract;
import org.linyi.viva.mvp.presenter.Classify2ListPresenterImpl;
import org.linyi.viva.ui.activity.BookDetailActivity;
import org.linyi.viva.ui.adapter.Classify2ListAdapter;


import java.util.List;

public class Classify2ListFragment extends LazyFragment<Classify2ListPresenterImpl> implements Classify2ListContract.View<BookApi.BookOverView>, OnRefreshListener, OnRefreshLoadMoreListener, ItemClickListener {
    SmartRefreshLayout refresh;
    Classify2ListAdapter adapter;
    RecyclerView recycler_list;

    public static Classify2ListFragment newInstance(String category, String title) {
        Bundle args = new Bundle();
        Classify2ListFragment fragment = new Classify2ListFragment();
        args.putString("category", category);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected Classify2ListPresenterImpl initPresenter() {
        return new Classify2ListPresenterImpl(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_classify2_list;
    }

    @Override
    protected void initView() {
        refresh = mRootView.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        refresh.setOnRefreshLoadMoreListener(this);

        adapter = new Classify2ListAdapter();
        adapter.setItemListener(this);

        recycler_list = mRootView.findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_list.setItemAnimator(new DefaultItemAnimator());
        recycler_list.setAdapter(adapter);
    }

    @Override
    public void onFirstUserVisible() {
        String category = getArguments().getString("category");
        String title = getArguments().getString("title");
        presenter.init(category, title);
        presenter.loadingHead();

    }

    @Override
    public void onloadHeadSuccess(List data) {
        adapter.loadHead(data);
    }

    @Override
    public void onloadError(String error) {
        ToastUtils.showLong(error);
    }

    @Override
    public void onloadMoreSuccess(List data) {
        adapter.loadMore(data);
    }

    @Override
    public void showLoading() {
        refresh.autoRefresh();
    }

    @Override
    public void hideLoading() {
        refresh.finishLoadMore();
        refresh.finishRefresh();
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
    public void onItemClick(View view, int postion) {
        Intent intent = new Intent(getContext(), BookDetailActivity.class);
        intent.putExtra("id", adapter.getPositionData(postion).getBookID());
        getContext().startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int postion) {

    }


}

package org.linyi.viva.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.api.changdu.proto.BookApi;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.ui.flowlayout.AutoFlowLayout;
import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.SearchContract;
import org.linyi.viva.mvp.presenter.SearchContractPresentImpl;
import org.linyi.viva.ui.adapter.Classify2ListAdapter;

import java.util.List;

public class SearchActivity extends BaseActivity<SearchContractPresentImpl> implements SearchContract.View, View.OnClickListener, OnRefreshListener, OnRefreshLoadMoreListener {
    private EditText ed_search;
    private RelativeLayout rl_hot, rl_history;
    private AutoFlowLayout afl_hot, afl_history;
    private SmartRefreshLayout refresh_layout;
    private RecyclerView recycler_list;
    private Classify2ListAdapter adapter;

    private TempView tempView;
    private LayoutInflater mLayoutInflater;
    private List<String> listHot, listHistory;

    @Override
    protected int setLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchContractPresentImpl initPresenter() {
        return new SearchContractPresentImpl(this);
    }

    @Override
    protected void initView() {
        mLayoutInflater = LayoutInflater.from(this);
        tempView = findViewById(R.id.tempView);
        ed_search = findViewById(R.id.ed_search);
        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int arg1, KeyEvent keyEvent) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    String str=textView.getText().toString();
                    presenter.searchData(str);
                }
                return false;
            }
        });
        findViewById(R.id.img_delect).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_for_another).setOnClickListener(this);
        findViewById(R.id.img_history_delect).setOnClickListener(this);
        afl_hot = findViewById(R.id.afl_hot);
        afl_history = findViewById(R.id.afl_history);
        rl_hot = findViewById(R.id.rl_hot);
        rl_history = findViewById(R.id.rl_history);
        afl_hot.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (listHot != null && listHot.size() > 0) {
                    ed_search.setText(listHot.get(position));
                    presenter.searchData(listHot.get(position));
                }
            }
        });
        afl_history.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (listHistory != null && listHistory.size() > 0) {
                    ed_search.setText(listHistory.get(position));
                    presenter.searchData(listHistory.get(position));
                }
            }
        });
        refresh_layout = findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(this);
        refresh_layout.setOnRefreshLoadMoreListener(this);
        adapter = new Classify2ListAdapter();
        recycler_list = findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recycler_list.setItemAnimator(new DefaultItemAnimator());
        recycler_list.setAdapter(adapter);
        adapter.setItemListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
                intent.putExtra("id", adapter.getPositionData(postion).getBookID());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int postion) {

            }
        });
    }

    @Override
    public void showLoading() {
        setTemp(TempView.STATUS_LOADING);
    }

    @Override
    public void hideLoading() {
        setTemp(TempView.STATUS_DISMISS);
        refresh_layout.finishRefresh();
        refresh_layout.finishLoadMore();
    }

    @Override
    protected void initData() {
        presenter.init();
    }

    @Override
    public void onShowHotWords(List<String> data) {
        if (rl_hot.getVisibility() == View.GONE) {
            rl_hot.setVisibility(View.VISIBLE);
        }
        listHot = data;
        afl_hot.removeAllViews();
        for (String str: data) {
            View item = mLayoutInflater.inflate(R.layout.activity_search_flow_item, null);
            TextView tvAttrTag =   item.findViewById(R.id.tv_word);
            tvAttrTag.setText(str);
            afl_hot.setSingleLine(false);
            afl_hot.addView(item);
        }
    }

    @Override
    public void onShowHistoryWords(List<String> data) {
        if (data == null || data.size() == 0) {
            rl_history.setVisibility(View.GONE);
        } else {
            rl_history.setVisibility(View.VISIBLE);
        }
        listHistory = data;
        afl_history.removeAllViews();
        for (String str: data) {
            View item = mLayoutInflater.inflate(R.layout.activity_search_flow_item, null);
            TextView tvAttrTag =   item.findViewById(R.id.tv_word);
            tvAttrTag.setText(str);
            afl_history.setMaxLines(4);
            afl_history.setSingleLine(false);
            afl_history.addView(item);
        }

    }

    @Override
    public void searchSuccess(BookApi.AckSearch data, int page) {
        rl_hot.setVisibility(View.GONE);
        rl_history.setVisibility(View.GONE);
        refresh_layout.setVisibility(View.VISIBLE);
        if (page == 1) {
            adapter.loadHead(data.getBookOverViewList());
        } else {
            adapter.loadMore(data.getBookOverViewList());
        }
    }

    @Override
    public void setTemp(int type) {
        if (tempView != null)
            tempView.setType(type);
    }

    @Override
    public void showRefresh() {
        refresh_layout.autoRefresh();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_delect:
                ed_search.setText("");
                if (afl_hot.getChildCount() > 0) {
                    rl_hot.setVisibility(View.VISIBLE);
                }
                if (afl_history.getChildCount() > 0) {
                    rl_history.setVisibility(View.VISIBLE);
                }
                refresh_layout.setVisibility(View.GONE);
                presenter.searchHistory();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_for_another:
                presenter.forAnother();
                break;
            case R.id.img_history_delect:
                rl_history.setVisibility(View.GONE);
                afl_history.removeAllViews();
                presenter.cleanSearchHistory();
                break;

        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        presenter.loadingFoot();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        presenter.loadingHead();
    }



}

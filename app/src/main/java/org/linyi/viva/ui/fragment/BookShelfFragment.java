package org.linyi.viva.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.linyi.base.ui.LazyFragment;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.help.TurnHelp;
import org.linyi.ui.popup.CommonPopupWindow;
import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.ui.utils.CommonUtil;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.MainShelfContract;
import org.linyi.viva.mvp.presenter.MainShelfPresenterImpl;
import org.linyi.viva.ui.activity.BookDetailActivity;
import org.linyi.viva.ui.activity.SearchActivity;
import org.linyi.viva.ui.adapter.ShelFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookShelfFragment extends LazyFragment<MainShelfPresenterImpl> implements MainShelfContract.View, OnRefreshListener, OnRefreshLoadMoreListener, ItemClickListener, View.OnClickListener,CommonPopupWindow.ViewInterface {
    SmartRefreshLayout refresh_shelf;
    ShelFragmentAdapter adapter;
    RecyclerView recycler_shelf;
    LinearLayout ll_empty;
    PopupWindow mPopupWindow,mPopupWindow2;
    RelativeLayout rl_toolbar_normal, rl_toolbar_batch;
    TextView tv_all_select;
    int currentLongClickPosition=-1;
    @Override
    protected MainShelfPresenterImpl initPresenter() {
        return new MainShelfPresenterImpl(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_main_shelf;
    }

    @Override
    public void initView() {
        refresh_shelf = mRootView.findViewById(R.id.refresh_shelf);
        adapter = new ShelFragmentAdapter();
        recycler_shelf = mRootView.findViewById(R.id.recycler_shelf);
        recycler_shelf.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_shelf.setAdapter(adapter);
        refresh_shelf.setOnRefreshListener(this);
        refresh_shelf.setOnRefreshLoadMoreListener(this);
        adapter.setItemListener(this);
        adapter.setEmptyListener(new ShelFragmentAdapter.EmptyListener() {
            @Override
            public void onEmpty() {
                showEmpty();
            }
        });

        findViewById(R.id.img_welfare).setOnClickListener(this);
        findViewById(R.id.img_search).setOnClickListener(this);

        ll_empty = findViewById(R.id.ll_empty);

        findViewById(R.id.bt_go_stack).setOnClickListener(this);

        rl_toolbar_normal =findViewById(R.id.rl_toolbar_normal);
        rl_toolbar_batch =findViewById(R.id.rl_toolbar_batch);
        tv_all_select =findViewById(R.id.tv_all_select);
        tv_all_select.setOnClickListener(this);


    }

    @Override
    public void onFirstUserVisible() {
        presenter.loadHead();
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        presenter.loadHead();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        presenter.loadMore();
    }

    @Override
    public void onLoadHeadSuccess(BookApi.AckMyBookShelf data) {
        adapter.loadHead(data);
    }

    @Override
    public void onLoadMoreSuccess(BookApi.AckMyBookShelf data) {
        adapter.loadMore(data);
    }

    @Override
    public void showEmpty() {
        refresh_shelf.setVisibility(View.GONE);
        ll_empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData() {
        if (ll_empty.getVisibility() == View.VISIBLE) {
            ll_empty.setVisibility(View.GONE);
        }
        if (refresh_shelf.getVisibility() == View.GONE) {
            refresh_shelf.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void removeSuccess(BookApi.AckBookShelf data) {
          adapter.removeData(data);
    }

    @Override
    public void showRefresh() {
        refresh_shelf.autoRefresh();
    }

    @Override
    public void finishRefresh() {
        refresh_shelf.finishRefresh();
        refresh_shelf.finishLoadMore();
    }

    @Override
    public List<BookApi.AckMyBookShelf.Data> getAdapterData() {
        return adapter.getDataList();
    }

    @Override
    public void topSuccess(BookApi.AckTopBookShelf data) {
          adapter.topDataChange(data);
    }

    @Override
    public void onItemClick(View view, int postion) {
        BookApi.AckMyBookShelf.Data data = adapter.getDataOnPosition(postion);
        presenter.onClick(getContext(), data);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        presenter.onVisible();
    }

    @Override
    public void onItemLongClick(View view, int postion) {
       if (ShelFragmentAdapter.showType==ShelFragmentAdapter.commonType){
           String id=adapter.getDataOnPosition(postion).getBookOverView().getBookID();
           boolean isTop= SharePreUtil.getBoolean(id,false);
           PopupWindowShow(isTop,adapter.getDataOnPosition(postion).getIsTop());
           currentLongClickPosition=postion;
       }
    }

    private void PopupWindowShow(boolean isTop,boolean isTop2) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) return;
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_shelf, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(popupView);
        mPopupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.popup_shelf)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, popupView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .create();
        TextView textView=    mPopupWindow.getContentView().findViewById(R.id.popu_go_top);
        if (isTop||isTop2){
            textView.setText("取消置顶");
        }else {
            textView.setText("置顶");
        }
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
    private void PopupWindowShow2() {
        if (mPopupWindow2 != null && mPopupWindow2.isShowing()) return;
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_bottom_batch, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(popupView);
        mPopupWindow2 = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.popup_bottom_batch)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, popupView.getMeasuredHeight())
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .setOutsideTouchable(false)
                .create();
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        mPopupWindow2.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.img_welfare:
                TurnHelp.mission(getContext());
                break;
            case R.id.img_search:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.popu_book_detail:
                Intent intent=new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("id", getShelfData().getBookOverView().getBookID());
                getContext().startActivity(intent);
                closePop();
                break;
            case R.id.popu_go_top:
                presenter.top(getShelfData());
                closePop();
                break;
            case R.id.popu_batch_management:
                showModel(1);
                closePop();
                break;
            case R.id.popu_delete:
               List<String> list= new ArrayList<String>();
               list.add(getShelfData().getBookOverView().getBookID());
                presenter.delete(list);
                closePop();
                break;
            case R.id.popu_cancel:
               closePop();
                break;
            case R.id.bt_go_stack:
                presenter.onClick(getContext(), null);
                break;
            case R.id.tv_all_select:
                adapter.setShowType(ShelFragmentAdapter.allSelect);
                break;
            case R.id.tv_cancel:
                showModel(0);
                break;
            case R.id.tv_delete:
                presenter.delete(adapter.getIds());
                showModel(0);
                break;
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId){
            case R.layout.popup_shelf:
                view.findViewById(R.id.popu_book_detail).setOnClickListener(this);
                view.findViewById(R.id.popu_go_top).setOnClickListener(this);
                view.findViewById(R.id.popu_batch_management).setOnClickListener(this);
                view.findViewById(R.id.popu_delete).setOnClickListener(this);
                view.findViewById(R.id.popu_cancel).setOnClickListener(this);
                break;
            case R.layout.popup_bottom_batch:
                view.findViewById(R.id.tv_cancel).setOnClickListener(this);
                view.findViewById(R.id.tv_delete).setOnClickListener(this);
                break;
        }
    }
   public void closePop(){
       if (mPopupWindow != null) {
           mPopupWindow.dismiss();
       }
    }
    public BookApi.AckMyBookShelf.Data getShelfData(){
        return adapter.getDataOnPosition(currentLongClickPosition);
    }
    public void showModel(int type){
        if (type==0){
            rl_toolbar_normal.setVisibility(View.VISIBLE);
            rl_toolbar_batch.setVisibility(View.GONE);
            if (mPopupWindow2 != null) {
                mPopupWindow2.dismiss();
            }
            adapter.setShowType(ShelFragmentAdapter.commonType);
        }else {
            rl_toolbar_normal.setVisibility(View.GONE);
            rl_toolbar_batch.setVisibility(View.VISIBLE);
            PopupWindowShow2();
            adapter.setShowType(ShelFragmentAdapter.deleType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        if (rl_toolbar_batch.getVisibility()==View.GONE){
            adapter.setShowType(0);

        }

    }
}

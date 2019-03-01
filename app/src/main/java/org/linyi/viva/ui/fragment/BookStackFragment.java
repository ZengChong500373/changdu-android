package org.linyi.viva.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.api.changdu.proto.BookApi;

import org.linyi.base.ui.LazyFragment;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.TurnHelp;
import org.linyi.ui.magicindicator.MagicIndicator;
import org.linyi.ui.magicindicator.ViewPagerHelper;
import org.linyi.ui.magicindicator.buildins.circlenavigator.CircleNavigator;
import org.linyi.ui.utils.RxRecyclerViewDividerTool;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.MainStackContract;
import org.linyi.viva.mvp.presenter.MainStackPresenterImpl;
import org.linyi.viva.ui.activity.ClassifyActivity;
import org.linyi.viva.ui.activity.DiscoverActivity;
import org.linyi.viva.ui.activity.RankActivity;
import org.linyi.viva.ui.activity.SearchActivity;
import org.linyi.viva.ui.adapter.BannerAdapter;

import org.linyi.viva.ui.adapter.StackRecommendAdapter;


/**
 * 书库
 */
public class BookStackFragment extends LazyFragment<MainStackPresenterImpl> implements MainStackContract.View, View.OnClickListener {

    RelativeLayout rl_banner;
    ViewPager banner;
    MagicIndicator magic_indicator1;
    BannerAdapter adapter;
    RecyclerView recycler_boy, recycler_girl;

    StackRecommendAdapter boyAdapter, girlAdapter;
    CircleNavigator circleNavigator;
    @Override
    protected MainStackPresenterImpl initPresenter() {
        return new MainStackPresenterImpl(this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_main_stack;
    }

    @Override
    public void initView() {
        float hdw=UIUtils.getHDividew();
        rl_banner=findViewById(R.id.rl_banner);
        ViewGroup.LayoutParams params= rl_banner.getLayoutParams();
        int w=UIUtils.getWindowWidth()-UIUtils.dp2px(30);
        if (hdw>1.7&&hdw<1.8){
            params.height= (int) (w/2.60);
        }else  if (hdw>1.8){
            params.height= (int) (w/2.275);
        }else {
            params.height= (int) (w/2.75);
        }
        rl_banner.setLayoutParams(params);

        banner = findViewById(R.id.banner);
        magic_indicator1=findViewById(R.id.magic_indicator1);
        circleNavigator = new CircleNavigator(getContext());
        adapter = new BannerAdapter();
        banner.setAdapter(adapter);
        findViewById(R.id.img_welfare).setOnClickListener(this);
        findViewById(R.id.img_search).setOnClickListener(this);

        findViewById(R.id.tv_classify).setOnClickListener(this);
        findViewById(R.id.tv_ranking).setOnClickListener(this);
        findViewById(R.id.tv_find).setOnClickListener(this);


        findViewById(R.id.tv_boy_for_another).setOnClickListener(this);
        findViewById(R.id.tv_girl_for_another).setOnClickListener(this);


        recycler_boy = findViewById(R.id.recycler_boy);
        recycler_boy.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recycler_boy.addItemDecoration(new RxRecyclerViewDividerTool(UIUtils.dp2px(5f)));
        boyAdapter = new StackRecommendAdapter();
        recycler_boy.setAdapter(boyAdapter);

        recycler_girl = findViewById(R.id.recycler_girl);
        recycler_girl.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recycler_girl.addItemDecoration(new RxRecyclerViewDividerTool(UIUtils.dp2px(5f)));
        girlAdapter = new StackRecommendAdapter();
        recycler_girl.setAdapter(girlAdapter);

    }

    @Override
    public void onFirstUserVisible() {
        presenter.init();
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
            case R.id.tv_classify:
                startActivity(new Intent(getContext(), ClassifyActivity.class));
                break;
            case R.id.tv_ranking:
                startActivity(new Intent(getContext(), RankActivity.class));
                break;
            case R.id.tv_find:
                startActivity(new Intent(getContext(), DiscoverActivity.class));
                break;
            case R.id.tv_boy_for_another:
                boyAdapter.forAnother();
                break;
            case R.id.tv_girl_for_another:
                girlAdapter.forAnother();
                break;
        }
    }

    @Override
    public void onLoadSuccess(BookApi.AckDomainRecommend data) {
        adapter.setData(data.getHotList());
        circleNavigator.setCircleCount(data.getHotCount());
        circleNavigator.setCircleColor(Color.parseColor("#FF6349"));
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                banner.setCurrentItem(index);
            }
        });
        magic_indicator1.setNavigator(circleNavigator);
        ViewPagerHelper.bind(magic_indicator1, banner);
        boyAdapter.setData(data.getDataList().get(0));
        girlAdapter.setData(data.getDataList().get(1));
    }


    @Override
    public void showEmpty() {

    }


}

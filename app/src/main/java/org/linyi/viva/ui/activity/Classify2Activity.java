package org.linyi.viva.ui.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.linyi.base.ui.BaseActivity;
import org.linyi.ui.entity.TabEntity;
import org.linyi.ui.utils.ViewFindUtils;
import org.linyi.viva.R;

import org.linyi.viva.mvp.contract.Classify2Contract;
import org.linyi.viva.mvp.presenter.Classify2PresenterImpl;
import org.linyi.viva.ui.adapter.Classify2PagerAdapter;

import java.util.ArrayList;


/**
* 2级别分类
* */
public class Classify2Activity extends BaseActivity<Classify2PresenterImpl> implements Classify2Contract.View , OnTabSelectListener {
    Toolbar tool_bar;
    TextView tv_title;

    View mDecorView;
    CommonTabLayout tab_layout;
    ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    ViewPager vp_classify;
    Classify2PagerAdapter adapter;
    @Override
    protected int setLayout() {
        return R.layout.activity_classify2;
    }

    @Override
    protected Classify2PresenterImpl initPresenter() {
        return new Classify2PresenterImpl(this);
    }
    @Override
    protected void initView() {
        tool_bar=findViewById(R.id.tool_bar);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title=findViewById(R.id.tv_title);

//        tab_layout=findViewById(R.id.tab_layout);
        mDecorView = getWindow().getDecorView();
        /** indicator固定宽度 */
        tab_layout = ViewFindUtils.find(mDecorView, R.id.tab_layout);
        tab_layout.setOnTabSelectListener(this);
        vp_classify=findViewById(R.id.vp_classify);
        adapter = new Classify2PagerAdapter(getSupportFragmentManager());
        vp_classify.setAdapter(adapter);

        vp_classify.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
               tab_layout.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void initData() {
        String category= getIntent().getStringExtra("category");
        adapter.setCategory(category);
        tv_title.setText(category);
        presenter.titleList(category);
    }

    @Override
    public void onLoadSuccess(BookApi.AckRankOption data) {
          for (int i=0;i<data.getRankOptionCount();i++){
              mTabEntities.add(new TabEntity(data.getRankOption(i)));
          }
        tab_layout.setTabData(mTabEntities);
        vp_classify.setOffscreenPageLimit(data.getRankOptionCount());
        adapter.setData(data);
    }


    @Override
    public void onTabSelect(int position) {
       vp_classify.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }
}

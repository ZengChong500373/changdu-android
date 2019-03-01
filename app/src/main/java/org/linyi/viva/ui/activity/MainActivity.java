package org.linyi.viva.ui.activity;


import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.utils.UIUtils;
import org.linyi.ui.TabViewpager;
import org.linyi.viva.R;
import org.linyi.viva.ui.adapter.MainPagerAdapter;

public class MainActivity extends BaseActivity {
    private MainPagerAdapter adapter;
    public static RadioGroup rg_main_parent;
    public static TabViewpager vp_main;

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        rg_main_parent = findViewById(R.id.rg_main_parent);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        vp_main = findViewById(R.id.vp_main);
        vp_main.setAdapter(adapter);
        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 2) {
                    themeColorSetting(UIUtils.getColor(R.color.colorPrimary));
                } else {
                    themeColorSetting(Color.WHITE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    public void shelf(View view) {
        vp_main.setCurrentItem(0, false);
    }

    public void stack(View view) {
        vp_main.setCurrentItem(1, false);
    }

    public void mine(View view) {
        vp_main.setCurrentItem(2, false);
    }

}

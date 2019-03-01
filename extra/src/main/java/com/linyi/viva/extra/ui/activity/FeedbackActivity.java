package com.linyi.viva.extra.ui.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.linyi.viva.extra.R;
import com.linyi.viva.extra.ui.fragment.ContactFragment;
import com.linyi.viva.extra.ui.fragment.FAQFragment;

import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.ui.adapter.vp.SimpleVPAdapter;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/29 0029 下午 5:44
 */
public class FeedbackActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mVp;
    private SimpleVPAdapter<LazyFragment> mAdapter;
    private RadioGroup mTabContainer;

    @Override
    protected int setLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleView.setText(R.string.help_feedback);
        mVp = findViewById(R.id.vp);
        mTabContainer = findViewById(R.id.tab_container);
        mAdapter = new SimpleVPAdapter<>(getSupportFragmentManager());
        mAdapter.addFrag(FAQFragment.newInstance(FAQFragment.TYPE_FAQ));
        mAdapter.addFrag(FAQFragment.newInstance(FAQFragment.TYPE_BOOK));
        mAdapter.addFrag(new ContactFragment());
        mVp.setAdapter(mAdapter);
        mVp.addOnPageChangeListener(this);
        setOnClickListener(R.id.faq, R.id.get_book_area, R.id.contact);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        View child = mTabContainer.getChildAt(position);
        if(child instanceof RadioButton)
            ((RadioButton) child).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.faq) {
            mVp.setCurrentItem(0);
        } else if (id == R.id.get_book_area) {
            mVp.setCurrentItem(1);
        } else if (id == R.id.contact) {
            mVp.setCurrentItem(2);
        }
    }
}

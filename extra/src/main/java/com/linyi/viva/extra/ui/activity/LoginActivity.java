package com.linyi.viva.extra.ui.activity;

import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.ui.fragment.LoginFragment;
import com.linyi.viva.extra.ui.fragment.PreferenceFragment;
import com.linyi.viva.extra.ui.fragment.SexFragment;

import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.ui.adapter.vp.SimpleVPAdapter;
import org.linyi.base.utils.LogUtil;
import org.linyi.ui.CommonViewPager;

import java.util.List;

public class LoginActivity extends BaseActivity{

    private CommonViewPager mVp;
    private SimpleVPAdapter<LazyFragment> mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        mVp = findViewById(R.id.vp);
        mVp.setCanScroll(false);
        mAdapter = new SimpleVPAdapter<>(getSupportFragmentManager());
        mAdapter.addFrag(new LoginFragment());
        mAdapter.addFrag(new SexFragment());
        mAdapter.addFrag(new PreferenceFragment());
        mVp.setAdapter(mAdapter);
    }

    public void switchTab(int position) {
        LogUtil.d("mAdapter", "mAdapter = " + mAdapter);
        if(mAdapter != null && mAdapter.getCount() > position)
            mVp.setCurrentItem(position);
    }

    public void setList(List<UserApi.AckDeviceLogin.FavoriteOption> list) {
        if (mAdapter != null) {
            LazyFragment fragment = mAdapter.getFragment(2);
            if(fragment instanceof PreferenceFragment)
                ((PreferenceFragment) fragment).addData(list);
        }
    }

    public void setGender(UserApi.Gender gender) {
        if (mAdapter != null) {
            LazyFragment fragment = mAdapter.getFragment(2);
        }
    }

    @Override
    public void onBackPressed() {
        int currentItem = mVp.getCurrentItem();
        if(currentItem > 1)
            mVp.setCurrentItem(currentItem - 1);
    }
}


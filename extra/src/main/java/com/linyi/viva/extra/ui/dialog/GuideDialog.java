package com.linyi.viva.extra.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.api.changdu.proto.Api;
import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.net.module.UserModule;
import com.linyi.viva.extra.ui.fragment.GuideFragment;
import com.linyi.viva.extra.ui.fragment.PreferenceFragment;
import com.linyi.viva.extra.ui.fragment.SexFragment;

import org.linyi.base.constant.Key;
import org.linyi.base.constant.SystemConstant;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.ui.BaseDialog;
import org.linyi.base.ui.adapter.vp.SimpleVPAdapter;
import org.linyi.base.utils.CommUtils;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.TurnHelp;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/21 0021 下午 7:38
 */
public class GuideDialog extends BaseDialog implements ViewPager.OnPageChangeListener {

    private ViewPager mVp;
    private SimpleVPAdapter mAdapter;
    private int[] guideArr = new int[]{R.mipmap.guide_1, R.mipmap.guide_2};
    private PreferenceFragment mPreferenceFragment;
    private List<UserApi.AckDeviceLogin.FavoriteOption> mFavoriteOptionList;
    private ViewGroup mContainer;
    private int currPos = 0;

    public static GuideDialog newInstance(String preferenceData) {
        Bundle bundle = new Bundle();
        if(!TextUtils.isEmpty(preferenceData))
            bundle.putString(Key.STRING, preferenceData);
        GuideDialog dialog = new GuideDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_guide;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setCancelable(false);
        mVp = findViewById(R.id.vp);
        mContainer = findViewById(R.id.container);
        String preferenceData = getArguments().getString(Key.STRING);
        boolean hasPreferenceData = !TextUtils.isEmpty(preferenceData);
        FragmentManager fm = getChildFragmentManager();
        LogUtil.d(TAG, "FragmentManager = " + fm);
        mAdapter = new SimpleVPAdapter(fm);
        for (int i = 0; i < guideArr.length; i++) {
            mAdapter.addFrag(GuideFragment.newInstance(guideArr[i], i == guideArr.length - 1 && !hasPreferenceData));
        }
        initContainer();
        if (hasPreferenceData) {
            try {
                UserApi.AckDeviceLogin login = UserApi.AckDeviceLogin.parseFrom(preferenceData.getBytes(SystemConstant.CHARSET_NAME));
                if (login != null) {
                    this.mPreferenceFragment = new PreferenceFragment();
                    this.mFavoriteOptionList = login.getFavoriteOptionList();
                    SexFragment fragment = new SexFragment();
                    fragment.setSexConsumer(mSexConsumer);
                    mAdapter.addFrag(fragment);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mVp.setAdapter(mAdapter);
        mVp.addOnPageChangeListener(this);
        SharePreUtil.putInt(Key.CURR_VERSION, CommUtils.getVersionCode(getContext()));
    }

    private void initContainer() {
        for (int i = 0; i < guideArr.length; i++) {
            View view = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)UIUtils.getDimension(R.dimen.h_20), (int)UIUtils.getDimension(R.dimen.v_2));
            params.leftMargin = (int) UIUtils.getDimension(R.dimen.h_10);
            params.rightMargin = (int) UIUtils.getDimension(R.dimen.h_10);
            mContainer.addView(view, params);
            view.setBackgroundColor(UIUtils.getColor(i == 0 ? R.color.guide_go_on : R.color.white));
        }
    }

    private Consumer<UserApi.Gender> mSexConsumer = new Consumer<UserApi.Gender>() {
        @Override
        public void accept(UserApi.Gender gender) throws Exception {
            LogUtil.d(TAG, "gender = " + gender, "mFavoriteOptionList = " + mFavoriteOptionList, mPreferenceFragment, mAdapter);
            if (mPreferenceFragment != null && mFavoriteOptionList != null && mAdapter != null) {
                mPreferenceFragment.setGender(mFavoriteOptionList, gender);
                if (!mAdapter.getFragmentList().contains(mPreferenceFragment)) {
                    mAdapter.addFrag(mPreferenceFragment);
                    mAdapter.notifyDataSetChanged();
                }
                LogUtil.d(TAG, mVp.getChildCount());
                mVp.setCurrentItem(mVp.getChildCount() - 1);
            } else {
                start(gender);
            }
        }
    };

    private void start(UserApi.Gender gender) {
        UserModule.favorite(gender, null, true)
                .subscribe(new CommonObserver<Api.Response>() {
                    @Override
                    public void onNext(Api.Response response) {
                        TurnHelp.main(getContext());
                    }
                });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position < mContainer.getChildCount()) {
            mContainer.setVisibility(View.VISIBLE);
            View lastChild = mContainer.getChildAt(currPos);
            lastChild.setBackgroundColor(UIUtils.getColor(R.color.white));
            View child = mContainer.getChildAt(position);
            child.setBackgroundColor(UIUtils.getColor(R.color.guide_go_on ));
            currPos = position;
        } else {
            mContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}

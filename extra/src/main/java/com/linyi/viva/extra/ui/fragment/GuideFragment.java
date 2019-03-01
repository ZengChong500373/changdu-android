package com.linyi.viva.extra.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.linyi.viva.extra.R;

import org.linyi.base.constant.Key;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.utils.help.TurnHelp;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/21 0021 下午 7:34
 */
public class GuideFragment extends LazyFragment {

    private int resID;
    private boolean showEnterBtn;

    public static GuideFragment newInstance(int resID, boolean showEnterBtn) {
        Bundle bundle = new Bundle();
        bundle.putInt(Key.INT, resID);
        bundle.putBoolean(Key.BOOLEAN, showEnterBtn);
        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void initView() {
        resID = getArguments().getInt(Key.INT);
        showEnterBtn = getArguments().getBoolean(Key.BOOLEAN);
        ImageView img = (ImageView) findViewById(R.id.img);
        img.setImageResource(resID);
        View enterView = findViewById(R.id.enter);
        enterView.setVisibility(showEnterBtn ? View.VISIBLE : View.GONE);
        enterView.setOnClickListener(mViewClickListener);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.enter) {
            TurnHelp.main(getContext());
        }
    }

    @Override
    public void onFirstUserVisible() {

    }

}

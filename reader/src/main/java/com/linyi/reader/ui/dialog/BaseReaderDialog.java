package com.linyi.reader.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.linyi.reader.R;
import com.linyi.reader.manager.NovelManager;

import org.linyi.base.ui.BaseDialog;
import org.linyi.base.utils.UIUtils;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/15 0015 上午 10:48
 */
abstract class BaseReaderDialog extends BaseDialog{

    private TextView mLeftView;
    private TextView mTitleView;

    @Override
    protected void init(Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        //设置背景透明
        lp.dimAmount = 0f;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mLeftView = findViewById(R.id.left);
        mTitleView = findViewById(R.id.title);

        mRootView.setOnClickListener(mViewClickListener);
        mLeftView.setOnClickListener(mViewClickListener);
        if(mTitleView != null)
            mTitleView.setText(NovelManager.getInstance().getBookName());
    }

    protected void setNightMode(boolean isNightMode) {
        mLeftView.setTextColor(UIUtils.getColor(isNightMode ? R.color.night_text : R.color.day_text));
        mTitleView.setTextColor(UIUtils.getColor(isNightMode ? R.color.night_text : R.color.day_text));
        mLeftView.setCompoundDrawablesWithIntrinsicBounds(isNightMode ? R.mipmap.ic_back : R.mipmap.ic_back_white, 0, 0, 0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.left) {
            getActivity().onBackPressed();
        } else if (v == mRootView) {
            dismiss();
        }
    }
}

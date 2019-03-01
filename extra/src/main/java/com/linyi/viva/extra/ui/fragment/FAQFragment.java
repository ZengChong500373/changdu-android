package com.linyi.viva.extra.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linyi.viva.extra.R;

import org.linyi.base.constant.Key;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.utils.ToastUtils;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/29 0029 下午 6:20
 */
public class FAQFragment extends LazyFragment implements TextWatcher {

    public static final int TYPE_FAQ = 0;   //常见问题
    public static final int TYPE_BOOK = 1;  //求书区域

    private EditText mEditView;
    private View mEnterView;
    private int mType;

    public static FAQFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Key.TYPE, type);
        FAQFragment faqFragment = new FAQFragment();
        faqFragment.setArguments(bundle);
        return faqFragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_faq;
    }

    @Override
    protected void initView() {
        mType = getArguments().getInt(Key.TYPE);
        mEditView = (EditText) findViewById(R.id.edit);
        TextView titleView = (TextView) findViewById(R.id.title);
        mEditView.addTextChangedListener(this);
        mEnterView = findViewById(R.id.enter);
        mEnterView.setOnClickListener(mViewClickListener);
        titleView.setText(mType == TYPE_BOOK ? R.string.get_book : R.string.faq);
        mEditView.setHint(mType == TYPE_BOOK ? R.string.input_user_info : R.string.input_faq);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.enter && mEnterView.isEnabled()) {
            if (mType == TYPE_BOOK) {

            } else {

            }
            ToastUtils.getInstance().showShortSingle("click me");
        }
    }

    @Override
    public void onFirstUserVisible() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mEnterView.setEnabled(!TextUtils.isEmpty(s));
    }
}

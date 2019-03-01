package com.linyi.viva.extra.ui.fragment;

import android.view.View;

import com.api.changdu.proto.SystemApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.ui.weidgt.IconTextView;

import org.linyi.base.manager.AppManager;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.utils.CommUtils;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/25 0025 下午 6:04
 */
public class ContactFragment extends LazyFragment{

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_constact;
    }

    @Override
    protected void initView() {
        IconTextView contactQQ = (IconTextView) findViewById(R.id.contact_qq);
        IconTextView contactWechat = (IconTextView) findViewById(R.id.contact_wechat);
        IconTextView contactEmail = (IconTextView) findViewById(R.id.contact_email);
        SystemApi.AckSystemInit ackSystemInit = AppManager.getInstance().getAckSystemInit();
        if (ackSystemInit != null) {
            SystemApi.AckSystemInit.ContactInformation information = ackSystemInit.getContactInformation();
            if (information != null) {
                contactQQ.setRight_text(information.getQq());
                contactEmail.setRight_text(information.getEmail());
                contactWechat.setRight_text(information.getWechat());
            }
        }
        contactQQ.setOnClickListener(mViewClickListener);
        contactWechat.setOnClickListener(mViewClickListener);
        contactEmail.setOnClickListener(mViewClickListener);
    }

    @Override
    public void onFirstUserVisible() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v instanceof IconTextView) {
            CommUtils.copyText(((IconTextView)v).getRight_text());
        }
    }
}

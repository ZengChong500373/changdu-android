package com.linyi.viva.extra.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.linyi.viva.extra.R;
import com.linyi.viva.extra.manager.EventManager;

import org.linyi.base.constant.Key;
import org.linyi.base.constant.RequestCode;
import org.linyi.base.entity.event.DialogClickEvent;
import org.linyi.base.entity.params.MsgDialogEntity;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.utils.help.TurnHelp;

/**
 * @fun: 采用activity的形式展示dialog, 全局展示(比如登录框提示)
 * @developer: rwz
 * @date: 2019/1/29 0029 下午 2:09
 */
public class DialogActivity extends BaseActivity{

    private MsgDialogEntity mEntity;

    @Override
    protected int setLayout() {
        return R.layout.dialog_msg;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mEntity = bundle.getParcelable(Key.PARCELABLE_ENTITY);
        }
        TextView title = findViewById(org.linyi.base.R.id.title);
        TextView msg = findViewById(org.linyi.base.R.id.msg);
        TextView cancel = findViewById(org.linyi.base.R.id.cancel);
        TextView enter = findViewById(org.linyi.base.R.id.enter);
        if (mEntity != null) {
            boolean isSingleBtn = TextUtils.isEmpty(mEntity.getCancelText());
            cancel.setVisibility(isSingleBtn ? View.GONE : View.VISIBLE);
            cancel.setText(mEntity.getCancelText());
            enter.setText(mEntity.getEnterText());
            title.setText(mEntity.getTitle());
            msg.setText(mEntity.getMsg());
        }
        setOnClickListener(R.id.cancel, R.id.enter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == org.linyi.base.R.id.cancel) {
            onClickDialog(false);
        } else if (v.getId() == org.linyi.base.R.id.enter) {
            onClickDialog(true);
        }
        finish();
    }

    private void onClickDialog(boolean isOk) {
        if (mEntity != null) {
            if (mEntity.getRequestCode() == RequestCode.LOGIN) {
                if(isOk)
                    TurnHelp.login(this);
            } else {
                EventManager.getInstance().onClickDialogActivity(new DialogClickEvent(isOk, mEntity));
            }
        }
    }


}

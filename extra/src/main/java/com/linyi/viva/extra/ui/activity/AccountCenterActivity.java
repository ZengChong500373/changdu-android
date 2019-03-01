package com.linyi.viva.extra.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;

import org.linyi.base.manager.UserManager;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.help.TurnHelp;

/**
 * 账号中心
 */
public class AccountCenterActivity extends BaseActivity {

    @Override
    protected int setLayout() {
        return R.layout.activity_account_center;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleView.setText(R.string.account_center);
        findViewById(R.id.head_line).setVisibility(View.GONE);
        TextView mCoinView = findViewById(R.id.coin);
        UserApi.AckUserInfo user = UserManager.getInstance().getUser();
        LogUtil.d("user = " + user);
        if (user != null) {
            mCoinView.setText(user.getScore() + "");
        }
        setOnClickListener(R.id.reward_record, R.id.exchange_record, R.id.handle_task);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.handle_task) {
            TurnHelp.mission(this);
        } else if (id == R.id.exchange_record) {

        } else if (id == R.id.reward_record) {

        }
    }
}

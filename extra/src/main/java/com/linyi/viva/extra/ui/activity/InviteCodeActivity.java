package com.linyi.viva.extra.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.api.changdu.proto.MissionApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.entity.event.RedPacketEvent;
import com.linyi.viva.extra.manager.EventManager;
import com.linyi.viva.extra.net.module.MissionModule;

import org.linyi.base.inf.CommonObserver;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;

/**
 * 兑换码
 */
public class InviteCodeActivity extends BaseActivity<BasePresenter> implements TextWatcher {

    private TextView mEnterView;
    private String mInviteCode;

    @Override
    protected int setLayout() {
        return R.layout.activity_invite_code;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleView.setText(R.string.write_invite_code);
        mEnterView = findViewById(R.id.enter);
        mEnterView.setEnabled(false);
        EditText inviteCode = findViewById(R.id.invite_code);
        inviteCode.addTextChangedListener(this);
        setOnClickListener(R.id.enter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.enter && !TextUtils.isEmpty(mInviteCode)) {
            MissionModule.missionPerform(MissionApi.MissionType.REDEMPTION, mInviteCode)
                    .subscribe(new CommonObserver<MissionApi.AckMissionFinish>() {
                        @Override
                        public void onNext(MissionApi.AckMissionFinish data) {
                            int score = data.getScore();
                            MissionApi.ExtInfo extraData = data.getExtInfo();
                            if(extraData != null)
                                EventManager.getInstance().openRedPacket(
                                        new RedPacketEvent(score, extraData.getTotalScore(),data.getIsShowAd(),
                                                data.getCanDoubleScore(), data.getCode(), MissionApi.MissionType.REDEMPTION));
                            finish();
                        }
                    });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mInviteCode = s.toString();
        mEnterView.setEnabled(!TextUtils.isEmpty(mInviteCode));
    }
}

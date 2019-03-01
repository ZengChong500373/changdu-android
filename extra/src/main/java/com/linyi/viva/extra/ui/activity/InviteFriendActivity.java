package com.linyi.viva.extra.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.api.changdu.proto.MissionApi;
import com.api.changdu.proto.SystemApi;
import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.constant.Constant;
import com.linyi.viva.extra.entity.comm.ShareEntity;
import com.linyi.viva.extra.manager.EventManager;
import com.linyi.viva.extra.utils.help.TurnHelp;
import com.test001.reader.WeChatProxy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.linyi.base.VivaConstant;
import org.linyi.base.constant.Key;
import org.linyi.base.entity.event.WeChatEvent;
import org.linyi.base.manager.AppManager;
import org.linyi.base.manager.UserManager;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.utils.CommUtils;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.SnackBarHelp;

import java.io.Serializable;


/**
 * 邀请朋友
 */
public class InviteFriendActivity extends BaseActivity<BasePresenter> {

    private String myInviteName;
    private WeChatProxy mWeChatProxy;
    private View mInsidePagesContainer;

    @Override
    protected int setLayout() {
        return R.layout.activity_invite_friend;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        super.initData();
        mWeChatProxy = new WeChatProxy(this);

        TextView myInviteCode = findViewById(R.id.my_invite_code);
        TextView invitedView = findViewById(R.id.invited_friend);
        TextView receivedMarsCoin = findViewById(R.id.received_mars_coin);
        TextView receivedMarsCoinKey = findViewById(R.id.received_mars_coin_key);
        TextView rewardNum = findViewById(R.id.rewardNum);
        TextView inviteCodeDes = findViewById(R.id.invite_code_des);
        mInsidePagesContainer = findViewById(R.id.inside_pages_container);

        receivedMarsCoinKey.setText(getString(R.string.received_coin_default, getString(R.string.coin)));
        mTitleView.setText(R.string.share);
        UserApi.AckUserInfo user = UserManager.getInstance().getUser();
        if (user != null) {
            myInviteName = user.getUserID();
            myInviteCode.setText(getString(R.string.my_invite_code_default, myInviteName));
            myInviteCode.setOnClickListener(mViewClickListener);
        }
        Serializable one = getIntent().getSerializableExtra(Key.ONE);
        MissionApi.AckMissionList.Mission mission = (MissionApi.AckMissionList.Mission) one;
        Serializable two = getIntent().getSerializableExtra(Key.TWO);
        MissionApi.ExtInfo extInfo = (MissionApi.ExtInfo) two;
        if (extInfo != null) {
            invitedView.setText(String.valueOf(extInfo.getInviteNum()));
            receivedMarsCoin.setText(String.valueOf(extInfo.getInviteScore()));
        }
        if(mission != null)
            rewardNum.setText(" X " + mission.getIntroduce());
        inviteCodeDes.setText(getString(R.string.invite_code_detail_default, getString(R.string.app_name)));
        setOnClickListener(R.id.wechat, R.id.friend_circle, R.id.more);
        EventManager.getInstance().register(this);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInsidePagesContainer.getLayoutParams();
        if (params != null) {
            params.width = (int) (VivaConstant.ScreenWidth - UIUtils.getDimension(R.dimen.h_30) * 2);
            params.height = params.width * 719 / 660;
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.my_invite_code) {
            CommUtils.copyText(myInviteName);
        } else if (id == R.id.wechat) {
            share(ShareEntity.WECHAT, false);
        } else if (id == R.id.friend_circle) {
            share(ShareEntity.FRIEND_CIRCLE, false);
        } else if (id == R.id.more) {
            share(0, true);
        }
    }

    private void share(int type, boolean isMoreShare) {
        SystemApi.AckSystemInit init = AppManager.getInstance().getAckSystemInit();
        if(init == null)
            return;
        String title = getString(R.string.share_from_default, getString(R.string.app_name));
        SystemApi.AckSystemInit.Share shareApp = init.getShareApp();
        if(shareApp == null)
            return;
        String content = shareApp.getDetail();
        String url = shareApp.getUrl();
        if (isMoreShare) {
            TurnHelp.shareTextToSystem(this,url + "\n" + content);
        } else {
            mWeChatProxy.share(new ShareEntity(title, content, null, url, type));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedWechatEvent(WeChatEvent event) {
        int type = event.getType();
        int result = event.getResult();
        if (type == WeChatEvent.SHARE) {
            hideLoading();
            if (result == Constant.ShareResult.CANCEL) {
                SnackBarHelp.showSnackBar(mRootView, getString(R.string.cancel_share));
            } else if (result == Constant.ShareResult.FAIL) {
                SnackBarHelp.showSnackBar(mRootView, getString(R.string.share_fail));
            } else {
                SnackBarHelp.showSnackBar(mRootView, getString(R.string.share_success));
            }
        }
    }

}

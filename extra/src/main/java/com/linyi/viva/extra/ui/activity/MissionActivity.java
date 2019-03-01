package com.linyi.viva.extra.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.api.changdu.proto.MissionApi;
import com.api.changdu.proto.SystemApi;
import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.entity.event.ADEvent;
import com.linyi.viva.extra.entity.event.CountdownEvent;
import com.linyi.viva.extra.entity.event.RedPacketEvent;
import com.linyi.viva.extra.entity.event.SignEvent;
import com.linyi.viva.extra.manager.EventManager;
import com.linyi.viva.extra.mvp.contract.MissionContract;
import com.linyi.viva.extra.mvp.presenter.MissionPresenter;
import com.linyi.viva.extra.net.module.MissionModule;
import com.linyi.viva.extra.ui.dialog.SignDialog;
import com.linyi.viva.extra.ui.weidgt.AutoScrollView;
import com.linyi.viva.extra.ui.weidgt.CountdownTextView;
import com.linyi.viva.extra.ui.weidgt.redpacket.RedPacketDialogFragment;
import com.linyi.viva.extra.ui.weidgt.redpacket.RedPacketResp;
import com.linyi.viva.extra.utils.help.TurnHelp;
import com.rwz.adlib.ADManager;
import com.rwz.adlib.admob.AdMobManager;
import com.rwz.adlib.base.CommADListener;
import com.rwz.adlib.base.SimpleVideoADListener;
import com.rwz.adlib.base.VideoADListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.linyi.base.constant.Key;
import org.linyi.base.entity.entity.BenefitEntity;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.manager.AppManager;
import org.linyi.base.manager.UserManager;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.adapter.lv.AbsSimpleAdapter;
import org.linyi.base.ui.adapter.lv.AbsSimpleViewHolder;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.help.CheckHelp;
import org.linyi.base.utils.help.DialogHelp;

import java.util.ArrayList;
import java.util.List;

/**
 * @fun: 任务页面
 * @developer: rwz
 * @date: 2019/1/31 0031 下午 3:54
 */
public class MissionActivity extends BaseActivity<MissionContract.Presenter> implements MissionContract.View, AdapterView.OnItemClickListener {

    private ListView mList;
    private TempView mTempView;
    private List<BenefitEntity> mData;
    private AbsSimpleAdapter mAdapter;
    private View mMissionHeaderView;
    private AutoScrollView mScrollView, mScrollView2 ,mScrollView3;
    private TextView mScoreNumView;

    @Override
    protected int setLayout() {
        return R.layout.activity_mission;
    }

    @Override
    protected MissionContract.Presenter initPresenter() {
        return new MissionPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        Bundle bundle = new Bundle();
        SystemApi.AckSystemInit init = AppManager.getInstance().getAckSystemInit();
        if (init != null) {
            SystemApi.AckSystemInit.Advert advert = init.getAdvert();
            if (advert != null) {
                bundle.putString(Key.VIDEO_ID, advert.getVideoID());
                bundle.putString(Key.INTERSTITIAL_ID, advert.getPluginID());
            }
        }
        ADManager.getInstance().init(this, bundle);
        mTitleView.setText(R.string.mission_center);
        mTempView = findViewById(R.id.tempView);
        mMissionHeaderView = findViewById(R.id.mission_header);
        mScoreNumView = findViewById(R.id.score_num);
        mScrollView =  findViewById(R.id.autoScrollView);
        mScrollView2 =  findViewById(R.id.autoScrollView2);
        mScrollView3 =  findViewById(R.id.autoScrollView3);
        initScrollView(mScrollView);
        initScrollView(mScrollView2);
        initScrollView(mScrollView3);
        mList = findViewById(R.id.list);
        mData = new ArrayList<>();
        mAdapter = new AbsSimpleAdapter<BenefitEntity>(this, mData, R.layout.item_mission_countdown) {
            @Override
            public void convert(int position, AbsSimpleViewHolder helper, BenefitEntity item) {
                MissionApi.AckMissionList.Mission mission = item.getMission();
                if (mission != null) {
                    ImageView imgView = helper.getView(R.id.img);
                    ImageLoaderUtil.getInstance().loadImage(imgView, mission.getIconUrl());
                    helper.setText(R.id.title, mission.getName());
                    CountdownTextView countdownTextView = helper.getView(R.id.enter);
                    countdownTextView.setBenefitEntity(item);
                }
            }
        };
        mList.setAdapter(mAdapter);
        mTempView.setOnRetryListener(mViewClickListener);
        setTemp(TempView.STATUS_LOADING);
        presenter.requestData();
        mList.setOnItemClickListener(this);
        UserApi.AckUserInfo user = UserManager.getInstance().getUser();
        if (user != null) {
            setScore(user.getScore(), false);
        }
        EventManager.getInstance().register(this);
    }

    private void setScore(int score, boolean isSaveUser) {
        if (score >= 0) {
            if(mScoreNumView != null)
                mScoreNumView.setText("X " + String.valueOf(score));
            if(isSaveUser)
                UserManager.getInstance().saveScore(score);
        }
    }


    private void initScrollView(AutoScrollView scrollView) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(String.valueOf(i));
        }
        scrollView.setData(data);
        scrollView.setSelected(0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.reload && presenter != null) {
            setTemp(TempView.STATUS_LOADING);
            presenter.requestData();
        }
    }

    @Override
    public void setTemp(int type) {
        if(mTempView != null)
            mTempView.setType(type);
    }

    @Override
    public void onRequestCompleted(MissionApi.AckMissionList ackMissionList) {
        LogUtil.d("onRequestCompleted", "ackMissionList = " + ackMissionList);
        if (ackMissionList != null && mData != null && mAdapter != null) {
            boolean hasGame = false;
            List<MissionApi.AckMissionList.Mission> list = ackMissionList.getMissionList();
            int totalScore = -1;
            for (MissionApi.AckMissionList.Mission mission : list) {
                MissionApi.ExtInfo extInfo = ackMissionList.getExtInfo();
                if(extInfo != null)
                    totalScore = extInfo.getTotalScore();
                mData.add(new BenefitEntity(mission.getCd(), mission, extInfo));
                hasGame = hasGame || mission.getMissionType() == MissionApi.MissionType.GAME;
            }
            if(totalScore >= 0)
                setScore(totalScore, true);
            mMissionHeaderView.setVisibility(hasGame ? View.VISIBLE : View.GONE);
            mAdapter.notifyDataSetChanged();
            setTemp(TempView.STATUS_DISMISS);
        } else {
            setTemp(TempView.STATUS_NULL);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!CheckHelp.checkClickTime())
            return;
        BenefitEntity entity = mData.get(position);
        if (entity.getCountdownTime() > 0) {
            LogUtil.d("countdownTime : " + entity.getCountdownTime());
            return;
        }
        switch (entity.getType()) {
            case GAME:
                performGame();
                break;
            case VIDEO:
                loadVideo(entity);
                break;
            case INVITE: //邀请
                turnInviteFriend(entity);
                break;
            case REDEMPTION:
                TurnHelp.inviteCode(this);
                EventManager.getInstance().startCountdown(new CountdownEvent(entity.getType()));
                break;
            case CHECK_IN_DAY:
                showSignDialog(entity);
                break;
        }
    }

    private void showSignDialog(BenefitEntity entity) {
        if (entity != null && entity != null) {
            SignDialog dialog = SignDialog.newInstance(entity.getExtInfo());
            DialogHelp.show(this, dialog, "SignDialog");
            EventManager.getInstance().startCountdown(new CountdownEvent(entity.getType()));
        }
    }

    private void turnInviteFriend(BenefitEntity entity) {
        TurnHelp.inviteFriend(this, entity.getMission(), entity.getExtInfo());
        EventManager.getInstance().startCountdown(new CountdownEvent(entity.getType()));
    }

    private void loadVideo(BenefitEntity entity) {
        boolean result = ADManager.getInstance().loadVideoAD(this, videoADListener, false);
        if (!result) {
            ToastUtils.getInstance().showShortSingle(R.string.loading_video);
        }
        EventManager.getInstance().startCountdown(new CountdownEvent(entity.getType()));
    }

    private void performGame() {
        startScroll(mScrollView, AutoScrollView.ITEM_CIRCLE);
        startScroll(mScrollView2, AutoScrollView.ITEM_CIRCLE);
        startScroll(mScrollView3, AutoScrollView.ITEM_CIRCLE);
        presenter.missionPerform(MissionApi.MissionType.GAME, null);
    }

    private void startScroll(AutoScrollView scrollView, String flag) {
        scrollView.start(flag, false, (int) (Math.random() * 20 + 20));
    }

    @Override
    public void onMissionPerformCompleted(MissionApi.MissionType type, MissionApi.AckMissionFinish ackMissionFinish) {
        LogUtil.d("onMissionPerformCompleted", "ackMissionFinish = " + ackMissionFinish);
        if (ackMissionFinish == null) {
            startScroll(mScrollView, "0");
            startScroll(mScrollView2, "0");
            startScroll(mScrollView3, "0");
            return;
        } else {
            setCountdownMaxTime(MissionApi.MissionType.GAME, ackMissionFinish.getCd());
            int score = ackMissionFinish.getScore();
            String format = String.format("%03d", score > 0 ? score : 0);
            startScroll(mScrollView, format.charAt(0) + "");
            startScroll(mScrollView2, format.charAt(1) + "");
            startScroll(mScrollView3, format.charAt(2) + "");
            showRedPacketDialog(ackMissionFinish);
        }
    }

    @Override
    protected void onDestroy() {
        startScroll(mScrollView, AutoScrollView.ITEM_STOP);
        startScroll(mScrollView2, AutoScrollView.ITEM_STOP);
        startScroll(mScrollView3, AutoScrollView.ITEM_STOP);
        EventManager.getInstance().unregister(this);
        ADManager.getInstance().onDestroy(this);
        super.onDestroy();
    }

    private void setCountdownMaxTime(MissionApi.MissionType type, long maxTime) {
        LogUtil.d("setCountdownMaxTime", "type = " + type, "maxTime = " + maxTime);
        BenefitEntity benefitEntity = getBenefitEntity(type);
        if(benefitEntity != null)
            benefitEntity.setCountdownTime(maxTime);
    }

    private BenefitEntity getBenefitEntity(MissionApi.MissionType type) {
        if (mData != null) {
            for (BenefitEntity entity : mData) {
                if (type == entity.getType()) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openRedPacket(RedPacketEvent event) {
        if (event != null)
            showRedPacketDialog(event);
    }

    /**
     * 展示红包弹窗
     */
    private void showRedPacketDialog(RedPacketEvent event) {
        //红包积分需要大于0才展示
        if (event != null && event.getScore() > 0) {
            RedPacketResp bean = new RedPacketResp();
            bean.setEvent(event);
            bean.setWithAnimation(true);
            RedPacketDialogFragment mRedPacketDialogFragment = RedPacketDialogFragment.newInstance(bean);
            DialogHelp.show(this, mRedPacketDialogFragment, "RedPacketDialogFragment");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openRedPacket(SignEvent event) {
        if (event != null) {
            BenefitEntity entity = getBenefitEntity(MissionApi.MissionType.CHECK_IN_DAY);
            if (entity != null) {
                MissionApi.ExtInfo extInfo = entity.getExtInfo();
                if (extInfo != null) {
                    MissionApi.ExtInfo info = extInfo.toBuilder().setCheckInDay(event.getSignDay()).setTodayIsCheck(event.isSigned()).build();
                    entity.setExtInfo(info);
                    setScore(info.getTotalScore(), true);
                }
            }
        }
    }

    //翻倍的标识
    private String mDoubleCode;
    private MissionApi.MissionType mADMissionType;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateScore(ADEvent event) {
        if (!isAlive()) {
            return;
        }
        if (event != null && event.getAdType() == ADEvent.TYPE_INTERSTITIAL) {
            ADManager.getInstance().loadInterstitialAD(this, interstitialADListener, false);
            RedPacketEvent redPacketEvent = event.getEvent();
            if (redPacketEvent != null) {
                setScore(redPacketEvent.getTotalScore(), true);
            }
        } else if (event != null && event.getAdType() == ADEvent.TYPE_VIDEO) {
            RedPacketEvent redPacketEvent = event.getEvent();
            if (redPacketEvent != null) {
                mDoubleCode = redPacketEvent.getCode();
                mADMissionType = redPacketEvent.getType();
                boolean result = ADManager.getInstance().loadVideoAD(this, videoADListener, false);
                if (!result) {
                    ToastUtils.getInstance().showShortSingle(R.string.loading_video);
                }
            }
        }
    }

    private VideoADListener videoADListener = new SimpleVideoADListener() {
        @Override
        public void onAdClosed() {
            ADManager.getInstance().loadVideoAD(MissionActivity.this, videoADListener, true);
        }

        @Override
        public void onAdLoadedError(int errorCode, String msg) {
        }

        @Override
        public void onCompleted(String params) {
            LogUtil.d("videoADListener", "mDoubleCode = " + mDoubleCode);
            if (TextUtils.isEmpty(mDoubleCode)) {
                MissionModule.missionPerform(MissionApi.MissionType.VIDEO, null)
                        .subscribe(new CommonObserver<MissionApi.AckMissionFinish>() {
                            @Override
                            public void onNext(MissionApi.AckMissionFinish data) {
                                setCountdownMaxTime(MissionApi.MissionType.VIDEO, data.getCd());
                                showRedPacketDialog(data);
                            }
                        });
            } else {
                MissionModule.doubleScore(mDoubleCode).subscribe(new CommonObserver<MissionApi.AckMissionDoubleScore>() {
                    @Override
                    public void onNext(MissionApi.AckMissionDoubleScore data) {
                        MissionApi.ExtInfo extInfo = data.getExtInfo();
                        if (extInfo != null) {
                            RedPacketEvent event = new RedPacketEvent(data.getScore(), extInfo.getTotalScore(),
                                    data.getIsShowAd(), false, "", data.getMissionType());
                            showRedPacketDialog(event, mADMissionType);
                        }
                    }
                });
                mDoubleCode = null;
            }
        }
    };

    private void showRedPacketDialog(MissionApi.AckMissionFinish data) {
        MissionApi.ExtInfo extInfo = data.getExtInfo();
        if (extInfo != null) {
            RedPacketEvent event = new RedPacketEvent(data.getScore(), extInfo.getTotalScore(),
                    data.getIsShowAd(), data.getCanDoubleScore(), data.getCode(), data.getMissionType());
            showRedPacketDialog(event, data.getMissionType());
        }
    }

    private void showRedPacketDialog(RedPacketEvent event, MissionApi.MissionType type) {
        if (event != null && isAlive()) {
            LogUtil.d("showRedPacketDialog, score = " + event.getScore());
            //如果获取到的积分低于0(达到积分领取上限)，直接加载广告
            if (event.getScore() > 0) {
                EventManager.getInstance().startCountdown(new CountdownEvent(type));
                setScore(event.getTotalScore(), true);
                showRedPacketDialog(event);
            } else {
                EventManager.getInstance().loadAD(new ADEvent(ADEvent.TYPE_INTERSTITIAL, event));
            }
        }
    }

    private CommADListener interstitialADListener = new CommADListener() {
        @Override
        public void onAdLoader() {

        }

        @Override
        public void onAdClosed() {
            ADManager.getInstance().loadInterstitialAD(MissionActivity.this, this, true);
        }

        @Override
        public void onAdLoadedError(int errorCode, String msg) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ADManager.getInstance().onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ADManager.getInstance().onPause(this);
    }


}

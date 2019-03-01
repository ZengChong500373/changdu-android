package com.linyi.viva.extra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.api.changdu.proto.SystemApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.entity.comm.ShareEntity;
import com.linyi.viva.extra.mvp.contract.LauncherContract;
import com.linyi.viva.extra.mvp.presenter.LauncherPresenter;
import com.linyi.viva.extra.ui.weidgt.count_down_btn.JumpBt;
import com.test001.reader.WeChatProxy;

import org.linyi.base.constant.Key;
import org.linyi.base.manager.AppManager;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.CommUtils;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.help.TurnHelp;


/**
 * @fun: 启动页
 * @developer: rwz
 * @date: 2019/1/30 0030 上午 11:53
 */
public class LauncherActivity extends BaseActivity<LauncherContract.Presenter> implements LauncherContract.View {

    private JumpBt mJumpBt;
    private TempView mTempView;
    //是否展示引导页
    private boolean showGuide;

    @Override
    protected int setLayout() {
        return R.layout.activity_launcher;
    }

    @Override
    protected LauncherContract.Presenter initPresenter() {
        showGuide = checkNewVersion();
        return new LauncherPresenter(this, showGuide);
    }

    /** 跟保存的版本是否匹配 **/
    private boolean checkNewVersion() {
        return SharePreUtil.getInt(Key.CURR_VERSION) < CommUtils.getVersionCode(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mJumpBt = findViewById(R.id.jumpBt);
        mTempView = findViewById(R.id.tempView);
        mTempView.setOnRetryListener(mViewClickListener);
        if(receiveWebData(getIntent()))
            finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreateCompleted();
    }

    @Override
    public void startCountDown() {
        mJumpBt.setVisibility(View.VISIBLE);
        mJumpBt.start();
        mJumpBt.setOnClickListener(mViewClickListener);
        mJumpBt.setOnFinishListener(new JumpBt.OnFinishListener() {
            @Override
            public void onFinish() {
                launcherApp();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.jumpBt) {
            launcherApp();
        } else if (v.getId() == R.id.reload) {
            setTemp(TempView.STATUS_LOADING);
            if(presenter != null)
                presenter.loadData();
        }
    }

    private void launcherApp() {
       /* SystemApi.AckSystemInit ackSystemInit = AppManager.getInstance().getAckSystemInit();
        if(ackSystemInit == null)
            return;
        String shareUrl = ackSystemInit.getShareUrl();
        if(TextUtils.isEmpty(shareUrl))
            return;
        WeChatProxy mWeChatProxy = new WeChatProxy(this);
        ShareEntity entity = new ShareEntity("title", "des",
                "https://bookcover.yuewen.com/qdbimg/349573/1014023072", shareUrl.replace("{bookId}", "bh70puij530u84bms6t0"), ShareEntity.WECHAT);
        mWeChatProxy.share(entity);
        if(true)
            return;*/
        if(presenter != null)
            presenter.launcherApp();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setTemp(int type) {
        if(mTempView != null)
            mTempView.setType(type);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        receiveWebData(intent);
    }

    /** 接收h5打开app的请求 **/
    private boolean receiveWebData(Intent intent) {
        if (intent == null) {
            return false;
        }
        String action = intent.getAction();
        if(Intent.ACTION_VIEW.equals(action)){
            Uri uri = intent.getData();
            //changdubook://changdu?type=1&params={bookId=bh70puij530u84bms6t0}
            //uri = changdubook://changdu/bookDetail?bookId=bh72ttqj530u84bn8ong
            LogUtil.d("receiveWebData, uri = " + uri);
            if(uri != null){
                String typeStr = uri.getQueryParameter("type");
                int type = CommUtils.parseInt(typeStr, 0);
                String json = uri.getQueryParameter("params");
                TurnHelp.main(this);
                return true;
            }
        }
        LogUtil.d("receiveWebData" ,"浏览器不带数据跳转首页");
        return false;
    }



}

package com.linyi.viva.extra.ui.activity;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.api.changdu.proto.SystemApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.constant.RequestCode;
import com.linyi.viva.extra.net.module.AppModule;
import com.linyi.viva.extra.ui.weidgt.IconTextView;
import com.linyi.viva.extra.utils.CacheUtils;

import org.linyi.base.constant.Key;
import org.linyi.base.entity.params.MsgDialogEntity;
import org.linyi.base.inf.CommBiConsumer;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.dialog.MsgDialog;
import org.linyi.base.utils.CommUtils;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.help.DialogHelp;
import org.linyi.base.utils.help.SnackBarHelp;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 设置
 */
public class SetupActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private IconTextView mClearCache;

    @Override
    protected int setLayout() {
        return R.layout.activity_setup;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        IconTextView mVersionUpdateView = findViewById(R.id.version_update);
        mClearCache = findViewById(R.id.clear_cache);
        mVersionUpdateView.setRight_text("v_" + CommUtils.getVersionName(this));
        setOnClickListener(R.id.clear_cache, R.id.version_update, R.id.night_mode, R.id.login_out, R.id.switch_account);
        SwitchCompat sc = findViewById(R.id.sc);
        boolean isAutoSubscribe = SharePreUtil.getBoolean(Key.AUTO_SUBSCRIBE, false);
        sc.setChecked(isAutoSubscribe);
        sc.setOnCheckedChangeListener(this);
        mClearCache.setRight_text(CacheUtils.formatFileSize(CacheUtils.getCacheSize()));
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleView.setText(R.string.setup);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.clear_cache) {
            MsgDialog dialog = MsgDialog.newInstance(new MsgDialogEntity(getString(R.string.tips), getString(R.string.clear_cache_tips), RequestCode.SINGLE));
            dialog.setListener(mCacheListener);
            DialogHelp.show(getSupportFragmentManager(), dialog, "MsgDialog");
        } else if (id == R.id.version_update) {
            AppModule.init()
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            showLoading(getString(R.string.check_new_version));
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            hideLoading();
                        }
                    })
                    .subscribe(mObserver);
        } else if (id == R.id.night_mode) {

        } else if (id == R.id.login_out) {
            loginOut();
        } else if (id == R.id.switch_account) {

        }
    }

    private CommBiConsumer<MsgDialogEntity, Boolean> mCacheListener = new CommBiConsumer<MsgDialogEntity, Boolean>() {
        @Override
        public void accept(MsgDialogEntity entity, Boolean result) throws Exception {
            if (result) {
                CacheUtils.cleanCache();
                mClearCache.setRight_text("0.0B");
                SnackBarHelp.showSnackBar(mRootView, R.string.cleared_cache);
            }
        }
    };

    private CommonObserver<SystemApi.AckSystemInit> mObserver = new CommonObserver<SystemApi.AckSystemInit>() {
        @Override
        public void onNext(SystemApi.AckSystemInit ackSystemInit) {
            SystemApi.AckSystemInit.UpdateMessage updateMessage = ackSystemInit.getUpdateMessage();
            if (updateMessage != null) {
                String productVersion = ackSystemInit.getProductVersion();
                int verCode = CommUtils.parseInt(productVersion, 0);
                //新版本
                if (verCode > CommUtils.getVersionCode(SetupActivity.this)) {
                    // TODO: 2019/1/31 0031 新版本升级
                } else {
                    SnackBarHelp.showSnackBar(SetupActivity.this.mRootView, getString(R.string.no_new_version));
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mObserver != null) {
            mObserver.destroy();
        }
    }

    private void loginOut() {
        MsgDialogEntity entity = new MsgDialogEntity(getString(R.string.tips), getString(R.string.login_out_msg), RequestCode.SINGLE);
        MsgDialog dialog = MsgDialog.newInstance(entity);
        dialog.setListener(new CommBiConsumer<MsgDialogEntity, Boolean>() {
            @Override
            public void accept(MsgDialogEntity msgDialogEntity, Boolean result) throws Exception {
                if (result) {
                    SnackBarHelp.showSnackBar(mRootView, R.string.login_out_success);
                }
            }
        });
        DialogHelp.show(getSupportFragmentManager(), dialog, "login_out");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharePreUtil.putBoolean(Key.AUTO_SUBSCRIBE, isChecked);
    }



}

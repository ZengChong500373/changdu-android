package com.linyi.viva.extra.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.api.changdu.proto.SystemApi;
import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.constant.RequestCode;
import com.linyi.viva.extra.mvp.contract.LauncherContract;
import com.linyi.viva.extra.net.module.AppModule;
import com.linyi.viva.extra.net.module.UserModule;
import com.linyi.viva.extra.ui.dialog.GuideDialog;
import com.test001.reader.WeChatProxy;

import org.linyi.base.VivaConstant;
import org.linyi.base.entity.params.MsgDialogEntity;
import org.linyi.base.inf.CommBiConsumer;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.inf.SimpleObserver;
import org.linyi.base.manager.AppManager;
import org.linyi.base.manager.UserManager;
import org.linyi.base.ui.dialog.MsgDialog;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.PermissionUtils;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.DialogHelp;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.base.utils.help.TurnHelp;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/30 0030 下午 2:51
 */
public class LauncherPresenter implements LauncherContract.Presenter {

    private Context mContext;
    private LauncherContract.View mView;
    //偏好设置页面数据
    private String preferenceData;

    @Override
    public void detachView() {
        this.mView = null;
        this.mContext = null;
    }

    //是否已经启动app
    private boolean hasLauncherApp;
    //是否显示向导界面
    private boolean showGuide;

    public LauncherPresenter(LauncherContract.View view, boolean showGide) {
        this.mView = view;
        this.showGuide = showGide;
        this.mContext = view.getContext();
    }

    @Override
    public void onCreateCompleted() {
        requestPermission();
    }

    @Override
    public void loadData() {
        permissionCompleted();
    }

    private void requestPermission() {
        //读写文件、 获取设备信息
        PermissionUtils.requestMulPermissions((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new CommonObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean result) {
                    }

                    @Override
                    public void onComplete() {
                        permissionCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        permissionCompleted();
                    }
                });
    }

    private void permissionCompleted() {
        //必须有读写权限
        if (PermissionUtils.hasWritePermission()) {
            onWritePermissionCompleted();
        } else {
            MsgDialog dialog = MsgDialog.newInstance(new MsgDialogEntity(UIUtils.getString(R.string.tips), UIUtils.getString(R.string.write_permission), RequestCode.WRITE_PERMISSION));
            dialog.setListener(listener);
            DialogHelp.show(mContext, dialog, "MsgDialog");
        }
    }

    private CommBiConsumer<MsgDialogEntity, Boolean> listener = new CommBiConsumer<MsgDialogEntity, Boolean>() {
        @Override
        public void accept(MsgDialogEntity entity, Boolean result) throws Exception {
            if (result) {
                requestPermission();
            } else {
                onWritePermissionCompleted();
            }
        }
    };

    private void onWritePermissionCompleted() {
        //设备登录
        if (!UserManager.getInstance().isLogin()) {
            deviceLogin();
        } else {
            onRequestSuccess();
        }
    }

    private void deviceLogin() {
        UserModule.deviceLogin().subscribe(new CommonObserver<UserApi.AckDeviceLogin>() {
            @Override
            public void onNext(UserApi.AckDeviceLogin login) {
                if (login.getIsFirstLogin()) {
                    preferenceData = ModuleHelp.parseString(login);
                }
                onRequestSuccess();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LogUtil.d("", "deviceLogin, onError = " + (e == null ? "" : e.getMessage()));
                if(mView != null)
                    mView.setTemp(TempView.STATUS_ERROR);
            }
        });
    }

    private void onRequestSuccess() {
        //请求初始化信息
        requestInitData();
        //更新个人信息
        UserModule.userInfo().subscribe(new SimpleObserver<>());
    }

    private void requestInitData() {
        AppModule.init()
                .subscribe(new CommonObserver<SystemApi.AckSystemInit>() {
                    @Override
                    public void onNext(SystemApi.AckSystemInit ackSystemInit) {
                        AppManager.getInstance().setAckSystemInit(ackSystemInit);
                        onInitSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        SystemApi.AckSystemInit ackSystemInit = AppManager.getInstance().getSavedAckSystemInit();
                        if (ackSystemInit != null) {
                            AppManager.getInstance().setAckSystemInit(ackSystemInit);
                            onInitSuccess();
                        } else {
                            if(mView != null)
                                mView.setTemp(TempView.STATUS_ERROR);
                        }
                    }
                });
    }

    private void onInitSuccess() {
        if(mView != null)
            mView.setTemp(TempView.STATUS_DISMISS);
        LogUtil.d("showGuide = " + showGuide, "preferenceData = " + preferenceData);
        if (showGuide || !TextUtils.isEmpty(preferenceData)) {
            GuideDialog guideDialog = GuideDialog.newInstance(preferenceData);
            DialogHelp.show(mContext, guideDialog, "GuideDialog");
        } else {
            if(mView != null)
                mView.startCountDown();
        }
    }

    /**
     * 获取权限成功
     */
    private void showGuideDialog() {

    }

    /**
     * 启动首页
     */
    public void launcherApp() {
        LogUtil.d("————————————————    启动首页    ————————————————");
        if (!hasLauncherApp) {
            TurnHelp.main(mContext);
            hasLauncherApp = true;
        }
    }

}

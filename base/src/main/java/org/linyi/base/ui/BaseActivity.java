package org.linyi.base.ui;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import org.linyi.base.R;
import org.linyi.base.entity.params.LoadingDialogEntity;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;
import org.linyi.base.ui.dialog.LoadingDialog;
import org.linyi.base.utils.StatusBarUtil;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.CheckHelp;
import org.linyi.base.utils.help.DialogHelp;

import io.reactivex.annotations.Nullable;


/**
 * Created by zc on 2018/4/25.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    protected P presenter;
    protected TextView mTitleView;
    protected View mRootView;
    private boolean isAlive = false;      //activity是否存在
    private boolean isRunning = false;    //activity是否可见
    //加载中dialog
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAlive = true;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        themeColorSetting(Color.WHITE);//设置状态栏颜色
        mRootView = LayoutInflater.from(this).inflate(setLayout(), null, false);
        setContentView(mRootView);

        presenter = initPresenter();
        initView();
        initData();
    }

    // 布局资源ID
    protected abstract int setLayout();

    // 初始化P层
    protected abstract P initPresenter();

    protected void initView() {
        mTitleView = findViewById(R.id.title);
        setOnClickListener(R.id.left, R.id.right);
    }

    protected void initData() {
    }

    /**
     * 主题颜色设置
     * @param color
     */
    public void themeColorSetting(int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //需要修改状态栏icon颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setDarkStatusBar(false, true);
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 设置深色状态栏文字
     * @param isDarkStatus : true : 黑字;  false 白字
     */
    private boolean isFullScreen;
    private boolean isDarkStatus;
    public void setDarkStatusBar(boolean isFullScreen, boolean isDarkStatus) {
        if (this.isFullScreen != isFullScreen || this.isDarkStatus != isDarkStatus) {
            this.isFullScreen = isFullScreen;
            this.isDarkStatus = isDarkStatus;
            StatusBarUtil.setDarkStatusBar(this, isFullScreen, isDarkStatus);
        }
    }


    @Override
    public void showLoading() {
        showLoading(null);
    }

    protected void showLoading(String tipsText) {
        if(mLoadingDialog == null)
            mLoadingDialog = LoadingDialog.newInstance(new LoadingDialogEntity(tipsText));
        else
            mLoadingDialog.setTips(tipsText);
        DialogHelp.show(this, mLoadingDialog, "LoadingDialog");
    }

    @Override
    public void hideLoading() {
        if(mLoadingDialog != null && isAlive())
            mLoadingDialog.dismissAllowingStateLoss();
    }

    @Override
    public void showMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.getInstance().show(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
        isAlive = false;
        isRunning = false;
        if (presenter != null) {
            presenter.detachView();
        }
    }

    protected void setOnClickListener(@IdRes int... resId) {
        if(resId == null) return;
        for (int id : resId) {
            View view = findViewById(id);
            if(view != null)
                view.setOnClickListener(mViewClickListener);
        }
    }

    protected View.OnClickListener mViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (CheckHelp.checkClickTime()) {
                if (v.getId() == R.id.left) {
                    onClickLeft();
                } else if (v.getId() == R.id.right){
                    onClickRight();
                } else {
                    BaseActivity.this.onClick(v);
                }
            }
        }
    };

    protected void onClickLeft() {
        onBackPressed();
    }

    protected void onClickRight() {

    }

    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isRunning() {
        return isRunning;
    }



}

package org.linyi.base.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.linyi.base.entity.params.LoadingDialogEntity;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;
import org.linyi.base.ui.dialog.LoadingDialog;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.help.CheckHelp;
import org.linyi.base.utils.help.DialogHelp;


/**
 * Created by jyh on 2016/11/18.
 * <p>
 * 懒加载fragment
 *
 * isPrepared ：如果视图都可见了，那么这个视图一定准备好了！值一定为true
 */

public abstract class LazyFragment<P extends BasePresenter> extends Fragment implements BaseView {
    private Boolean isFirst = true;
    private Boolean isPrepared = false;
    public View mRootView;
    protected P presenter;
    private boolean isAlive = false;      //Fragment是否存在
    private boolean isRunning = false;    //Fragment是否可见
    //加载中dialog
    private LoadingDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (setLayout() != 0) {
            mRootView = inflater.inflate(setLayout(), container, false);
            return mRootView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        presenter=initPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isAlive = true;
        initPrepare();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirst) {
                initPrepare();
            } else if (isPrepared) {
                onUserVisible();
            }
        } else {
            onUserInVisible();
        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
            isFirst =false;
        }else {
            isPrepared=true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(presenter !=  null)
            presenter.detachView();
    }

    protected abstract P initPresenter();
    public abstract int setLayout();

    protected abstract void initView();

    public abstract void onFirstUserVisible();

    public  void onUserVisible(){};

    public  void onUserInVisible(){};


    @Override
    public void showLoading() {
        if(mLoadingDialog == null)
            mLoadingDialog = LoadingDialog.newInstance(new LoadingDialogEntity());
        DialogHelp.show(this, mLoadingDialog, "LoadingDialog");
    }

    @Override
    public void hideLoading() {
        if(mLoadingDialog != null && isAlive())
            mLoadingDialog.dismissAllowingStateLoss();
    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.showLong(msg);
    }

    protected <V extends View> V findViewById(@IdRes int id) {
        return mRootView == null ?  null : (V) (mRootView.findViewById(id));
    }

    protected void setOnClickListener(@IdRes int... resId) {
        if(resId == null || mRootView == null) return;
        for (int id : resId) {
            View view = mRootView.findViewById(id);
            if(view != null)
                view.setOnClickListener(mViewClickListener);
        }
    }

    protected View.OnClickListener mViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(CheckHelp.checkClickTime())
                LazyFragment.this.onClick(v);
        }
    };

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
        isRunning = false;
        if (presenter!=null){
            presenter.detachView();
        }
        hideLoading();
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isRunning() {
        return isRunning;
    }
}

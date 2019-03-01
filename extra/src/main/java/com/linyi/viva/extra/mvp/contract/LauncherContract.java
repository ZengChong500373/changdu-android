package com.linyi.viva.extra.mvp.contract;

import android.content.Context;

import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.BaseView;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/30 0030 下午 3:17
 */
public interface LauncherContract {

    interface Presenter extends BasePresenter{
        void launcherApp();

        void onCreateCompleted();

        void loadData();
    }

    interface View extends BaseView{

        void startCountDown();

        Context getContext();

        void setTemp(int type);
    }


}

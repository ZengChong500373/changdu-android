package com.rwz.adlib.base;

public interface CommADListener {

    /** 广告加载完成之后 **/
    void onAdLoader();

    /** 广告关闭之后 **/
    void onAdClosed();

    /** 广告加载失败 **/
    void onAdLoadedError(int errorCode, String msg);

}

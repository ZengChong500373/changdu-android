package com.rwz.adlib.base;

public interface VideoADListener extends CommADListener {

    /** 开始播放之后 **/
    void onStarted();

    /** 离开应用 **/
    void onLeftApplication();

    /** 播放完成之后 **/
    void onCompleted(String params);

}

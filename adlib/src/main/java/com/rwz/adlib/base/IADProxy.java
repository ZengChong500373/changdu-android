package com.rwz.adlib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;


public interface IADProxy {

    void init(Context context, Bundle bundle);

    /** 页面可见 **/
    void onResume(Context context);

    /** 页面不可见 **/
    void onPause(Context context);

    /** 页面销毁 **/
    void onDestroy(Context context);

    /** 加载插屏广告 **/
    void loadInterstitialAD(Context context, @Nullable CommADListener listener , boolean isOnlyLoad);

    /** 加载视频广告 **/
    boolean loadVideoAD(Context context,@Nullable VideoADListener listener, boolean isOnlyLoad);


}

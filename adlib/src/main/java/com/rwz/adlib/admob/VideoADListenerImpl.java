package com.rwz.adlib.admob;

import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.rwz.adlib.base.VideoADListener;

import org.linyi.base.utils.LogUtil;

public class VideoADListenerImpl implements RewardedVideoAdListener{

    public static final String TAG = AdMobManager.TAG + "_video";
    private VideoADListener listener;
    private RewardedVideoAd mRewardedVideoAd;

    public VideoADListenerImpl(VideoADListener listener) {
        this.listener = listener;
    }

    public void setListener(VideoADListener listener) {
        this.listener = listener;
    }

    public void setRewardedVideoAd(RewardedVideoAd mRewardedVideoAd) {
        this.mRewardedVideoAd = mRewardedVideoAd;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        LogUtil.d(TAG, "onRewardedVideoAdLoaded");
        if (mRewardedVideoAd != null && mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
            mRewardedVideoAd = null;
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        LogUtil.d(TAG, "onRewardedVideoAdOpened");
        if(listener != null)
            listener.onAdLoader();
    }

    @Override
    public void onRewardedVideoStarted() {
        LogUtil.d(TAG, "onRewardedVideoStarted");
        if(listener != null)
            listener.onStarted();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        LogUtil.d(TAG, "onRewardedVideoAdClosed");
        if(listener != null)
            listener.onAdClosed();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        if (rewardItem != null) {
            LogUtil.d(TAG, "type = " + rewardItem.getType(),"amount = " + rewardItem.getAmount());
        } else {
            LogUtil.d(TAG, "onRewarded");
        }
        if(listener != null)
            listener.onCompleted(rewardItem == null ? "" : String.valueOf(rewardItem.getAmount()));
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        LogUtil.d(TAG, "onRewardedVideoAdLeftApplication");
        if(listener != null)
            listener.onLeftApplication();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
//        AdRequest.ERROR_CODE_NO_FILL
        //ERROR_CODE_INTERNAL_ERROR 0 - 内部出现问题；例如，收到广告服务器的无效响应。
        //ERROR_CODE_INVALID_REQUEST 1 - 广告请求无效；例如，广告单元 ID 不正确。
        //ERROR_CODE_NETWORK_ERROR 2 - 由于网络连接问题，广告请求失败。
        //ERROR_CODE_NO_FILL - 3 广告请求成功，但由于缺少广告资源，未返回广告。
        LogUtil.d(TAG, "onRewardedVideoAdFailedToLoad", "error = " + i);
        if(listener != null)
            listener.onAdLoadedError(i, null);
    }

    @Override
    public void onRewardedVideoCompleted() {
        LogUtil.d(TAG, "onRewardedVideoCompleted");
    }
}

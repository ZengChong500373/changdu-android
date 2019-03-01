package com.rwz.adlib.admob;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.rwz.adlib.base.CommADListener;
import com.rwz.adlib.base.IADProxy;
import com.rwz.adlib.base.VideoADListener;

import org.linyi.base.VivaConstant;
import org.linyi.base.VivaSdk;
import org.linyi.base.utils.LogUtil;


public class AdMobManager implements IADProxy {

    public static final String TAG = "Ads";
    private static final boolean isDebug = VivaConstant.isDebug;

    private RewardedVideoAd mRewardedVideoAd;
    private VideoADListenerImpl mRewardedVideoAdListener;
    private boolean isInit;
    //视频id
    private String VIDEO_ID;
    //插屏广告id
    private String INTERSTITIAL_ID;
    private InterstitialAd mInterstitialAd;
    private InterstitialADListenerImpl mInterstitialAdListener;

    @Override
    public void init(Context context, Bundle bundle) {
        if (bundle != null) {
            VIDEO_ID = bundle.getString("VIDEO_ID");
            INTERSTITIAL_ID = bundle.getString("INTERSTITIAL_ID");
        }
        if (!isInit) {
            isInit = true;
            String appId = getApplicationMetaData(VivaSdk.getContext(), "com.google.android.gms.ads.APPLICATION_ID");
            LogUtil.d(TAG, "appId = " + appId, "isDebug = " + isDebug);
            MobileAds.initialize(context, appId);
            MobileAds.setAppMuted(true);
            //            MobileAds.openDebugMenu(context, TAG);
        }
        //初始化插屏广告
        loadInterstitial(context, null, true);
        //初始化视频广告
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        mRewardedVideoAdListener = new VideoADListenerImpl(null);
        mRewardedVideoAd.setRewardedVideoAdListener(mRewardedVideoAdListener);
        if(!mRewardedVideoAd.isLoaded())
            loadVideoAD(null);
        /*if(!mRewardedVideoAd.isLoaded())
            loadVideoAD(null);*/
    }

    private void loadInterstitial(Context context, @Nullable CommADListener listener, boolean isOnlyLoad) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAdListener = new InterstitialADListenerImpl(null, mInterstitialAd);
        mInterstitialAd.setAdListener(mInterstitialAdListener);
        String id = isDebug ? ADConstant.INTERSTITIAL_ID_DEBUG : INTERSTITIAL_ID;
        LogUtil.d(TAG, "loadInterstitialAD,id : "  + id);
        if (TextUtils.isEmpty(id)) {
            if (listener != null) {
                listener.onAdLoadedError(-1, "interstitial ad id is null");
            }
            return;
        }
        mInterstitialAdListener.setListener(listener);
        mInterstitialAdListener.setInterstitialAd(isOnlyLoad ? null : mInterstitialAd);
        mInterstitialAd.setAdUnitId(id);
        mInterstitialAd.loadAd(getAdRequest());
    }

    @Override
    public void onResume(Context context) {
        LogUtil.d(TAG, "onResume", mRewardedVideoAd, context);
        if (mRewardedVideoAd == null) {
            mRewardedVideoAd.resume(context);
        }
    }

    @Override
    public void onPause(Context context) {

    }

    @Override
    public void onDestroy(Context context) {
        mRewardedVideoAd.destroy(context);
        mRewardedVideoAd = null;
        mRewardedVideoAdListener = null;

        mInterstitialAd = null;
        mInterstitialAdListener = null;
    }

    @Override
    public void loadInterstitialAD(Context context, @Nullable CommADListener listener, boolean isOnlyLoad) {
        boolean isLoad = mInterstitialAd != null && mInterstitialAd.isLoaded();
        LogUtil.d(TAG, "loadInterstitialAD", "isLoad = " + isLoad, "isOnlyLoad = " + isOnlyLoad);
        if (isLoad) {
            mInterstitialAdListener.setListener(listener);
            if (!isOnlyLoad) {
                if(mInterstitialAdListener != null)
                    mInterstitialAdListener.setInterstitialAd(null);
                mInterstitialAd.show();
            }
        } else {
            loadInterstitial(context, listener, isOnlyLoad);
        }
    }

    @Override
    public boolean loadVideoAD(Context context, @Nullable VideoADListener listener, boolean isOnlyLoad) {
        if (mRewardedVideoAd != null) {
            boolean isLoaded = mRewardedVideoAd.isLoaded();
            LogUtil.d(TAG, "loadVideoAD", "isLoaded = " + isLoaded, "isOnlyLoad = " + isOnlyLoad);
            mRewardedVideoAdListener.setListener(listener);
            if (!isLoaded) {
                mRewardedVideoAd.setRewardedVideoAdListener(mRewardedVideoAdListener);
                if(!isOnlyLoad)
                    mRewardedVideoAdListener.setRewardedVideoAd(mRewardedVideoAd);
                loadVideoAD(listener);
            } else {
                mRewardedVideoAdListener.setRewardedVideoAd(null);
                mRewardedVideoAd.show();
                return true;
            }
        }
        return false;
    }

    private void loadVideoAD(@Nullable VideoADListener listener) {
        if(mRewardedVideoAd == null || mRewardedVideoAdListener == null)
            return;
        String id = isDebug ? ADConstant.VIDEO_ID_DEBUG : VIDEO_ID;
        LogUtil.d(TAG, "loadVideoAD, id = "  + id);
        if (TextUtils.isEmpty(id)) {
            if (listener != null) {
                listener.onAdLoadedError(-1, "video ad id is null");
            }
            return;
        }
        mRewardedVideoAd.loadAd(id, getAdRequest());
    }

    @NonNull
    private AdRequest getAdRequest() {
        return new AdRequest.Builder()
                .addTestDevice("3B7F28A5A4C2FBC3B1C3032C2495A0E1") //小米8SE
                .addTestDevice("F32998F64D428A3DBD19A433F7C61AB5") //雷神模拟器
                .build();
    }

    /**
     * 读取Application节点的meta-data数据
     */
    public static String getApplicationMetaData(Context context, String key) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA).metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}

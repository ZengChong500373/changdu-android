package com.rwz.adlib.admob;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.rwz.adlib.base.CommADListener;

import org.linyi.base.utils.LogUtil;

public class InterstitialADListenerImpl extends AdListener {

    private static final String TAG = AdMobManager.TAG + "_interstitial";
    private CommADListener listener;
    private InterstitialAd mInterstitialAd;

    public InterstitialADListenerImpl(CommADListener listener, InterstitialAd mInterstitialAd) {
        this.listener = listener;
        this.mInterstitialAd = mInterstitialAd;
    }

    public void setListener(CommADListener listener) {
        this.listener = listener;
    }

    public void setInterstitialAd(InterstitialAd mInterstitialAd) {
        this.mInterstitialAd = mInterstitialAd;
    }

    @Override
    public void onAdFailedToLoad(int i) {
        super.onAdFailedToLoad(i);
        LogUtil.d(TAG, "onAdFailedToLoad", "error = " + i);
        if(listener != null)
            listener.onAdLoadedError(i, null);
    }

    @Override
    public void onAdClosed() {
        LogUtil.d(TAG, "onAdClosed");
        super.onAdClosed();
        if(listener != null)
            listener.onAdClosed();
    }

    @Override
    public void onAdOpened() {
        LogUtil.d(TAG, "onAdLoader");
        super.onAdOpened();
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        LogUtil.d(TAG, "onAdLoaded");
        if(listener != null)
            listener.onAdLoader();
        if(mInterstitialAd != null)
            mInterstitialAd.show();
    }
}

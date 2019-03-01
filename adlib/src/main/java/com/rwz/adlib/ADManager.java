package com.rwz.adlib;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rwz.adlib.admob.AdMobManager;
import com.rwz.adlib.base.CommADListener;
import com.rwz.adlib.base.IADProxy;
import com.rwz.adlib.base.VideoADListener;


public class ADManager implements IADProxy {

    private static IADProxy instance;
    private static final boolean OPEN_AD = true;

    public static IADProxy getInstance() {
        if(instance == null)
            synchronized (ADManager.class) {
                if(instance == null)
                    instance = new AdMobManager();
            }
        return instance;
    }

    @Override
    public void init(Context context, Bundle bundle) {
        if(instance != null && OPEN_AD)
            instance.init(context, bundle);
    }

    @Override
    public void onResume(Context context) {
        if(instance != null && OPEN_AD)
            instance.onResume(context);
    }

    @Override
    public void onPause(Context context) {
        if(instance != null && OPEN_AD)
            instance.onPause(context);
    }

    @Override
    public void onDestroy(Context context) {
        if(instance != null && OPEN_AD)
            instance.onDestroy(context);
    }

    @Override
    public void loadInterstitialAD(Context context, @Nullable CommADListener listener, boolean isOnlyLoad) {
        if(instance != null && OPEN_AD)
            instance.loadInterstitialAD(context, listener, isOnlyLoad);
    }

    @Override
    public boolean loadVideoAD(Context context, @Nullable VideoADListener listener, boolean isOnlyLoad) {
        if(instance != null && OPEN_AD)
           return instance.loadVideoAD(context, listener, isOnlyLoad);
        return false;
    }
}

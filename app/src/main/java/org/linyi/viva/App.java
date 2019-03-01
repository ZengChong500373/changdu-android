package org.linyi.viva;


import android.os.Handler;

import com.linyi.reader.ReaderSDK;
import com.linyi.reader.ui.ReaderActivity;
import com.linyi.viva.extra.ui.ExtraSDK;

import org.linyi.base.BaseApp;
import org.linyi.base.utils.help.TurnHelp;
import org.linyi.viva.ui.activity.HistoryRecordActivity;
import org.linyi.viva.ui.activity.MainActivity;
import org.linyi.viva.ui.activity.ReadCatalogActivity;


public class App extends BaseApp {
    public static volatile Handler applicationHandler;
    public static Boolean getIsNeedRefreshShelf() {
        return isNeedRefreshShelf;
    }

    public static void setIsNeedRefreshShelf(Boolean isNeedRefreshShelf) {
        App.isNeedRefreshShelf = isNeedRefreshShelf;
    }

    private static Boolean isNeedRefreshShelf=false;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationHandler = new Handler(getApplicationContext().getMainLooper());
        TurnHelp.register(MainActivity.class);
        TurnHelp.register(ReadCatalogActivity.class);
        TurnHelp.register(HistoryRecordActivity.class);
        ExtraSDK.init();
        ReaderSDK.init();
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            applicationHandler.post(runnable);
        } else {
            applicationHandler.postDelayed(runnable, delay);
        }
    }
}

package org.linyi.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;

import org.linyi.viva.db.DaoMaster;
import org.linyi.viva.db.DaoSession;

import java.util.List;

public class BaseApp extends Application {
    /**
     *
     *
     * */
    private static Context mContext;
    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        VivaSdk.init(mContext);

        if (TextUtils.equals(getProcessName(this, android.os.Process.myPid()), getPackageName())) {
            initDataBase();
        }

        //查看数据库 在chrome浏览器 访问chrome://inspect/#devices 必须翻墙才可用
        Stetho.initializeWithDefaults(this);

    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    /**
     * 全局上下文
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 初始化数据库
     */
    private void initDataBase() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(mContext, "VivaNovel.db");
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        mDaoSession = master.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
}

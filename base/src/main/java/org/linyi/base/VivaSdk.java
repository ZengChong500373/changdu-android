package org.linyi.base;

import android.content.Context;

import org.linyi.base.http.NetWork;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.manager.UserManager;
import org.linyi.base.utils.CrashHandler;

public class VivaSdk {
    /**
     * 上下文对象
     * */
    private static Context mContext;
    public static void init(Context initContext){
        mContext = initContext;
        CrashHandler.getInstance().init();
        RetrofitManager.init(initContext, VivaConstant.baseUrl, null);
        //必须在RetrofitManager.init()之后调用
        UserManager.init();
    }
    /**
     * 全局上下文
     */
    public static Context getContext() {
        return mContext;
    }

}

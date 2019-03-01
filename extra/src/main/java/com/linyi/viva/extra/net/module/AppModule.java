package com.linyi.viva.extra.net.module;

import com.api.changdu.proto.SystemApi;
import com.linyi.viva.extra.net.api.AppAPI;

import org.linyi.base.http.RetrofitManager;
import org.linyi.base.utils.help.ModuleHelp;

import io.reactivex.Observable;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/30 0030 下午 3:00
 */
public class AppModule {

    private static AppAPI getService() {
        return RetrofitManager.getInstance().getService(AppAPI.class);
    }

    public static Observable<SystemApi.AckSystemInit> init() {
        return getService().init(ModuleHelp.geneRequestBody(SystemApi.ReqSystemInit.newBuilder().build()))
                .compose(ModuleHelp.commTrans(SystemApi.AckSystemInit.class));
    }

}

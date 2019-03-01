package com.linyi.viva.extra.ui;

import com.linyi.viva.extra.net.module.UserModule;
import com.linyi.viva.extra.ui.activity.DialogActivity;
import com.linyi.viva.extra.ui.activity.LoginActivity;
import com.linyi.viva.extra.ui.activity.MissionActivity;

import org.linyi.base.inf.CommonObserver;
import org.linyi.base.inf.SimpleObserver;
import org.linyi.base.manager.UserManager;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.base.utils.help.TurnHelp;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/25 0025 下午 1:42
 */
public class ExtraSDK {

    public static void init() {
        TurnHelp.register(LoginActivity.class);
        TurnHelp.register(DialogActivity.class);
        TurnHelp.register(MissionActivity.class);
        ModuleHelp.setLoginObserver(new CommonObserver() {
            @Override
            public void onNext(Object o) {
                UserModule.deviceLogin().subscribe(new SimpleObserver<>());
            }
        });
    }

}

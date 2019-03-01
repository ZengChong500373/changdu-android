package org.linyi.base.manager;

import android.text.TextUtils;

import com.api.changdu.proto.SystemApi;
import com.google.protobuf.InvalidProtocolBufferException;

import org.linyi.base.constant.Key;
import org.linyi.base.constant.SystemConstant;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.help.ModuleHelp;

import java.io.UnsupportedEncodingException;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/1 0001 下午 3:10
 */
public class AppManager {

    private static AppManager instance;

    private SystemApi.AckSystemInit ackSystemInit;

    public static AppManager getInstance() {
        if(instance == null)
            synchronized (AppManager.class) {
                if(instance == null)
                    instance = new AppManager();
            }
        return instance;
    }

    public SystemApi.AckSystemInit getAckSystemInit() {
        return ackSystemInit;
    }

    public void setAckSystemInit(SystemApi.AckSystemInit ackSystemInit) {
        if (ackSystemInit != null) {
            this.ackSystemInit = ackSystemInit;
            SharePreUtil.putString(Key.INIT_DATA, ModuleHelp.parseString(ackSystemInit));
        }
    }

    public SystemApi.AckSystemInit getSavedAckSystemInit(){
        String data = SharePreUtil.getString(Key.INIT_DATA);
        SystemApi.AckSystemInit ackSystemInit = null;
        if (!TextUtils.isEmpty(data)) {
            try {
                ackSystemInit = SystemApi.AckSystemInit.parseFrom(data.getBytes(SystemConstant.CHARSET_NAME));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return ackSystemInit;
    }



}

package com.linyi.viva.extra.net.module;

import android.os.Build;
import android.text.TextUtils;

import com.api.changdu.proto.Api;
import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.manager.EventManager;
import com.linyi.viva.extra.net.api.UserAPI;

import org.linyi.base.VivaConstant;
import org.linyi.base.VivaSdk;
import org.linyi.base.constant.Key;
import org.linyi.base.entity.event.LoginEvent;
import org.linyi.base.http.RetrofitManager;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.manager.UserManager;
import org.linyi.base.utils.CommUtils;
import org.linyi.base.utils.DeviceInfoUtil;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.ModuleHelp;

import java.util.Set;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/24 0024 上午 11:26
 */
public class UserModule {

    private static UserAPI getService() {
        return RetrofitManager.getInstance().getService(UserAPI.class);
    }

    /**
     * 设备登录
     */
    public static Observable<UserApi.AckDeviceLogin> deviceLogin() {
        UserApi.ReqDeviceLogin login = UserApi.ReqDeviceLogin.newBuilder()
                .setBrand(Build.BRAND)
                .setModal(Build.MODEL)
                .setPackageID(VivaConstant.PackageName)
                .setSystemVersion(Build.VERSION.RELEASE)
                .setChannelName(VivaConstant.ChannelName)
                .setDeviceID(DeviceInfoUtil.getInstance().getUUID(VivaSdk.getContext()))
                .build();
        return getService().deviceLogin(ModuleHelp.geneRequestBody(login))
                .compose(ModuleHelp.commTrans(UserApi.AckDeviceLogin.class))
                .doOnNext(new Consumer<UserApi.AckDeviceLogin>() {
                    @Override
                    public void accept(UserApi.AckDeviceLogin ackDeviceLogin) throws Exception {
                        UserManager.getInstance().saveToken(ackDeviceLogin.getToken());
                        EventManager.getInstance().loginSuccess(new LoginEvent());
                    }
                });
    }

    /**
     * 登录
     */
    public static Observable<UserApi.AckUserLogin> login(UserApi.LoginType type, String tel, String code) {
        UserApi.ReqUserLogin.Builder builder = UserApi.ReqUserLogin.newBuilder();
        if(!TextUtils.isEmpty(tel))
            builder.setPhoneNum(tel);
        UserApi.ReqUserLogin login = builder
                .setCode(code)
                .setLoginType(type)
                .setChannelName(VivaConstant.ChannelName)
                .setPackageName(VivaConstant.PackageName)
                .build();
        return getService().login(ModuleHelp.geneRequestBody(login))
                .compose(ModuleHelp.commTrans(UserApi.AckUserLogin.class));
    }

    /**
     * 发送验证码
     */
    public static Observable<UserApi.AckSMS> sendCode(String code) {
        UserApi.ReqSMS reqSMS = UserApi.ReqSMS.newBuilder().setPhoneNum(code).build();
        return getService().sendCode(ModuleHelp.geneRequestBody(reqSMS))
                .compose(ModuleHelp.commTrans(UserApi.AckSMS.class));
    }

    /**
     * 保存偏好设置
     */
    public static Observable<Api.Response> favorite(UserApi.Gender gender, Set<String> category, boolean isSkip) {
        UserApi.ReqFavorite.Builder builder = UserApi.ReqFavorite.newBuilder();
        if (category != null && !category.isEmpty()) {
            builder.addAllCategory(category);
        }
        if(gender != null)
            builder.setGender(gender);
        UserApi.ReqFavorite reqFavorite = builder.setIsSkip(isSkip).build();
        return getService().favorite(ModuleHelp.geneRequestBody(reqFavorite)).compose(ModuleHelp.commTrans());
    }

    /**
     * 个人资料
     */
    public static Observable<UserApi.AckUserInfo> userInfo() {
        return getService().userInfo(ModuleHelp.geneRequestBody(UserApi.ReqUserInfo.newBuilder().build()))
                .compose(ModuleHelp.commTrans(UserApi.AckUserInfo.class))
                .doOnNext(new Consumer<UserApi.AckUserInfo>() {
                    @Override
                    public void accept(UserApi.AckUserInfo ackUserInfo) throws Exception {
                        UserManager.getInstance().saveUser(ackUserInfo);
                    }
                });
    }

    /**
     * 经验等级数据
     */
    public static Observable<UserApi.AckExpInfo> expInfo() {
        return getService().expInfo(ModuleHelp.geneRequestBody(UserApi.ReqExpInfo.newBuilder().build()))
                .compose(ModuleHelp.commTrans(UserApi.AckExpInfo.class));
    }



}

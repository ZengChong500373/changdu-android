package com.linyi.viva.extra.manager;

import com.linyi.viva.extra.entity.event.ADEvent;
import com.linyi.viva.extra.entity.event.CountdownEvent;
import com.linyi.viva.extra.entity.event.RedPacketEvent;
import com.linyi.viva.extra.entity.event.SignEvent;

import org.linyi.base.entity.event.WeChatEvent;

import org.greenrobot.eventbus.EventBus;
import org.linyi.base.entity.event.DialogClickEvent;
import org.linyi.base.entity.event.LoginEvent;

/**
 * Created by rwz on 2018/8/9.
 */

public class EventManager {

    private static EventManager instance;

    public static EventManager getInstance() {
        if(instance == null)
            synchronized (EventManager.class) {
                if(instance == null)
                    instance = new EventManager();
            }
        return instance;
    }

    /** 注册 **/
    public void register(Object subscriber) {
        if(!isRegister(this))
            EventBus.getDefault().register(subscriber);
    }

    /** 是否注册 **/
    public boolean isRegister(Object subscriber) {
        return EventBus.getDefault().isRegistered(subscriber);
    }

    /** 取消注册 **/
    public void unregister(Object subscriber) {
        if(isRegister(this))
            EventBus.getDefault().unregister(subscriber);
    }

    /** 发送微信事件 **/
    public void sendWeChat(WeChatEvent event) {
        EventBus.getDefault().post(event);
    }

    /** 发送登录成功事件 **/
    public void loginSuccess(LoginEvent event) {
        EventBus.getDefault().post(event);
    }

    /** dialog 点击事件 **/
    public void onClickDialogActivity(DialogClickEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 开始倒计时动画
     * @param event
     */
    public void startCountdown(CountdownEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 打开红包
     * @param event
     */
    public void openRedPacket(RedPacketEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 签到
     */
    public void sign(SignEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 加载广告
     * @param event
     */
    public void loadAD(ADEvent event) {
        EventBus.getDefault().post(event);
    }


}

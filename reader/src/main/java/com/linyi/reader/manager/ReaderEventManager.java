package com.linyi.reader.manager;

import com.linyi.reader.entity.ReaderConfig;
import com.linyi.reader.entity.event.LoadChapterEvent;
import com.linyi.reader.entity.event.LoadChapterSuccessEvent;
import com.linyi.reader.entity.event.ReaderConfigEvent;

import org.greenrobot.eventbus.EventBus;
import org.linyi.base.entity.event.ActivityResultEvent;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/15 0015 上午 11:41
 */
public class ReaderEventManager {

    private static ReaderEventManager instance;

    public static ReaderEventManager getInstance() {
        if(instance == null)
            synchronized (ReaderEventManager.class) {
                if(instance == null)
                    instance = new ReaderEventManager();
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

    /**
     * activity回调事件
     */
    public void onActivityResult(ActivityResultEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 更新配置文件
     */
    public void onReaderConfig(ReaderConfigEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 加载章节数据成功
     */
    public void loadChapter(LoadChapterEvent event) {
        EventBus.getDefault().post(event);
    }

}

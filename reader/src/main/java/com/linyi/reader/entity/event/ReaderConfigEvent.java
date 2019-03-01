package com.linyi.reader.entity.event;

import com.linyi.reader.entity.ReaderConfig;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/15 0015 下午 12:00
 */
public class ReaderConfigEvent {

    private ReaderConfig config;
    //是否需要重新计算参数
    private boolean isReCalcParams;

    public ReaderConfigEvent(ReaderConfig config, boolean isReCalcParams) {
        this.config = config;
        this.isReCalcParams = isReCalcParams;
    }

    public ReaderConfig getConfig() {
        return config;
    }

    public boolean isReCalcParams() {
        return isReCalcParams;
    }
}

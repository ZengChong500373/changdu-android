package com.linyi.viva.extra.entity.event;

public class ADEvent {

    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_INTERSTITIAL = 2;

    private int adType;
    private RedPacketEvent event;

    public ADEvent(int adType, RedPacketEvent event) {
        this.adType = adType;
        this.event = event;
    }

    public RedPacketEvent getEvent() {
        return event;
    }

    public int getAdType() {
        return adType;
    }

}

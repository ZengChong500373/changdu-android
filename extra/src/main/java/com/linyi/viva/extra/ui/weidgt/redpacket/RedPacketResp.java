package com.linyi.viva.extra.ui.weidgt.redpacket;

import android.os.Parcel;
import android.os.Parcelable;

import com.linyi.viva.extra.entity.event.RedPacketEvent;


/**
 * Description:
 */

public class RedPacketResp implements Parcelable {

    private boolean withAnimation;
    private RedPacketEvent event;

    public RedPacketEvent getEvent() {
        return event;
    }

    public void setEvent(RedPacketEvent event) {
        this.event = event;
    }

    public void setWithAnimation(boolean withAnimation) {
        this.withAnimation = withAnimation;
    }

    public boolean isWithAnimation() {
        return withAnimation;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.withAnimation ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.event, flags);
    }

    public RedPacketResp() {
    }

    protected RedPacketResp(Parcel in) {
        this.withAnimation = in.readByte() != 0;
        this.event = in.readParcelable(RedPacketEvent.class.getClassLoader());
    }

    public static final Creator<RedPacketResp> CREATOR = new Creator<RedPacketResp>() {
        @Override
        public RedPacketResp createFromParcel(Parcel source) {
            return new RedPacketResp(source);
        }

        @Override
        public RedPacketResp[] newArray(int size) {
            return new RedPacketResp[size];
        }
    };
}

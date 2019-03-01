package com.linyi.viva.extra.entity.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.api.changdu.proto.MissionApi;


public class RedPacketEvent implements Parcelable {

    private int score;
    private int totalScore;
    private boolean isShowAD;
    private boolean canDoubleScore;
    private  String code;
    private MissionApi.MissionType type;

    public RedPacketEvent(int score, int totalScore, boolean isShowAD, boolean canDoubleScore, String code, MissionApi.MissionType type) {
        this.score = score;
        this.totalScore = totalScore;
        this.isShowAD = isShowAD;
        this.canDoubleScore = canDoubleScore;
        this.code = code;
        this.type = type;
    }

    public boolean isCanDoubleScore() {
        return canDoubleScore;
    }

    public String getCode() {
        return code;
    }

    public MissionApi.MissionType getType() {
        return type;
    }

    public boolean isShowAD() {
        return isShowAD;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.score);
        dest.writeInt(this.totalScore);
        dest.writeByte(this.isShowAD ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canDoubleScore ? (byte) 1 : (byte) 0);
        dest.writeString(this.code);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }

    protected RedPacketEvent(Parcel in) {
        this.score = in.readInt();
        this.totalScore = in.readInt();
        this.isShowAD = in.readByte() != 0;
        this.canDoubleScore = in.readByte() != 0;
        this.code = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : MissionApi.MissionType.values()[tmpType];
    }

    public static final Creator<RedPacketEvent> CREATOR = new Creator<RedPacketEvent>() {
        @Override
        public RedPacketEvent createFromParcel(Parcel source) {
            return new RedPacketEvent(source);
        }

        @Override
        public RedPacketEvent[] newArray(int size) {
            return new RedPacketEvent[size];
        }
    };
}

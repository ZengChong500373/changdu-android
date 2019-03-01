package org.linyi.base.entity.entity;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Parcelable ： 传递到签到页面
 */
public class SignDayEntity implements Parcelable {

    private int day;
    private int point;
    private boolean isSigned;

    public SignDayEntity(int day, int point, boolean isSigned) {
        this.day = day;
        this.point = point;
        this.isSigned = isSigned;
    }

    public int getDay() {
        return day;
    }

    public int getPoint() {
        return point;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.day);
        dest.writeInt(this.point);
        dest.writeByte(this.isSigned ? (byte) 1 : (byte) 0);
    }

    protected SignDayEntity(Parcel in) {
        this.day = in.readInt();
        this.point = in.readInt();
        this.isSigned = in.readByte() != 0;
    }

    public static final Creator<SignDayEntity> CREATOR = new Creator<SignDayEntity>() {
        @Override
        public SignDayEntity createFromParcel(Parcel source) {
            return new SignDayEntity(source);
        }

        @Override
        public SignDayEntity[] newArray(int size) {
            return new SignDayEntity[size];
        }
    };
}

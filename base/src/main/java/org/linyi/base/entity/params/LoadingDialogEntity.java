package org.linyi.base.entity.params;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by rwz on 2017/3/13.
 * 消息对话框传递实体类
 */

public class LoadingDialogEntity implements Parcelable {

    //为空则显示不带背景的loading
    private String tips;

    private boolean canDismissOutSide;//点击外面是否可以关闭

    public LoadingDialogEntity(@Nullable String tips) {
        this.tips = tips;
        canDismissOutSide = false;
    }

    public LoadingDialogEntity(@Nullable String tips, boolean canDismissOutSide) {
        this.tips = tips;
        this.canDismissOutSide = canDismissOutSide;
    }

    public void setCanDismissOutSide(boolean canDismissOutSide) {
        this.canDismissOutSide = canDismissOutSide;
    }

    public boolean isCanDismissOutSide() {
        return canDismissOutSide;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public LoadingDialogEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tips);
        dest.writeByte(this.canDismissOutSide ? (byte) 1 : (byte) 0);
    }

    protected LoadingDialogEntity(Parcel in) {
        this.tips = in.readString();
        this.canDismissOutSide = in.readByte() != 0;
    }

    public static final Creator<LoadingDialogEntity> CREATOR = new Creator<LoadingDialogEntity>() {
        @Override
        public LoadingDialogEntity createFromParcel(Parcel source) {
            return new LoadingDialogEntity(source);
        }

        @Override
        public LoadingDialogEntity[] newArray(int size) {
            return new LoadingDialogEntity[size];
        }
    };
}

package org.linyi.base.entity.params;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.linyi.base.R;
import org.linyi.base.utils.UIUtils;


/**
 * Created by rwz on 2017/3/13.
 * 消息对话框传递实体类
 */

public class MsgDialogEntity implements Parcelable {

    private int requestCode;
    private String title;
    private String msg;
    private String hint;
    private String enterText;  //确认文字
    private String cancelText; //取消文字(为空 则 只显示单按钮)
    private boolean cancelable;//设置外部区域是否可以取消
    private Bundle params;     //参数

    public MsgDialogEntity(String title, String msg, int requestCode) {
        this.title = title;
        this.msg = msg;
        this.requestCode = requestCode;
        enterText = UIUtils.getString(R.string.enter);
        cancelText = UIUtils.getString(R.string.cancel);
        cancelable = true;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Bundle getParams() {
        return params;
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public String getEnterText() {
        return enterText;
    }

    public void setEnterText(String enterText) {
        this.enterText = enterText;
    }

    public String getCancelText() {
        return cancelText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return msg;
    }

    public MsgDialogEntity() {
    }

    public static Creator<MsgDialogEntity> getCREATOR() {
        return CREATOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsgDialogEntity that = (MsgDialogEntity) o;
        return requestCode == that.requestCode;

    }

    @Override
    public int hashCode() {
        return requestCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.msg);
        dest.writeInt(this.requestCode);
        dest.writeString(this.enterText);
        dest.writeString(this.cancelText);
        dest.writeByte(this.cancelable ? (byte) 1 : (byte) 0);
        dest.writeBundle(this.params);
    }

    protected MsgDialogEntity(Parcel in) {
        this.title = in.readString();
        this.msg = in.readString();
        this.requestCode = in.readInt();
        this.enterText = in.readString();
        this.cancelText = in.readString();
        this.cancelable = in.readByte() != 0;
        this.params = in.readBundle(getClass().getClassLoader());
    }

    public static final Creator<MsgDialogEntity> CREATOR = new Creator<MsgDialogEntity>() {
        @Override
        public MsgDialogEntity createFromParcel(Parcel source) {
            return new MsgDialogEntity(source);
        }

        @Override
        public MsgDialogEntity[] newArray(int size) {
            return new MsgDialogEntity[size];
        }
    };
}

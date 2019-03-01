package com.linyi.reader.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.linyi.reader.R;
import com.linyi.reader.constant.PageMode;

import org.linyi.base.utils.UIUtils;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/23 0023 下午 6:03
 */
public class ReaderConfig implements Parcelable {

    public static final int SIZE_10 = (int) UIUtils.getDimension(R.dimen.h_10);

    //小说字体大小px
    private int fontSize = (int) UIUtils.getDimension(R.dimen.text_16);
    //电池字体大小px
    private int batteryFontSize = (int) UIUtils.getDimension(R.dimen.text_13);
    //章节字体大小px
    private int chapterFontSize = (int) UIUtils.getDimension(R.dimen.text_24);
    //电池边界宽度
    private int batteryWidth = (int) UIUtils.getDimension(R.dimen.h_20);
    //电池边界高度
    private int batteryHeight = (int) UIUtils.getDimension(R.dimen.h_10);
    //电池线条尺寸
    private int batteryLineSize = (int) UIUtils.getDimension(R.dimen.h_1);
    //左右与边缘的距离
    private int marginHeight = (int) UIUtils.getDimension(R.dimen.h_16);
    //上下与边缘的距离
    private int marginWidth = (int) UIUtils.getDimension(R.dimen.h_16);
    //状态栏距离底部高度
    private int statusMarginBottom = (int) UIUtils.getDimension(R.dimen.h_25);
    //行间距
    private int lineSpace = SIZE_10;
    //章节空隙
    private int chapterSpace = SIZE_10;
    //段间距
    private int paragraphSpace = SIZE_10 * 2;
    //是否开启护眼模式
    private boolean isOpenEyeCareMode;
    //是否开启自动阅读
    private boolean isOpenAutoRead;
    //翻页模式
    private int pageMode = PageMode.COVER;
    //皮肤类型
    private int skinType = 0;
    //屏幕亮度（0 ~ 1f, -1表示跟随系统）
    private float screenLight = -1;
    //字体
    private int typeface;

    public int getTypeface() {
        return typeface;
    }

    public void setTypeface(int typeface) {
        this.typeface = typeface;
    }

    public float getScreenLight() {
        return screenLight;
    }

    public void setScreenLight(float screenLight) {
        this.screenLight = screenLight;
    }

    public int getSkinType() {
        return skinType;
    }

    public void setSkinType(int skinType) {
        this.skinType = skinType;
    }

    public int getPageMode() {
        return pageMode;
    }

    public void setPageMode(int pageMode) {
        this.pageMode = pageMode;
    }

    public boolean isOpenAutoRead() {
        return isOpenAutoRead;
    }

    public void setOpenAutoRead(boolean openAutoRead) {
        isOpenAutoRead = openAutoRead;
    }

    public boolean isOpenEyeCareMode() {
        return isOpenEyeCareMode;
    }

    public void setOpenEyeCareMode(boolean openEyeCareMode) {
        isOpenEyeCareMode = openEyeCareMode;
    }

    public int getBatteryHeight() {
        return batteryHeight;
    }

    public int getBatteryWidth() {
        return batteryWidth;
    }

    public int getBatteryLineSize() {
        return batteryLineSize;
    }

    public int getBatteryFontSize() {
        return batteryFontSize;
    }

    public int getChapterFontSize() {
        return chapterFontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getMarginWidth() {
        return marginWidth;
    }

    public int getMarginHeight() {
        return marginHeight;
    }

    public int getStatusMarginBottom() {
        return statusMarginBottom;
    }

    public int getLineSpace() {
        return lineSpace;
    }

    public int getParagraphSpace() {
        return paragraphSpace;
    }

    public int getChapterSpace() {
        return chapterSpace;
    }

    public void setChapterSpace(int chapterSpace) {
        this.chapterSpace = chapterSpace;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setBatteryFontSize(int batteryFontSize) {
        this.batteryFontSize = batteryFontSize;
    }

    public void setBatteryWidth(int batteryWidth) {
        this.batteryWidth = batteryWidth;
    }

    public void setBatteryHeight(int batteryHeight) {
        this.batteryHeight = batteryHeight;
    }

    public void setBatteryLineSize(int batteryLineSize) {
        this.batteryLineSize = batteryLineSize;
    }

    public void setChapterFontSize(int chapterFontSize) {
        this.chapterFontSize = chapterFontSize;
    }

    public void setMarginHeight(int marginHeight) {
        this.marginHeight = marginHeight;
    }

    public void setMarginWidth(int marginWidth) {
        this.marginWidth = marginWidth;
    }

    public void setStatusMarginBottom(int statusMarginBottom) {
        this.statusMarginBottom = statusMarginBottom;
    }

    public void setLineSpace(int lineSpace) {
        this.lineSpace = lineSpace;
    }

    public void setParagraphSpace(int paragraphSpace) {
        this.paragraphSpace = paragraphSpace;
    }

    public ReaderConfig() {
    }

    @Override
    public String toString() {
        return "ReaderConfig{" +
                "fontSize=" + fontSize +
                ", batteryFontSize=" + batteryFontSize +
                ", chapterFontSize=" + chapterFontSize +
                ", batteryWidth=" + batteryWidth +
                ", batteryHeight=" + batteryHeight +
                ", batteryLineSize=" + batteryLineSize +
                ", marginHeight=" + marginHeight +
                ", marginWidth=" + marginWidth +
                ", statusMarginBottom=" + statusMarginBottom +
                ", lineSpace=" + lineSpace +
                ", chapterSpace=" + chapterSpace +
                ", paragraphSpace=" + paragraphSpace +
                ", skinType=" + skinType +
                ", isOpenEyeCareMode=" + isOpenEyeCareMode +
                ", isOpenAutoRead=" + isOpenAutoRead +
                ", pageMode=" + pageMode +
                ", skinType=" + skinType +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fontSize);
        dest.writeInt(this.batteryFontSize);
        dest.writeInt(this.chapterFontSize);
        dest.writeInt(this.batteryWidth);
        dest.writeInt(this.batteryHeight);
        dest.writeInt(this.batteryLineSize);
        dest.writeInt(this.marginHeight);
        dest.writeInt(this.marginWidth);
        dest.writeInt(this.statusMarginBottom);
        dest.writeInt(this.lineSpace);
        dest.writeInt(this.chapterSpace);
        dest.writeInt(this.paragraphSpace);
        dest.writeByte(this.isOpenEyeCareMode ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOpenAutoRead ? (byte) 1 : (byte) 0);
        dest.writeInt(this.pageMode);
        dest.writeInt(this.skinType);
        dest.writeFloat(this.screenLight);
        dest.writeInt(this.typeface);
    }

    protected ReaderConfig(Parcel in) {
        this.fontSize = in.readInt();
        this.batteryFontSize = in.readInt();
        this.chapterFontSize = in.readInt();
        this.batteryWidth = in.readInt();
        this.batteryHeight = in.readInt();
        this.batteryLineSize = in.readInt();
        this.marginHeight = in.readInt();
        this.marginWidth = in.readInt();
        this.statusMarginBottom = in.readInt();
        this.lineSpace = in.readInt();
        this.chapterSpace = in.readInt();
        this.paragraphSpace = in.readInt();
        this.isOpenEyeCareMode = in.readByte() != 0;
        this.isOpenAutoRead = in.readByte() != 0;
        this.pageMode = in.readInt();
        this.skinType = in.readInt();
        this.screenLight = in.readFloat();
        this.typeface = in.readInt();
    }

    public static final Creator<ReaderConfig> CREATOR = new Creator<ReaderConfig>() {
        @Override
        public ReaderConfig createFromParcel(Parcel source) {
            return new ReaderConfig(source);
        }

        @Override
        public ReaderConfig[] newArray(int size) {
            return new ReaderConfig[size];
        }
    };
}

package org.linyi.base.entity.params;

import android.graphics.drawable.Drawable;

import org.linyi.base.R;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.NetUtil;
import org.linyi.base.utils.UIUtils;


/**
 * Created by rwz on 2017/3/13.
 * 空视图基类
 */

public class TempEntity {

    private String nullTipsStr = UIUtils.getString(R.string.null_data);
    private String errorTipsStr  = UIUtils.getString(R.string.load_error);
    private String nullBtnStr = UIUtils.getString(R.string.click_retry);
    private String errorBtnStr = UIUtils.getString(R.string.click_retry);
    private boolean isShowNullBtn = false;
    private boolean isShowErrorBtn = true;
    private Drawable nullImgRes;    // = R.mipmap.yemianjiazaishibai;
    private Drawable errorImgRes; // = R.mipmap.yemianjiazaishibai;
    public int type;

    public TempEntity() {
        this(TempView.STATUS_LOADING);
    }

    public TempEntity(Drawable nullImgRes, Drawable errorImgRes) {
        this(TempView.STATUS_LOADING);
        this.nullImgRes = nullImgRes;
        this.errorImgRes = errorImgRes;
    }

    public TempEntity(int type) {
        this.type = type;
        nullImgRes = UIUtils.getDrawable(R.mipmap.ic_launcher);
        errorImgRes = UIUtils.getDrawable(R.mipmap.ic_launcher);
    }

    public String getNullTipsStr() {
        return nullTipsStr;
    }

    public String getErrorTipsStr() {
        return NetUtil.isNetworkAvailable() ? errorTipsStr : UIUtils.getString(R.string.no_net_check_retry);
    }

    public boolean isNoNet(int type) {
        return type == TempView.STATUS_ERROR && !NetUtil.isNetworkAvailable();
    }

    public String getNullBtnStr() {
        return nullBtnStr;
    }

    public String getErrorBtnStr() {
        return errorBtnStr;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public boolean getShowNullBtn(int type) {
        return isShowNullBtn && (type == TempView.STATUS_NULL);
    }

    public boolean getShowErrorBtn(int type) {
        return isShowErrorBtn && (type == TempView.STATUS_ERROR);
    }

    public Drawable getNullImgRes() {
        return nullImgRes;
    }

    public Drawable getErrorImgRes() {
        return errorImgRes;
    }

    public void setNullTipsStr(String nullTipsStr) {
        this.nullTipsStr = nullTipsStr;
    }

    public void setErrorTipsStr(String errorTipsStr) {
        this.errorTipsStr = errorTipsStr;
    }

    public void setNullBtnStr(String nullBtnStr) {
        this.nullBtnStr = nullBtnStr;
    }

    public void setErrorBtnStr(String errorBtnStr) {
        this.errorBtnStr = errorBtnStr;
    }

    public void setShowNullBtn(boolean showNullBtn) {
        isShowNullBtn = showNullBtn;
    }

    public void setShowErrorlBtn(boolean showErrorBtn) {
        isShowErrorBtn = showErrorBtn;
    }

    public void setNullImgRes(Drawable nullImgRes) {
        this.nullImgRes = nullImgRes;
    }

    public void setErrorImgRes(Drawable errorImgRes) {
        this.errorImgRes = errorImgRes;
    }

    public boolean isShowNullBtn() {
        return isShowNullBtn;
    }

    public boolean isShowErrorBtn() {
        return isShowErrorBtn;
    }
}

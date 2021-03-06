package org.linyi.ui.text;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;

/**
 * Created by Administrator on 2017/2/7.
 */

public class CommSpannableString extends SpannableString {

    private String text;

    private CommSpannableString(CharSequence source) {
        super(source);
        this.text = source+"";
    }

    public static CommSpannableString getInstance(String text) {
        return new CommSpannableString(text);
    }

    /**
     *   Spanned.SPAN_EXCLUSIVE_EXCLUSIVE —— (a,b)
     Spanned.SPAN_EXCLUSIVE_INCLUSIVE —— (a,b]
     Spanned.SPAN_INCLUSIVE_EXCLUSIVE —— [a,b)
     Spanned.SPAN_INCLUSIVE_INCLUSIVE —— [a,b]
     * @function 添加字体样式
     */
    public CommSpannableString addTextAppearanceSpan(Context context, @StyleRes int idStyle , int start , int end) {
        if (!TextUtils.isEmpty(text) && start >= 0 && end <= text.length()) {
            setSpan(new TextAppearanceSpan(context, idStyle), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /**
     * @function 添加字体相对大小
     */
    public CommSpannableString addRelativeSizeSpan(float relativeSize, int start , int end) {
        if (!TextUtils.isEmpty(text) && start >= 0 && end <= text.length()) {
            setSpan(new RelativeSizeSpan(relativeSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }


    public CommSpannableString addRelativeSizeSpan(float relativeSize, String target) {
        if (!TextUtils.isEmpty(text)) {
            int index = text.indexOf(target);
            if (index >= 0) {
                return addRelativeSizeSpan(relativeSize, index, index + target.length());
            }
        }
        return this;
    }

    /***
     * @function 添加字体颜色
     */
    public CommSpannableString addForegroundColorSpan(@ColorInt int color, int start , int end) {
        if (!TextUtils.isEmpty(text) && start >= 0 && end <= text.length()) {
            setSpan(new ForegroundColorSpan(color),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /** 前景着色 **/
    public CommSpannableString addForegroundColorSpan(@ColorInt int color, String targetStr) {
        if (!TextUtils.isEmpty(targetStr) && !TextUtils.isEmpty(text)) {
            int index = text.indexOf(targetStr);
            if (index >= 0) {
                return addForegroundColorSpan(color, index, index + targetStr.length());
            }
        }
        return this;
    }

    /** 添加icon **/
    public CommSpannableString addImageSpan(Context context, @DrawableRes int drawableId, int start , int end) {
        if (!TextUtils.isEmpty(text) && start >= 0 && end <= text.length()) {
            ImageSpan span = new ImageSpan(context, BitmapFactory.decodeResource(context.getResources(), drawableId));
            setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /** 添加下划线 **/
    public CommSpannableString addUnderlineSpan(int start , int end) {
        if (!TextUtils.isEmpty(text) && start >= 0 && end <= text.length()) {
            setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

}

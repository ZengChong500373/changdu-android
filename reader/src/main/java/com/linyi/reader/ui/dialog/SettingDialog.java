package com.linyi.reader.ui.dialog;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.linyi.reader.R;
import com.linyi.reader.ReaderConstant;
import com.linyi.reader.entity.ReaderConfig;
import com.linyi.reader.entity.event.ReaderConfigEvent;
import com.linyi.reader.manager.ReaderEventManager;
import com.linyi.reader.ui.CircleImageView;
import com.linyi.reader.utils.ReaderUtils;

import org.linyi.base.constant.Key;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.DialogHelp;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/15 0015 上午 10:49
 */
public class SettingDialog extends BaseReaderDialog {

    private final int MAX_FONT_SIZE = (int) UIUtils.getDimension(R.dimen.text_24);
    private final int MIN_FONT_SIZE = (int) UIUtils.getDimension(R.dimen.text_8);
    private final int FONT_SIZE_DX = (int) UIUtils.getDimension(R.dimen.h_2);
    //选中的icon
    private final int CHECKED_IMAGE = R.mipmap.ic_light;

    private SeekBar mSeekBar;
    private ReaderConfig mConfig;
    private CheckedTextView mEyeCareMode;
    private CheckedTextView mAutoRead;
    private CheckedTextView mFollowSys;
    private ImageView mLight;
    private ViewGroup mThemeColorContainer;
    private TextView mAddFont;
    private TextView mRemoveFont;
    private CheckedTextView mComplexChinese;
    private TextView mCustomFont;
    private ImageView mLayoutTight;
    private ImageView mLayoutNormal;
    private ImageView mLayoutSpacious;
    private TextView mCustomLayout;
    private TextView mCustomBg;
    private TextView mPageMode;

    @Override
    protected int setLayout() {
        return R.layout.layout_read_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mConfig = getArguments().getParcelable(Key.PARCELABLE_ENTITY);
        mLight = findViewById(R.id.light);
        mSeekBar = findViewById(R.id.seekBar);
        mEyeCareMode = findViewById(R.id.eye_care_mode);
        mAutoRead = findViewById(R.id.auto_read);
        mFollowSys = findViewById(R.id.follow_sys);
        mAddFont = findViewById(R.id.add_font);
        mRemoveFont = findViewById(R.id.remove_font);
        mComplexChinese = findViewById(R.id.complex_chinese);
        mCustomFont = findViewById(R.id.custom_font);
        mThemeColorContainer = findViewById(R.id.themeColorContainer);
        mLayoutTight = findViewById(R.id.layout_tight);
        mLayoutNormal = findViewById(R.id.layout_normal);
        mLayoutSpacious = findViewById(R.id.layout_spacious);
        mCustomLayout = findViewById(R.id.custom_layout);
        mCustomBg = findViewById(R.id.custom_bg);
        mPageMode = findViewById(R.id.page_mode);

        float screenLight = mConfig == null ? -1 : mConfig.getScreenLight();
        boolean isFollowSys = Float.compare(screenLight, -1f) == 0;
        if (isFollowSys) {
            float screenBrightness = ReaderUtils.getScreenBrightness(getActivity());
            mSeekBar.setProgress((int) (screenBrightness * mSeekBar.getMax()));
        }
        else
            mSeekBar.setProgress((int) (screenLight * mSeekBar.getMax()));
        mFollowSys.setChecked(isFollowSys);
        mEyeCareMode.setChecked(mConfig != null && mConfig.isOpenEyeCareMode());
        mAutoRead.setChecked(mConfig != null && mConfig.isOpenAutoRead());
        mComplexChinese.setChecked(mConfig != null && mConfig.getTypeface() == ReaderConstant.Typeface.COMPLEX);
        initThemeColor();
        //设置夜间模式
        int[] readBg = UIUtils.getIntArr(R.array.read_bg);
        int nightType = readBg != null ? readBg.length - 1: 0;
        boolean isNightMode = mConfig != null && mConfig.getSkinType() == nightType;
        setNightMode(isNightMode);

        setOnClickListener(R.id.add_font, R.id.remove_font,
                R.id.layout_tight, R.id.layout_normal, R.id.layout_spacious, R.id.custom_layout, R.id.custom_bg,
                R.id.page_mode, R.id.custom_font,
                R.id.follow_sys, R.id.complex_chinese, R.id.eye_care_mode, R.id.auto_read);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        if (mConfig != null) {
            setLayoutParams(mConfig.getLineSpace());
        }
    }

    private void setLayoutParams(int lineSpace) {
        if (ReaderConfig.SIZE_10 / 2 == lineSpace) {
            setLayoutSpace(ReaderConfig.SIZE_10 / 2, ReaderConfig.SIZE_10, ReaderConfig.SIZE_10 / 2);
            mLayoutTight.setImageResource(R.mipmap.ic_layout_5_checked);
            mLayoutNormal.setImageResource(R.mipmap.ic_layout_3);
            mLayoutSpacious.setImageResource(R.mipmap.ic_layout_2);
        } else if (ReaderConfig.SIZE_10 * 3 / 2 == lineSpace) {
            setLayoutSpace(ReaderConfig.SIZE_10 * 3 / 2, ReaderConfig.SIZE_10 * 3, ReaderConfig.SIZE_10 * 3 / 2);
            mLayoutTight.setImageResource(R.mipmap.ic_layout_5);
            mLayoutNormal.setImageResource(R.mipmap.ic_layout_3);
            mLayoutSpacious.setImageResource(R.mipmap.ic_layout_2_checked);
        } else {
            setLayoutSpace(ReaderConfig.SIZE_10, ReaderConfig.SIZE_10 * 2, ReaderConfig.SIZE_10);
            mLayoutTight.setImageResource(R.mipmap.ic_layout_5);
            mLayoutNormal.setImageResource(R.mipmap.ic_layout_3_checked);
            mLayoutSpacious.setImageResource(R.mipmap.ic_layout_2);
        }
    }

    @Override
    protected void setNightMode(boolean isNightMode) {
        super.setNightMode(isNightMode);
        mLight.setImageResource(isNightMode ? R.mipmap.ic_light : R.mipmap.ic_light);
//        mLayoutTight.setImageResource(isNightMode ? R.mipmap.ic_menu : R.mipmap.ic_menu_white);
//        mLayoutNormal.setImageResource(isNightMode ? R.mipmap.ic_menu : R.mipmap.ic_menu_white);
//        mLayoutSpacious.setImageResource(isNightMode ? R.mipmap.ic_menu : R.mipmap.ic_menu_white);
        int textColor = isNightMode ? R.color.reader_text_color_night : R.color.reader_text_color_day;
        ColorStateList colorStateList = getResources().getColorStateList(textColor);
        mEyeCareMode.setTextColor(colorStateList);
        mAutoRead.setTextColor(colorStateList);
        mFollowSys.setTextColor(colorStateList);
        mAddFont.setTextColor(colorStateList);
        mRemoveFont.setTextColor(colorStateList);
        mComplexChinese.setTextColor(colorStateList);
        mCustomFont.setTextColor(colorStateList);
        mCustomLayout.setTextColor(colorStateList);
        mCustomBg.setTextColor(colorStateList);
        mPageMode.setTextColor(colorStateList);
    }

    private CircleImageView mCurrColorView;
    private void initThemeColor() {
        int currColor = mConfig == null ? 0 : mConfig.getSkinType();
        int[] readBgArr = UIUtils.getIntArr(R.array.read_bg);
        if (readBgArr != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.rightMargin = (int) UIUtils.getDimension(R.dimen.h_16);
            params.width = 0;
            params.weight = 1;
            for (int i = 0; i < readBgArr.length - 1; i++) {
                int color = readBgArr[i];
                CircleImageView imageView = new CircleImageView(getContext());
                imageView.setCircleColor(color);
                imageView.setOnClickListener(onSkinClickListener);
                imageView.setTag(imageView.getId(), i);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setImageResource(color == currColor ? CHECKED_IMAGE : 0);
                if(color == currColor)
                    mCurrColorView = imageView;
                mThemeColorContainer.addView(imageView, params);
            }
        }
    }

    View.OnClickListener onSkinClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag(v.getId());
            if (tag instanceof Integer && mConfig !=  null) {
                if(mCurrColorView != null)
                    mCurrColorView.setImageResource(0);
                mConfig.setSkinType((Integer) tag);
                mCurrColorView = (CircleImageView) v;
                mCurrColorView.setImageResource(CHECKED_IMAGE);
                ReaderEventManager.getInstance().onReaderConfig(new ReaderConfigEvent(mConfig, false));
                SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.add_font) {  //加字号
            if(mConfig != null)
                setFontSize(mConfig.getFontSize() + FONT_SIZE_DX);
        } else if (id == R.id.remove_font) {//减字号
            if(mConfig != null)
                setFontSize(mConfig.getFontSize() - FONT_SIZE_DX);
        } else if (id == R.id.layout_tight) {//紧排版
            setLayoutParams(ReaderConfig.SIZE_10 / 2);
        } else if (id == R.id.layout_normal) {//正常排版
            setLayoutParams(ReaderConfig.SIZE_10);
        } else if (id == R.id.layout_spacious) {//宽松排版
            setLayoutParams(ReaderConfig.SIZE_10  * 3 / 2);
        } else if (id == R.id.custom_layout) {//自定义布局
            ToastUtils.getInstance().showShortSingle(R.string.not_support_now);
        } else if (id == R.id.custom_bg) {//自定义背景
            ToastUtils.getInstance().showShortSingle(R.string.not_support_now);
        } else if (id == R.id.page_mode) {//翻页模式
            modifyPageMode();
        } else if (id == R.id.follow_sys) { //跟随系统调节亮度
            mFollowSys.toggle();
            ReaderUtils.startAutoWindowBrightness(getActivity());
            float screenBrightness = ReaderUtils.getScreenBrightness(getActivity());
            mSeekBar.setProgress((int) (screenBrightness * mSeekBar.getMax()));
            if (mConfig != null) {
                mConfig.setScreenLight(-1);
            }
            SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
        } else if (id == R.id.custom_font) {//自定义字体
            ToastUtils.getInstance().showShortSingle(R.string.not_support_now);
        } else if (id == R.id.complex_chinese) {//简繁切换
            if (mConfig != null) {
                mComplexChinese.toggle();
                mConfig.setTypeface(mConfig.getTypeface() == ReaderConstant.Typeface.COMPLEX ? ReaderConstant.Typeface.SIMPLE : ReaderConstant.Typeface.COMPLEX);
                ReaderEventManager.getInstance().onReaderConfig(new ReaderConfigEvent(mConfig, true));
                SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
            }
        } else if (id == R.id.eye_care_mode) {//护眼模式
            if (mConfig != null) {
                mEyeCareMode.toggle();
                mConfig.setOpenEyeCareMode(!mConfig.isOpenEyeCareMode());
                ReaderEventManager.getInstance().onReaderConfig(new ReaderConfigEvent(mConfig, false));
                SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
            }
        } else if (id == R.id.auto_read) {//自动阅读
            if (mConfig != null) {
                mAutoRead.toggle();
                mConfig.setOpenAutoRead(!mConfig.isOpenAutoRead());
                ReaderEventManager.getInstance().onReaderConfig(new ReaderConfigEvent(mConfig, false));
                SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
                dismissAllowingStateLoss();
            }
        }
    }

    private void setFontSize(int fontSize) {
        if (mConfig != null) {
            if (fontSize > MAX_FONT_SIZE) {
                ToastUtils.getInstance().showShortSingle(R.string.set_max_font_size);
                return;
            }
            if (fontSize < MIN_FONT_SIZE) {
                ToastUtils.getInstance().showShortSingle(R.string.set_min_font_size);
                return;
            }
            mConfig.setFontSize(fontSize);
            ReaderEventManager.getInstance().onReaderConfig(new ReaderConfigEvent(mConfig, true));
            SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
        }
    }

    private void setLayoutSpace(int lineSpace, int paragraphSpace, int chapterSpace) {
        if (mConfig != null) {
            if(mConfig.getLineSpace() == lineSpace && mConfig.getParagraphSpace() == paragraphSpace && mConfig.getChapterSpace() == chapterSpace)
                return;
            mConfig.setLineSpace(lineSpace);
            mConfig.setParagraphSpace(paragraphSpace);
            mConfig.setChapterSpace(chapterSpace);
            ReaderEventManager.getInstance().onReaderConfig(new ReaderConfigEvent(mConfig, true));
            SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
        }
    }

    private void modifyPageMode() {
        PageModeDialog dialog = PageModeDialog.newInstance(mConfig);
        DialogHelp.show(getFragmentManager(), dialog, "PageModeDialog");
        dismissAllowingStateLoss();
    }

    private boolean modifyLight(boolean showToast) {
        FragmentActivity activity = getActivity();
        float brightness = mSeekBar.getProgress() * 1f / mSeekBar.getMax();
        ReaderUtils.setWindowBrightness(activity, brightness);
        if (mConfig != null) {
            mConfig.setScreenLight(brightness);
        }
        SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
        return true;
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        private boolean fromUser;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.fromUser = fromUser;
            if (this.fromUser) {
                mFollowSys.setChecked(false);
                modifyLight(false);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

}

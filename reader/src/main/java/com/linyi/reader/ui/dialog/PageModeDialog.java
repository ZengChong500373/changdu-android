package com.linyi.reader.ui.dialog;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.linyi.reader.R;
import com.linyi.reader.constant.PageMode;
import com.linyi.reader.entity.ReaderConfig;
import com.linyi.reader.entity.event.ReaderConfigEvent;
import com.linyi.reader.manager.ReaderEventManager;

import org.linyi.base.constant.Key;
import org.linyi.base.constant.ServerConstant;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.UIUtils;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/15 0015 上午 10:49
 */
public class PageModeDialog extends BaseReaderDialog{

    private ReaderConfig mConfig;
    private CheckedTextView mSimulationView;
    private CheckedTextView mCoverView;
    private CheckedTextView mNoneView;

    public static PageModeDialog newInstance(ReaderConfig config) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.PARCELABLE_ENTITY, config);
        PageModeDialog dialog = new PageModeDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_page_mode;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mSimulationView = findViewById(R.id.simulation);
        mCoverView = findViewById(R.id.cover);
        mNoneView = findViewById(R.id.none);
        setOnClickListener(R.id.simulation, R.id.cover, R.id.none);
        mConfig = getArguments().getParcelable(Key.PARCELABLE_ENTITY);
        int[] readBg = UIUtils.getIntArr(R.array.read_bg);
        int nightType = readBg != null ? readBg.length - 1: 0;
        boolean isNightMode = mConfig != null && mConfig.getSkinType() == nightType;
        setNightMode(isNightMode);
        if (mConfig != null) {
            int pageMode = mConfig.getPageMode();
            if (pageMode == PageMode.SIMULATION) {
                mSimulationView.setChecked(true);
            } else if (pageMode == PageMode.COVER) {
                mCoverView.setChecked(true);
            } else if (pageMode == PageMode.NONE) {
                mNoneView.setChecked(true);
            }
        }
    }

    @Override
    protected void setNightMode(boolean isNightMode) {
        super.setNightMode(isNightMode);
        int textColor = isNightMode ? R.color.reader_text_color_night : R.color.reader_text_color_day;
        ColorStateList colorStateList = getResources().getColorStateList(textColor);
        mSimulationView.setTextColor(colorStateList);
        mCoverView.setTextColor(colorStateList);
        mNoneView.setTextColor(colorStateList);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.simulation) {
            setLayoutSpace(PageMode.SIMULATION);
        } else if (id == R.id.cover) {
            setLayoutSpace(PageMode.COVER);
        } else if (id == R.id.none) {
            setLayoutSpace(PageMode.NONE);
        }
        dismiss();
    }

    private void setLayoutSpace(int pageMode) {
        if (mConfig != null) {
            mConfig.setPageMode(pageMode);
            ReaderEventManager.getInstance().onReaderConfig(new ReaderConfigEvent(mConfig, false));
            SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
        }
    }




}

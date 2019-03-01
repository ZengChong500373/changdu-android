package com.linyi.reader.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;
import com.linyi.reader.R;
import com.linyi.reader.entity.ReaderConfig;
import com.linyi.reader.entity.event.ReaderConfigEvent;
import com.linyi.reader.manager.ChapterManager;
import com.linyi.reader.manager.NovelManager;
import com.linyi.reader.manager.ReaderEventManager;

import org.linyi.base.constant.Key;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.DialogHelp;
import org.linyi.base.utils.help.TurnHelp;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/15 0015 上午 10:49
 */
public class MenuDialog extends BaseReaderDialog{

    //临时保存的主题色，用于夜间模式的切换
    private static int sTempThemeColor;
    private ReaderConfig mConfig;
    private int nightType;
    private TextView mDirectoryView;
    private TextView mNightModeView;
    private TextView mCacheView;
    private TextView mSettingView;

    public static MenuDialog newInstance(ReaderConfig config) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.PARCELABLE_ENTITY, config);
        MenuDialog dialog = new MenuDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_read_menu;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mConfig = getArguments().getParcelable(Key.PARCELABLE_ENTITY);
        setOnClickListener(R.id.directory, R.id.cache, R.id.night_mode, R.id.setting);
        mDirectoryView = findViewById(R.id.directory);
        mNightModeView = findViewById(R.id.night_mode);
        mCacheView = findViewById(R.id.cache);
        mSettingView = findViewById(R.id.setting);
        int[] readBg = UIUtils.getIntArr(R.array.read_bg);
        nightType = readBg != null ? readBg.length - 1: 0;
        boolean isNightMode = mConfig != null && mConfig.getSkinType() == nightType;
        setNightMode(isNightMode);
    }

    @Override
    protected void setNightMode(boolean isNightMode) {
        super.setNightMode(isNightMode);
        mDirectoryView.setCompoundDrawablesWithIntrinsicBounds(0, isNightMode ? R.mipmap.ic_menu : R.mipmap.ic_menu_white, 0, 0);
        mNightModeView.setCompoundDrawablesWithIntrinsicBounds(0, isNightMode ? R.mipmap.ic_mode_day : R.mipmap.ic_mode_night, 0, 0);
        mNightModeView.setText(isNightMode ? R.string.day_mode : R.string.night_mode);
        mCacheView.setCompoundDrawablesWithIntrinsicBounds(0, isNightMode ? R.mipmap.ic_down : R.mipmap.ic_down_white, 0, 0);
        mSettingView.setCompoundDrawablesWithIntrinsicBounds(0, isNightMode ? R.mipmap.ic_setup : R.mipmap.ic_setup_white, 0, 0);
        mDirectoryView.setTextColor(UIUtils.getColor(isNightMode ? R.color.night_text : R.color.day_text));
        mNightModeView.setTextColor(UIUtils.getColor(isNightMode ? R.color.night_text : R.color.day_text));
        mCacheView.setTextColor(UIUtils.getColor(isNightMode ? R.color.night_text : R.color.day_text));
        mSettingView.setTextColor(UIUtils.getColor(isNightMode ? R.color.night_text : R.color.day_text));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.setting) {
            SettingDialog mSettingDialog = new SettingDialog();
            mSettingDialog.setArguments(getArguments());
            DialogHelp.show(getFragmentManager(), mSettingDialog, "SettingDialog");
        } else if (id == R.id.directory) {
            String bookID = NovelManager.getInstance().getBookID();
            String bookName = NovelManager.getInstance().getBookName();
            int currChapterSort = NovelManager.getInstance().getCurrChapterSort();
            TurnHelp.directory(getContext(), bookID, bookName, currChapterSort);
        } else if (id == R.id.cache) {
            SubscribeDialog dialog = new SubscribeDialog();
            DialogHelp.show(getFragmentManager(), dialog, "SubscribeDialog");
        } else if (id == R.id.night_mode) {
            if (mConfig != null) {
                boolean isNightMode = mConfig.getSkinType() == nightType;
                setNightMode(!isNightMode);
                if (isNightMode) {
                    mConfig.setSkinType(sTempThemeColor);
                } else {
                    sTempThemeColor = mConfig.getSkinType();
                    mConfig.setSkinType(nightType);
                }
            }
            ReaderEventManager.getInstance().onReaderConfig(new ReaderConfigEvent(mConfig, false));
            SharePreUtil.putObject(Key.READER_CONFIG, ReaderConfig.class, mConfig);
        }
        dismissAllowingStateLoss();
    }
}

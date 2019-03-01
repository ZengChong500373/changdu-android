package com.linyi.reader.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.api.changdu.proto.SystemApi;
import com.api.changdu.proto.UserApi;
import com.linyi.reader.R;
import com.linyi.reader.entity.event.LoadChapterEvent;
import com.linyi.reader.manager.ChapterManager;
import com.linyi.reader.manager.NovelManager;
import com.linyi.reader.manager.ReaderEventManager;
import com.linyi.reader.utils.NovelUtils;

import org.linyi.base.constant.Key;
import org.linyi.base.constant.ServerConstant;
import org.linyi.base.manager.AppManager;
import org.linyi.base.manager.UserManager;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.help.TurnHelp;

/**
 * @fun: 订阅章节
 * @developer: rwz
 * @date: 2019/2/20 0020 上午 9:51
 */
public class SubscribeDialog extends BaseReaderDialog implements CompoundButton.OnCheckedChangeListener {

    private SwitchCompat mAutoSubscribe;
    private int price;
    private int mScore;

    @Override
    protected int setLayout() {
        return R.layout.layout_read_subscribe;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mAutoSubscribe = findViewById(R.id.sc);
        TextView balance = findViewById(R.id.balance);
        TextView pay = findViewById(R.id.pay);
        TextView enter = findViewById(R.id.enter);

        setNightMode(NovelUtils.isNightMode());

        UserApi.AckUserInfo user = UserManager.getInstance().getUser();
        if (user != null) {
            mScore = user.getScore();
        }
        balance.setText(mScore + getString(R.string.coin));
        boolean isAutoSubscribe = SharePreUtil.getBoolean(Key.AUTO_SUBSCRIBE, false);
        mAutoSubscribe.setChecked(isAutoSubscribe);
        ChapterManager manager = NovelManager.getInstance().getCurrChapterManager();
        if(manager != null && manager.getPayType() == ServerConstant.PayType.PAY)
            price = getScore();
        else
            price = 0;
        enter.setEnabled(price != 0);
        if (price == 0) {
            enter.setText(R.string.subscribed);
        } else if(mScore < price)
            enter.setText(R.string.balance_enough_handle_task);
        else
            enter.setText(R.string.enter);
        pay.setText(price + getString(R.string.coin));
        mAutoSubscribe.setOnCheckedChangeListener(this);
        setOnClickListener(R.id.enter);
    }

    private int getScore() {
        SystemApi.AckSystemInit init = AppManager.getInstance().getAckSystemInit();
        if (init != null) {
            return init.getCost();
        }
        return -1;
    }

    @Override
    protected void setNightMode(boolean isNightMode) {
        super.setNightMode(isNightMode);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.enter) {
            if (mScore < price) { //执行任务
                TurnHelp.mission(getContext());
            } else {    //订阅章节
                ReaderEventManager.getInstance().loadChapter(new LoadChapterEvent(NovelManager.getInstance().getCurrChapterSort()));
            }
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharePreUtil.putBoolean(Key.AUTO_SUBSCRIBE, isChecked);
    }
}

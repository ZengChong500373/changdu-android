package com.linyi.viva.extra.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.net.module.UserModule;
import com.linyi.viva.extra.utils.help.TurnHelp;

import org.linyi.base.entity.db.HistoryRecordDbEntity;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.manager.UserManager;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.mvp.module.HistoryRecordModule;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.utils.ImageLoader.ImageLoader;
import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.LogUtil;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @fun: 我的
 * @developer: rwz
 * @date: 2019/1/30 0030 上午 10:03
 */
public class MineFragment extends LazyFragment{

    private ImageView mAvatarView;
    private TextView mNickNameView;
    private TextView mUserIDView;
    private TextView mLevelView;
    private TextView mCoinView;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        mAvatarView = (ImageView) findViewById(R.id.avatar);
        mNickNameView = (TextView) findViewById(R.id.nickname);
        mUserIDView = (TextView) findViewById(R.id.userID);
        mLevelView = (TextView) findViewById(R.id.level);
        mCoinView = (TextView) findViewById(R.id.coin);
        UserApi.AckUserInfo user = UserManager.getInstance().getUser();
        LogUtil.d("user = " + user);
        if (user != null)
            setupData(user);
        else
            requestData();
        setOnClickListener(R.id.level_exp, R.id.account_center, R.id.history_record, R.id.help_feedback,
                R.id.setup, R.id.avatar);
    }

    private void requestData() {
        UserModule.userInfo().subscribe(new CommonObserver<UserApi.AckUserInfo>() {
            @Override
            public void onNext(UserApi.AckUserInfo userInfo) {
                setupData(userInfo);
            }
        });
    }

    private void setupData(UserApi.AckUserInfo user) {
        String headUrl = user.getHeadUrl();
        if (!TextUtils.isEmpty(headUrl)) {
            ImageLoader imageLoader = new ImageLoader.Builder()
                    .url(headUrl)
                    .setCircle(true)
                    .isCenterCrop(true)
                    .imgView(mAvatarView)
                    .placeHolder(R.mipmap.ic_avatar_default)
                    .setErrorDrawable(R.mipmap.ic_avatar_default)
                    .build();
            ImageLoaderUtil.getInstance().loadImage(getContext(), imageLoader);
        }
        mNickNameView.setText(user.getName());
        mUserIDView.setText("ID: " + user.getUserID());
        mLevelView.setText(getString(R.string.level_default, user.getLevel()));
        mCoinView.setText(user.getScore() + getString(R.string.coin));
    }

    @Override
    public void onFirstUserVisible() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.level_exp) {
            TurnHelp.levelExp(getContext());
        } else if (id == R.id.account_center) {
            TurnHelp.accountCenter(getContext());
        } else if (id == R.id.history_record) {
          org.linyi.base.utils.help.TurnHelp.history(getContext());
            //         org.linyi.base.utils.help.TurnHelp.mission(getContext());
        } else if (id == R.id.help_feedback) {
            TurnHelp.feedback(getContext());
        } else if (id == R.id.setup) {
            TurnHelp.setup(getContext());
        } else if (id == R.id.avatar) {
//            TurnHelp.userInfo(getContext());
            HistoryRecordModule.getHistoryList(0, 10).subscribe(new Consumer<List<HistoryRecordDbEntity>>() {
                @Override
                public void accept(List<HistoryRecordDbEntity> list) throws Exception {
                    long historyCount = HistoryRecordModule.getHistoryCount();
                    LogUtil.d("getHistoryList", "historyCount = " + historyCount,  list);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }
}

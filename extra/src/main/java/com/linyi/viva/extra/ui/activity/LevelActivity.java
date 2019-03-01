package com.linyi.viva.extra.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.entity.comm.DoubleEntity;
import com.linyi.viva.extra.net.module.UserModule;

import org.linyi.base.inf.CommonObserver;
import org.linyi.base.manager.UserManager;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 经验等级
 */
public class LevelActivity extends BaseActivity {

    private ViewGroup mLevelHeaderContainer;
    private ViewGroup mLevelFooterContainer;

    @Override
    protected int setLayout() {
        return R.layout.activity_level;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        mLevelHeaderContainer = findViewById(R.id.levelHeaderContainer);
        mLevelFooterContainer = findViewById(R.id.levelFooterContainer);
    }

    @Override
    protected void initData() {
        super.initData();
        mTitleView.setText(R.string.level_exp);
        UserModule.expInfo()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoading();
                    }
                })
                .subscribe(mObserver);
    }

    CommonObserver<UserApi.AckExpInfo> mObserver = new CommonObserver<UserApi.AckExpInfo>() {
        @Override
        public void onNext(UserApi.AckExpInfo ackExpInfo) {
            setupData(ackExpInfo);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mObserver != null)
            mObserver.destroy();
    }

    private void setupData(UserApi.AckExpInfo expInfo) {
        List<DoubleEntity> levelHeaderData = new ArrayList<>();
        levelHeaderData.add(new DoubleEntity(getString(R.string.curr_level), "LV" +expInfo.getCurrentLevel(), R.mipmap.ic_curr_level));
        levelHeaderData.add(new DoubleEntity(getString(R.string.curr_progress), expInfo.getProgress() + "", R.mipmap.ic_level_progress));
        levelHeaderData.add(new DoubleEntity(getString(R.string.coin_ceiling), "" + expInfo.getMaxScore(), R.mipmap.ic_coin_max));
        LayoutInflater inflater = LayoutInflater.from(this);
        for (DoubleEntity entity : levelHeaderData) {
            View view = inflater.inflate(R.layout.item_level_header, null, false);
            ((TextView)view.findViewById(R.id.key)).setText(entity.getOne());
            ((TextView)view.findViewById(R.id.value)).setText(entity.getTwo());
            ((ImageView)view.findViewById(R.id.img)).setImageResource(entity.getImg());
            mLevelHeaderContainer.addView(view);
        }
        //底部
        List<UserApi.AckExpInfo.ExpInfo> expInfoList = expInfo.getExpInfoList();
        if (expInfoList != null && expInfoList.size() > 0) {
            for (int i = 0; i < expInfoList.size(); i++) {
                UserApi.AckExpInfo.ExpInfo entity = expInfoList.get(i);
                View view = inflater.inflate(R.layout.item_level_footer, null, false);
                view.setBackgroundResource(i % 2 == 0 ? R.color.level_bg_one : R.color.level_bg_two);
                ((TextView)view.findViewById(R.id.left)).setText("LV" + entity.getLevel());
                ((TextView)view.findViewById(R.id.right)).setText(entity.getMaxScore() + "");
                mLevelFooterContainer.addView(view);
            }
        }
    }


}

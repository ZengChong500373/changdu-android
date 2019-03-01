package com.linyi.viva.extra.ui.activity;


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.constant.Constant;
import org.linyi.base.entity.event.WeChatEvent;
import com.linyi.viva.extra.manager.EventManager;
import com.linyi.viva.extra.net.module.UserModule;
import com.linyi.viva.extra.ui.weidgt.IconTextView;
import com.test001.reader.WeChatProxy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.manager.UserManager;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.utils.ImageLoader.ImageLoader;
import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.help.SnackBarHelp;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 个人信息
 */
public class UserInfoActivity extends BaseActivity {

    private ImageView mAvatarView;
    private IconTextView mNameView, mSexView, mTelView, mWeChatView, mQQView;
    private WeChatProxy mWeChatProxy;

    @Override
    protected int setLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        mAvatarView = findViewById(R.id.avatar);
        mNameView = findViewById(R.id.nickname);
        mSexView = findViewById(R.id.sex);
        mTelView = findViewById(R.id.tel);
        mWeChatView = findViewById(R.id.wechat);
        mQQView = findViewById(R.id.qq);
        setOnClickListener(R.id.avatar, R.id.nickname, R.id.sex, R.id.tel, R.id.qq, R.id.wechat);
    }

    @Override
    protected void initData() {
        super.initData();
        mWeChatProxy = new WeChatProxy(this);
        mTitleView.setText(R.string.user_info);
        final UserApi.AckUserInfo user = UserManager.getInstance().getUser();
        LogUtil.d("user = " + user, (user == null));
        if (user != null) {
            setupData(user);
        } else {
            UserModule.userInfo()
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
                    .subscribe(new CommonObserver<UserApi.AckUserInfo>() {
                @Override
                public void onNext(UserApi.AckUserInfo userInfo) {
                    setupData(userInfo);
                }
            });
        }
        EventManager.getInstance().register(this);
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
            ImageLoaderUtil.getInstance().loadImage(this, imageLoader);
        }
        mNameView.setRight_text(user.getName());
        mSexView.setRight_text(getSexText(user.getGender()));
        mTelView.setRight_text(getPrivateTel(user.getPhoneNum()));
        mWeChatView.setRight_text(getString(user.getHasWechatOpenId() ? R.string.had_bind : R.string.go_to_bind));
        mQQView.setRight_text(getString(user.getHasQQOpenId() ? R.string.had_bind : R.string.go_to_bind));
    }

    public String getSexText(UserApi.Gender gender) {
        if(gender  == UserApi.Gender.FEMALE)
            return getString(R.string.girl);
        if(gender == UserApi.Gender.MALE)
            return getString(R.string.boy);
        return "";
    }

    public String getPrivateTel(String phone) {
        if(TextUtils.isEmpty(phone) || phone.length() < 11)
            return "";
        String sub = phone.substring(3, 7);
        return phone.replace(sub, "****");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.avatar) {

        } else if (id == R.id.nickname) {

        } else if (id == R.id.sex) {

        } else if (id == R.id.tel) {

        } else if (id == R.id.qq) {

        } else if (id == R.id.wechat) {
            showLoading();
            mWeChatProxy.sendReq();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeChatEvent(WeChatEvent event) {
        int type = event.getType();
        int result = event.getResult();
        if (type == WeChatEvent.LOGIN) {
            if (result == Constant.ShareResult.CANCEL) {
                hideLoading();
                SnackBarHelp.showSnackBar(mRootView, getString(R.string.cancel_authorization));
            } else if (result == Constant.ShareResult.FAIL) {
                hideLoading();
                SnackBarHelp.showSnackBar(mRootView, getString(R.string.authorization_fail));
            } else {
                // TODO: 2019/2/1 0001 绑定微信
            }
        }
    }


}

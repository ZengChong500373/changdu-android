package com.linyi.viva.extra.ui.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.constant.Constant;
import org.linyi.base.entity.event.WeChatEvent;
import com.linyi.viva.extra.manager.EventManager;
import com.linyi.viva.extra.net.module.UserModule;
import com.linyi.viva.extra.ui.weidgt.CodeTextView;
import com.linyi.viva.extra.utils.RegexHelp;
import com.test001.reader.WeChatProxy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.linyi.base.constant.Key;
import org.linyi.base.entity.event.LoginEvent;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.manager.UserManager;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.help.SnackBarHelp;
import org.linyi.base.utils.help.TurnHelp;
import org.linyi.ui.text.StateButton;

import io.reactivex.functions.Action;

public class LoginFragment extends LazyFragment implements TextWatcher {

    private EditText mTelEt;
    private EditText mCodeEt;
    private StateButton mLoginBtn;
    private CodeTextView mSendCodeView;
    private WeChatProxy mWeChatProxy;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void initView() {
        mWeChatProxy = new WeChatProxy(getContext());
        mCodeEt = (EditText) findViewById(R.id.et_code);
        mTelEt = (EditText) findViewById(R.id.et_tel);
        mLoginBtn = (StateButton) findViewById(R.id.login);
        mSendCodeView = (CodeTextView) findViewById(R.id.send_code);
        mLoginBtn.setEnabled(false);
        mTelEt.addTextChangedListener(this);
        mCodeEt.addTextChangedListener(this);
        setOnClickListener(R.id.qq, R.id.wx,R.id.login, R.id.user_agreement, R.id.skip, R.id.send_code);
        EventManager.getInstance().register(this);
        mTelEt.setText(SharePreUtil.getString(Key.TEL));
        if(!TextUtils.isEmpty(mTelEt.getText().toString()))
            mCodeEt.requestFocus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().unregister(this);
    }

    @Override
    public void onFirstUserVisible() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.qq) {

        } else if (id == R.id.wx) {
            showLoading();
            mWeChatProxy.sendReq();
        } else if (id == R.id.login) {
            if (mLoginBtn.isEnabled()) {
                showLoading();
                login(UserApi.LoginType.PHONE, mCodeEt.getText().toString());
            }
        } else if (id == R.id.user_agreement) {

        } else if (id == R.id.skip) {
            TurnHelp.main(getContext());
        } else if (id == R.id.send_code) {
            sendCode();
        }
    }

    private void sendCode() {
        String phoneNumber = mTelEt.getText().toString();
        if (RegexHelp.isPhoneNumber(phoneNumber)) {
            showLoading();
            mSendCodeView.startCountDown();
            UserModule.sendCode(phoneNumber)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            hideLoading();
                        }
                    })
                    .subscribe(new CommonObserver<UserApi.AckSMS>() {
                        @Override
                        public void onNext(UserApi.AckSMS ackSMS) {
                            mSendCodeView.startCountDown();
                            ToastUtils.getInstance().showShortSingle(R.string.sent_code_to_email);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mSendCodeView.setCanClick(true);
                        }
                    });
        } else {
            ToastUtils.getInstance().showShortSingle(R.string.input_valid_tel);
        }
    }

    private void login(UserApi.LoginType type, String code) {
        final String tel = mTelEt.getText().toString();
        UserModule.login(type, tel, code)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoading();
                    }
                })
                .subscribe(new CommonObserver<UserApi.AckUserLogin>() {
                    @Override
                    public void onNext(UserApi.AckUserLogin ackUserLogin) {
                        LogUtil.d("ackUserLogin = " + ackUserLogin);
                        UserManager.getInstance().saveToken(ackUserLogin.getToken());
                        SharePreUtil.putString(Key.TEL, tel);
                        EventManager.getInstance().loginSuccess(new LoginEvent());
                        TurnHelp.main(getContext());
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean isPhoneNumber = RegexHelp.isPhoneNumber(mTelEt.getText().toString());
        mLoginBtn.setEnabled(isPhoneNumber && !TextUtils.isEmpty(mCodeEt.getText()));
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
                login(UserApi.LoginType.WECHAT, event.getParams() + "");
            }
        }
    }

}


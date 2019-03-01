package com.linyi.viva.extra.ui.fragment;

import android.view.View;

import com.api.changdu.proto.Api;
import com.api.changdu.proto.UserApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.net.module.UserModule;
import com.linyi.viva.extra.ui.activity.LoginActivity;

import org.linyi.base.inf.CommonObserver;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.help.TurnHelp;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/26 0026 上午 9:43
 */
public class SexFragment extends LazyFragment{

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_sex;
    }

    @Override
    public void initView() {
        setOnClickListener(R.id.boy, R.id.girl, R.id.skip);
    }

    @Override
    public void onFirstUserVisible() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.boy) {
            switchTab(UserApi.Gender.MALE);
        } else if (i == R.id.girl) {
            switchTab(UserApi.Gender.FEMALE);
        } else if (i == R.id.skip) {
            skip();
        }
    }

    private void skip() {
        UserModule.favorite(UserApi.Gender.SECRECY, null, true)
                .subscribe(new CommonObserver<Api.Response>() {
                    @Override
                    public void onNext(Api.Response response) {
                        TurnHelp.main(getContext());
                    }
                });
    }

    private void switchTab(UserApi.Gender gender) {
        if(mSexConsumer != null) {
            try {
                mSexConsumer.accept(gender);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       /* LogUtil.d("switchTab", "gender = " + gender);
        LoginActivity activity = (LoginActivity) getActivity();
        activity.setGender(gender);
        activity.switchTab(2);*/
    }

    private Consumer<UserApi.Gender> mSexConsumer;

    public void setSexConsumer(Consumer<UserApi.Gender> mSexConsumer) {
        this.mSexConsumer = mSexConsumer;
    }
}

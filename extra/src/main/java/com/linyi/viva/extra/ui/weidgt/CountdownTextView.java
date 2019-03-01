package com.linyi.viva.extra.ui.weidgt;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;


import com.api.changdu.proto.MissionApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.entity.event.CountdownEvent;
import com.linyi.viva.extra.manager.EventManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.linyi.base.entity.entity.BenefitEntity;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.UIUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * 倒计时
 */
public class CountdownTextView extends AppCompatTextView {

    private boolean clickEnable = true;  //是否可点击
    private boolean isFinishCountDown;//是否结束倒计时

    private String countdownText = UIUtils.getString(R.string.re_get_code);
    private String initText = UIUtils.getString(R.string.reg_get_code);
    private Disposable mDisposable;
    private BenefitEntity entity;

    public CountdownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountdownTextView, 0, 0);
        initText = typedArray.getString(R.styleable.CountdownTextView_initText);
        countdownText = typedArray.getString(R.styleable.CountdownTextView_countdownText);
        typedArray.recycle();
        init();
    }

    public CountdownTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        stopCountdown();
        clickEnable = true;
        isFinishCountDown = false;
        setText(initText);
        setEnabled(clickEnable);
        if (!EventManager.getInstance().isRegister(this)) {
            EventManager.getInstance().register(this);
        }
    }

    public boolean isClickEnable() {
        return clickEnable;
    }

    public void setClickEnable(boolean clickEnable) {
        this.clickEnable = clickEnable;
    }

    public boolean getEnabled() {
        return isEnabled();
    }

    public void setBenefitEntity(BenefitEntity entity) {
        this.entity = entity;
        if (entity.getCountdownTime() > 0) {
            startCountDown(entity.getCountdownTime());
        } else {
            init();
        }
    }

    public void startCountDown(final long maxTime) {
        if (maxTime <= 0) {
            init();
            return;
        }
        stopCountdown();
        clickEnable = false;
        isFinishCountDown = false;
        setCountdownText(maxTime);
        setEnabled(clickEnable);
        LogUtil.d("startCountdown", "maxTime = " + maxTime);
        Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        //倒计时结束、控件销毁、短信发送失败都将结束倒计时
                        return aLong >= maxTime || isFinishCountDown || clickEnable;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<Long>(){
                    @Override
                    public void onComplete() {
                        LogUtil.d("onCompleted" ,"isFinishCountDown = " + isFinishCountDown);
                        if (!isFinishCountDown) {
                            init();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        entity.setCountdownTime(maxTime - aLong);
                        setCountdownText(maxTime - aLong);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }
                });
    }

    private void setCountdownText(long time) {
        String text = parseTime(time);
        if (TextUtils.isEmpty(countdownText)) {
            setText(text);
        } else {
            setText(String.format(countdownText, text));
        }
    }

    private String parseTime(long time) {
        long hour = time / 3600;
        long minute = time / 60 % 60;
        long second = time % 60;
        return String.format("%02d", hour) + ":"+ String.format("%02d", minute) + ":" + String.format("%02d", second);

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isFinishCountDown = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isFinishCountDown = true;
        stopCountdown();
        if (EventManager.getInstance().isRegister(this)) {
            EventManager.getInstance().unregister(this);
        }
    }

    public void stopCountdown() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startCountdown(CountdownEvent event) {
        if (entity != null && event != null) {
            MissionApi.AckMissionList.Mission mission = entity.getMission();
            if(mission == null)
                return;
            LogUtil.d("startCountdown", event.getType(), entity.getType(), entity.getCountdownTime());
            if(event.getType() == entity.getType())
                startCountDown(entity.getCountdownTime());
        }
    }

}

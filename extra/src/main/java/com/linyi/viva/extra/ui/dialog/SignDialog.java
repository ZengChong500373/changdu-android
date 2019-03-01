package com.linyi.viva.extra.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.api.changdu.proto.MissionApi;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.entity.event.ADEvent;
import com.linyi.viva.extra.entity.event.RedPacketEvent;
import com.linyi.viva.extra.entity.event.SignEvent;
import com.linyi.viva.extra.manager.EventManager;
import com.linyi.viva.extra.net.module.MissionModule;

import org.linyi.base.constant.Key;
import org.linyi.base.entity.entity.SignDayEntity;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.ui.BaseDialog;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.ui.text.CommSpannableString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SignDialog extends BaseDialog implements View.OnClickListener {

    private View mSignedView;
    private TextView mEnterView;
    private LinearLayout mDayContainerView;
    private TextView mSignUninterruptedView;
    private boolean isSign;
    private MissionApi.ExtInfo mExtInfo;

    public static SignDialog newInstance(MissionApi.ExtInfo extInfo) {
        SignDialog dialog = new SignDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Key.PARCELABLE_ENTITY, extInfo);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_sign;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Serializable one = getArguments().getSerializable(Key.PARCELABLE_ENTITY);
        mExtInfo = (MissionApi.ExtInfo) one;
        List<Integer> checkedList = mExtInfo == null ? null : mExtInfo.getCheckInDaysList();
        LogUtil.d("SignDialog", "extInfo = " + mExtInfo, "checkedList = " + checkedList);
        mEnterView = findViewById(R.id.enter);
        mSignedView = findViewById(R.id.signed);
        mDayContainerView = findViewById(R.id.day_container);
        mSignUninterruptedView = findViewById(R.id.sign_uninterrupted);
        mRootView.setOnClickListener(this);
        mEnterView.setOnClickListener(this);
        if (mExtInfo != null) {
            isSign = mExtInfo.getTodayIsCheck();
            mEnterView.setText(isSign ? R.string.enter : R.string.sign);
            mSignedView.setVisibility(isSign ? View.VISIBLE : View.INVISIBLE);
            setDayNum(mExtInfo.getCheckInDay());
        }
        initSignDay(mExtInfo, checkedList);
    }

    private void setDayNum(int dayNum) {
        CommSpannableString spannableString = CommSpannableString.getInstance(getString(R.string.sign_uninterrupted, dayNum));
        spannableString.addRelativeSizeSpan(1.8f, String.valueOf(dayNum))
                .addForegroundColorSpan(UIUtils.getColor(R.color.signed_num), String.valueOf(dayNum));
        mSignUninterruptedView.setText(spannableString);
    }

    private List<SignDayEntity> mData;
    private void initSignDay(MissionApi.ExtInfo extInfo, List<Integer> checkedList) {
        if(checkedList == null || extInfo == null)
            return;
        mData = new ArrayList<>();
        for (int i = 0; i < checkedList.size(); i++) {
            mData.add(new SignDayEntity(i + 1, checkedList.get(i), extInfo.getCheckInDay() >= i + 1));
        }
        for (SignDayEntity entity : mData) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.item_sign_day, null, false);
            setChildContent(entity, root);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            mDayContainerView.addView(root, params);
        }
    }

    private void setChildContent(SignDayEntity entity, View root) {
        if (entity != null && root != null) {
            CheckedTextView circle = root.findViewById(R.id.circle);
            circle.setChecked(entity.isSigned());
            circle.setText(entity.isSigned() ? "" : String.valueOf(entity.getDay()));
            TextView point = root.findViewById(R.id.point);
            point.setText(String.valueOf(entity.getPoint()));
            point.setTextColor(UIUtils.getColor(entity.isSigned() ? R.color.colorPrimary : R.color.text_gray_color));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enter) {
            if (!isSign) {
                sign();
            } else {
                if (mEnterView.isEnabled()) {
                    isClickEnter = true;
                    dismiss();
                }
            }
        } else {
            dismiss();
        }
    }

    private void sign() {
        MissionModule.missionPerform(MissionApi.MissionType.CHECK_IN_DAY, null).subscribe(observer);
    }

    private boolean isClickEnter;
    private MissionApi.AckMissionFinish data;
    @Override
    public void onDismiss(DialogInterface dialog) {
        /*if (data != null) {
            MissionApi.ExtInfo extInfo = data.getExtInfo();
            //更新积分
            if (extInfo != null) {
                EventManager.getInstance().openRedPacket(
                        new RedPacketEvent(data.getScore(), extInfo.getTotalScore(), data.getIsShowAd(),
                                data.getCanDoubleScore(), data.getCode(), MissionApi.MissionType.CHECK_IN_DAY));
            }
        } else if (isClickEnter) {
        }*/
        if(isClickEnter)
            EventManager.getInstance().loadAD(new ADEvent(ADEvent.TYPE_INTERSTITIAL, null));
        super.onDismiss(dialog);
    }

    CommonObserver<MissionApi.AckMissionFinish> observer = new CommonObserver<MissionApi.AckMissionFinish>() {
        @Override
        public void onNext(MissionApi.AckMissionFinish data) {
            SignDialog.this.data = data;
            mExtInfo = data.getExtInfo();
            if (mSignedView != null) {
                mSignedView.setVisibility(View.VISIBLE);
            }
            int checkInDay = mExtInfo.getCheckInDay();
            setDayNum(checkInDay);
            if (mDayContainerView != null && mDayContainerView.getChildCount() > checkInDay - 1) {
                View child = mDayContainerView.getChildAt(checkInDay - 1);
                SignDayEntity entity = mData.get(checkInDay - 1);
                entity.setSigned(true);
                setChildContent(entity, child);
                setDayNum(mExtInfo.getCheckInDay());
                isSign = true;
                mEnterView.setText(R.string.enter);
                EventManager.getInstance().sign(new SignEvent(true, mExtInfo.getCheckInDay()));
            }
        }
    };



}

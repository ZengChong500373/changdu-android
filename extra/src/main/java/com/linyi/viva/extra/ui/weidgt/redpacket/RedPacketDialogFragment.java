package com.linyi.viva.extra.ui.weidgt.redpacket;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.linyi.viva.extra.R;
import com.linyi.viva.extra.entity.event.ADEvent;
import com.linyi.viva.extra.entity.event.RedPacketEvent;
import com.linyi.viva.extra.manager.EventManager;
import com.linyi.viva.extra.ui.weidgt.RollNumView;

import org.linyi.base.utils.UIUtils;

/**
 * Description:
 */

public class RedPacketDialogFragment extends DialogFragment implements View.OnClickListener{

    private static final String EXTRA_KEY = "extra_key";
    private static final String TAG = RedPacketDialogFragment.class.getName();
    private View mView;
    private View ivCoins;
    private View ivOpen;
    private View flClosed;
    private View ivlogo;
    private View llPacket;
    //    private View ivCancel;
    private View ivCover;
    private int mRedPacketResult;
    private RedPacketResp mRedPacket;
    private RollNumView rollNumView;

    public static RedPacketDialogFragment newInstance(RedPacketResp bean) {
        RedPacketDialogFragment fragment = new RedPacketDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_KEY, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null) {
            //无标题
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            //触摸外面不消失
            getDialog().setCanceledOnTouchOutside(false);
            if (getDialog().getWindow() != null) {
                //设置背景色透明
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
        mView = inflater.inflate(R.layout.dialog_red_packet, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupWindow();
        initView(mView);
    }

    private void setupWindow() {
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = screenWidth;
        lp.height = screenHeight;
        getDialog().getWindow().setAttributes(lp);
    }

    protected void initView(View view) {
        ivCoins = view.findViewById(R.id.iv_hb_coins);
        ivOpen = view.findViewById(R.id.iv_hb_open);
        flClosed = view.findViewById(R.id.fl_hb_closed);
        ivlogo = view.findViewById(R.id.iv_hb_logo);
        ivCover = view.findViewById(R.id.iv_hb_cover);
        llPacket = view.findViewById(R.id.ll_packet);
        view.findViewById(R.id.receive).setOnClickListener(this);
        TextView doubleCoinView = view.findViewById(R.id.double_coin);
        doubleCoinView.setText(getString(R.string.double_default, UIUtils.getString(R.string.coin)));
        doubleCoinView.setOnClickListener(this);
        rollNumView = view.findViewById(R.id.roll_num_view);
        mRedPacket = getArguments().getParcelable(EXTRA_KEY);
        boolean withAnimation = true;
        if (mRedPacket != null) {
            RedPacketEvent event = mRedPacket.getEvent();
            withAnimation = mRedPacket.isWithAnimation();
            if (event != null) {
                mRedPacketResult = event.getScore();
                doubleCoinView.setVisibility(event.isCanDoubleScore() ? View.VISIBLE : View.INVISIBLE);
            }
        }
        if (withAnimation) {
            setCameraDistance();
            //View没有测量完毕，无法获取宽高，投递Runnable到消息队列尾部，动画中需要宽高
            getView().post(new Runnable() {
                @Override
                public void run() {
                    openRedPacket();
                }
            });
        } else {
            showRedPacketResult();
        }
    }

    /**
     * 设置视角距离，贴近屏幕（布局Y轴旋转，需改变视角，否则会超出影响视觉体验）
     */
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getContext().getResources().getDisplayMetrics().density * distance;
        flClosed.setCameraDistance(scale);
    }

    /**
     * 直接展示红包数额
     */
    private void showRedPacketResult() {
        ivCoins.setVisibility(View.GONE);
        ivOpen.setVisibility(View.GONE);
        flClosed.setVisibility(View.VISIBLE);
        ivlogo.setVisibility(View.VISIBLE);
        rollNumView.setNumberValue(mRedPacketResult);
    }

    /**
     * 动画展示红包数额，打开红包
     * 执行红包动画
     * 1.金币下落，金额数字滚动
     * 2.关闭红包
     */
    private void openRedPacket() {
        redPacketRun();
        goldCoinDown();
    }

    /**
     * 金额数字滚动
     */
    private void redPacketRun() {
        rollNumView.setNumberValue(mRedPacketResult);
        rollNumView.startRoll();
    }

    /**
     * 金币下落
     */
    private void goldCoinDown() {
        Context context = getContext();
        //初始状态
        ivOpen.setVisibility(View.VISIBLE);
        flClosed.setVisibility(View.GONE);
        ivlogo.setVisibility(View.GONE);
        //金币下落
        ivCoins.setVisibility(View.VISIBLE);
        int openDuration = context.getResources().getInteger(R.integer.red_packet_open_anim_duration);
        float valueToTranslationY = ivCover.getMeasuredHeight();
        ObjectAnimator coinAnim = ObjectAnimator.ofFloat(ivCoins, "translationY", -valueToTranslationY, 0);
        coinAnim.setDuration(openDuration);
//        coinAnim.start();
        //关闭红包
        Animator packetCloseAnim = AnimatorInflater.loadAnimator(context, R.animator.red_packet_close);
        flClosed.setPivotX(0.5f);
        flClosed.setPivotY(0);
        packetCloseAnim.setTarget(flClosed);
        packetCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                ivOpen.setVisibility(View.GONE);
                flClosed.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ivlogo.setVisibility(View.VISIBLE);
            }
        });
//        packetCloseAnim.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(coinAnim).before(packetCloseAnim);
        animatorSet.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void show(FragmentManager manager) {
        showDialog(manager);
    }

    void showDialog(FragmentManager manager) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            Fragment prev = manager.findFragmentByTag(TAG);
            if (prev != null) {
                ft.remove(prev);
            }
            super.show(ft, TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        RedPacketEvent event = mRedPacket.getEvent();
        if (event != null) {
            if (id == R.id.receive) {
                if(mRedPacket != null && event.isShowAD())
                    EventManager.getInstance().loadAD(new ADEvent(ADEvent.TYPE_INTERSTITIAL, event));
            } else if (id == R.id.double_coin) {
                if(mRedPacket != null && event.isShowAD())
                    EventManager.getInstance().loadAD(new ADEvent(ADEvent.TYPE_VIDEO, event));
            }
        }
        dismiss();
    }
}

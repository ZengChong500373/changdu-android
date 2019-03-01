package org.linyi.base.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.linyi.base.R;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.utils.help.CheckHelp;


public abstract class BaseDialog extends DialogFragment {
    protected String TAG = "";
    protected View mRootView;
    protected BaseActivity mActivity;
    private CommonObserver<DialogInterface> dismissListener;

    public BaseDialog() {
        super();
        setStyle(STYLE_NO_TITLE, R.style.CommonDialog);
    }

    public void setOnDismissListener(CommonObserver<DialogInterface> postEvent) {
        this.dismissListener = postEvent;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(dismissListener != null)
            dismissListener.onNext(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getContext()).inflate(setLayout(), container, false);
        initFragment();
        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            mActivity = (BaseActivity) activity;
        }
    }

    private void initFragment() {
        TAG = getClass().getSimpleName();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
    }

    protected void init(Bundle savedInstanceState){
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract int setLayout();


    protected <V extends View> V findViewById(@IdRes int id) {
        return mRootView == null ?  null : (V) (mRootView.findViewById(id));
    }

    protected void setOnClickListener(@IdRes int... resId) {
        if(resId == null || mRootView == null) return;
        for (int id : resId) {
            View view = mRootView.findViewById(id);
            if(view != null)
                view.setOnClickListener(mViewClickListener);
        }
    }

    protected View.OnClickListener mViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(CheckHelp.checkClickTime())
                BaseDialog.this.onClick(v);
        }
    };

    public void onClick(View v) {

    }

}

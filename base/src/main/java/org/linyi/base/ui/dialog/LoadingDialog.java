package org.linyi.base.ui.dialog;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.linyi.base.R;
import org.linyi.base.constant.Key;
import org.linyi.base.entity.params.LoadingDialogEntity;
import org.linyi.base.ui.BaseDialog;
import org.linyi.base.utils.UIUtils;


/**
 *  加载中对话框
 */
public class LoadingDialog extends BaseDialog implements View.OnClickListener {

    LoadingDialogEntity mEntity;

    private TextView mTipsView;
    private View mLoadingContainer;

    public static LoadingDialog newInstance(LoadingDialogEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.PARCELABLE_ENTITY, entity);
        LoadingDialog dialog = new LoadingDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mEntity = bundle.getParcelable(Key.PARCELABLE_ENTITY);
        }
        mTipsView = findViewById(R.id.tips);
        if (mEntity != null) {
            mLoadingContainer = findViewById(R.id.loading_ll);
            String tips = mEntity.getTips();
            setTipsText(tips);

        }
        mRootView.setOnClickListener(this);
        setCancelable(true);
    }

    private void setTipsText(String tips) {
        if (TextUtils.isEmpty(tips)) {
            mTipsView.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mLoadingContainer.setBackground(null);
            } else {
                mLoadingContainer.setBackgroundDrawable(null);
            }
        } else {
            mTipsView.setText(tips);
            mLoadingContainer.setBackgroundResource(R.drawable.shape_loading_bg);
        }
    }

    /**
     * 设置提示语
     * @param tips
     */
    public void setTips(String tips) {
        if (this.mEntity != null) {
            this.mEntity.setTips(tips);
            setEntity(this.mEntity);
        } else {
            setTipsText(tips);
        }
    }

    public void setEntity(LoadingDialogEntity entity) {
        this.mEntity = entity;
        if (entity != null) {
            setTipsText(entity.getTips());
            Bundle bundle = getArguments();
            bundle.putParcelable(Key.PARCELABLE_ENTITY, entity);
            setArguments(bundle);
        }
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_loading;
    }

    @Override
    public void onClick(View v) {
        if (v == mRootView && mEntity != null && mEntity.isCanDismissOutSide()) {
            dismissAllowingStateLoss();
        }
    }
}

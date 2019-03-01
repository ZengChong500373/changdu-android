package org.linyi.base.ui.weidgt;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.linyi.base.R;
import org.linyi.base.entity.params.TempEntity;
import org.linyi.base.utils.LogUtil;

/**
 * @fun: 空试图
 * @developer: rwz
 * @date: 2019/1/30 0030 下午 5:29
 */
public class TempView extends FrameLayout{

    private TempEntity mTempEntity;
    /**
     * 正在加载中
     */
   public static final int STATUS_LOADING = 3538;
    /**
     * 显示空数据
     */
    public static final int STATUS_NULL = 3539;
    /**
     * 显示错误视图
     */
    public static final int STATUS_ERROR = 3540;
    /**
     * 移除空视图
     */
    public static final int STATUS_DISMISS = 3541;

    private View mRootView;
    private View mLoadingContainer;
    private View mTempContainer;
    private ImageView mImgView;
    private TextView mTextView;
    private TextView mReloadView;

    public TempView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TempView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_temp, null, false);
        mLoadingContainer = mRootView.findViewById(R.id.loadingContainer);
        mTempContainer = mRootView.findViewById(R.id.tempContainer);
        mImgView = mRootView.findViewById(R.id.img);
        mTextView = mRootView.findViewById(R.id.text);
        mReloadView = mRootView.findViewById(R.id.reload);
        addView(mRootView);
        setTempEntity(new TempEntity());
    }

    public void setTempEntity(TempEntity tempEntity) {
        if (tempEntity != null) {
            this.mTempEntity = tempEntity;
            int type = tempEntity.getType();
            if (mRootView != null) {
                if (type == STATUS_DISMISS) {
                    mRootView.setVisibility(GONE);
                    return;
                }
                mRootView.setVisibility(VISIBLE);
                boolean isNull = type == STATUS_NULL;
                boolean isError = type == STATUS_ERROR;
                mImgView.setImageDrawable(isError ? tempEntity.getErrorImgRes() : tempEntity.getNullImgRes());
                mTextView.setText(isError ? tempEntity.getErrorTipsStr() : tempEntity.getNullTipsStr());
                mReloadView.setText(isError ? tempEntity.getErrorBtnStr() : tempEntity.getNullBtnStr());
                boolean showErrorBtn = isError && tempEntity.isShowErrorBtn();
                boolean showNullBtn = isNull && tempEntity.isShowErrorBtn();
                mReloadView.setVisibility((showErrorBtn || showNullBtn) ? View.VISIBLE : View.GONE);
                mTempContainer.setVisibility((isNull || isError) ? View.VISIBLE : View.GONE);
                mLoadingContainer.setVisibility(type == STATUS_LOADING ? View.VISIBLE : View.GONE);
            }
        }
    }

    public void setOnRetryListener(View.OnClickListener listener) {
        if(this.mReloadView != null)
            this.mReloadView.setOnClickListener(listener);
    }

    public void setType(int type) {
        LogUtil.d("TempView, setType", "type = " + type);
        if (mTempEntity != null && mTempEntity.getType() != type) {
            mTempEntity.setType(type);
            if(Looper.myLooper() == Looper.getMainLooper()){
                setTempEntity(mTempEntity);
            } else {
                post(new Runnable() {
                    @Override
                    public void run() {
                        setTempEntity(mTempEntity);
                    }
                });
            }
        }
    }



}

package org.linyi.base.ui.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.linyi.base.R;
import org.linyi.base.constant.Key;
import org.linyi.base.entity.params.MsgDialogEntity;
import org.linyi.base.inf.CommBiConsumer;
import org.linyi.base.ui.BaseDialog;


/**
 * 消息对话框
 */
public class MsgDialog extends BaseDialog implements View.OnClickListener {

    private MsgDialogEntity mEntity;

    public static MsgDialog newInstance(MsgDialogEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Key.PARCELABLE_ENTITY,entity);
        MsgDialog dialog = new MsgDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setRequestCode(int requestCode) {
        if (mEntity != null) {
            mEntity.setRequestCode(requestCode);
        }
    }

    public void setEntity(MsgDialogEntity mEntity) {
        this.mEntity = mEntity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mEntity = bundle.getParcelable(Key.PARCELABLE_ENTITY);
        }
        TextView title = findViewById(R.id.title);
        TextView msg = findViewById(R.id.msg);
        TextView cancel = findViewById(R.id.cancel);
        TextView enter = findViewById(R.id.enter);
        if (mEntity != null) {
            boolean isSingleBtn = TextUtils.isEmpty(mEntity.getCancelText());
            cancel.setVisibility(isSingleBtn ? View.GONE : View.VISIBLE);
            cancel.setText(mEntity.getCancelText());
            enter.setText(mEntity.getEnterText());
            title.setText(mEntity.getTitle());
            msg.setText(mEntity.getMsg());
            setCancelable(mEntity.isCancelable());
        }
        cancel.setOnClickListener(this);
        enter.setOnClickListener(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_msg;
    }

    private CommBiConsumer<MsgDialogEntity, Boolean> listener;
    public MsgDialog setListener(CommBiConsumer<MsgDialogEntity, Boolean> listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            onClickDialog(false);
        } else if (v.getId() == R.id.enter) {
            onClickDialog(true);
        }
        dismiss();
    }

    private void onClickDialog(boolean isOk) {
        if (listener != null) {
            try {
                listener.accept(mEntity, isOk);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

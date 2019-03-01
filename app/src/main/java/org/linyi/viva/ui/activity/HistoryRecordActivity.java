package org.linyi.viva.ui.activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


import org.linyi.base.entity.db.HistoryRecordDbEntity;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.utils.ToastUtils;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.HistoryRecordContract;
import org.linyi.viva.mvp.presenter.HistoryRecordContractPresentImpl;
import org.linyi.viva.ui.adapter.HistoryRecordAapter;
import java.util.List;

public class HistoryRecordActivity extends BaseActivity<HistoryRecordContractPresentImpl> implements  HistoryRecordContract.View, View.OnClickListener{
    public HistoryRecordAapter adapter;
    public RecyclerView recycler;
    private LinearLayout ll_empty;
    @Override
    protected int setLayout() {
        return R.layout.activity_history_record;
    }

    @Override
    protected HistoryRecordContractPresentImpl initPresenter() {
        return new HistoryRecordContractPresentImpl(this);
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.tv_all_select).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_delete).setOnClickListener(this);

        ll_empty=findViewById(R.id.ll_empty);
        adapter = new HistoryRecordAapter();
        recycler=findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        adapter.setEmptyListener(new HistoryRecordAapter.EmptyListener() {
            @Override
            public void onEmpty() {
                isEmpty(true);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        presenter.init();
    }

    @Override
    public void onLoadSuccess(List<HistoryRecordDbEntity> list) {
       adapter.setData(list);
    }

    @Override
    public void isEmpty(boolean isEmpty) {
        if (isEmpty){
            ll_empty.setVisibility(View.VISIBLE);
        }else {
            ll_empty.setVisibility(View.GONE);
        }
    }



    @Override
    public void onDeleteSuccess() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_all_select:
                ToastUtils.showLong("tv_all_select");
                break;
            case R.id.tv_cancel:
                ToastUtils.showLong("tv_cancel");
                break;
                case R.id.tv_delete:
                ToastUtils.showLong("tv_delete");
                break;
        }
    }
}

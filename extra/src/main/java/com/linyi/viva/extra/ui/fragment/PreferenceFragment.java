package com.linyi.viva.extra.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.GridView;

import com.api.changdu.proto.Api;
import com.api.changdu.proto.UserApi;
import com.google.protobuf.ProtocolStringList;
import com.linyi.viva.extra.R;
import com.linyi.viva.extra.net.module.UserModule;

import org.linyi.base.entity.params.LoadingDialogEntity;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.mvp.BasePresenter;
import org.linyi.base.ui.LazyFragment;
import org.linyi.base.ui.adapter.lv.AbsSimpleAdapter;
import org.linyi.base.ui.adapter.lv.AbsSimpleViewHolder;
import org.linyi.base.ui.dialog.LoadingDialog;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.help.DialogHelp;
import org.linyi.base.utils.help.TurnHelp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.functions.Action;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/26 0026 上午 9:57
 */
public class PreferenceFragment extends LazyFragment implements AdapterView.OnItemClickListener {

    private GridView mGridView;
    private List<String> mData;
    private AbsSimpleAdapter<String> mAdapter;
    private Set<String> mCheckedData;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_preference;
    }

    @Override
    public void initView() {
        if(mData == null)
            mData = new ArrayList<>();
        mCheckedData = new HashSet<>();
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(getAdapter());
        mGridView.setOnItemClickListener(this);
        setOnClickListener(R.id.enter, R.id.skip);
    }

    private AbsSimpleAdapter getAdapter() {
        if(mAdapter == null)
            mAdapter = new AbsSimpleAdapter<String>(getContext(), mData, R.layout.item_novel_classify) {
                @Override
                public void convert(int position, AbsSimpleViewHolder helper, String item) {
                    helper.setText(R.id.name, item);
                }
            };
        return mAdapter;
    }

    private UserApi.Gender mGender;
    public void setGender(List<UserApi.AckDeviceLogin.FavoriteOption> list, UserApi.Gender mGender) {
        this.mGender = mGender;
        if (list == null || list.isEmpty())
            return;
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if(mCheckedData != null)
            mCheckedData.clear();
        for (UserApi.AckDeviceLogin.FavoriteOption option : list) {
            UserApi.Gender gender = option.getGender();
            if (gender == mGender) {
                mData.clear();
                ProtocolStringList categoryList = option.getCategoryList();
                if (categoryList != null && !categoryList.isEmpty()) {
                    for (String s : categoryList) {
                        mData.add(s);
                    }
                }
                if(mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    List<UserApi.AckDeviceLogin.FavoriteOption> list;
    public void addData(List<UserApi.AckDeviceLogin.FavoriteOption> list) {
        this.list = list;
    }

    @Override
    public void onFirstUserVisible() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.enter) {
            startRead();
        } else if (id == R.id.skip) {
            startRead();
        }
    }

    private void startRead() {
        showLoading();
        UserModule.favorite(mGender, mCheckedData, false)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoading();
                    }
                })
                .subscribe(new CommonObserver<Api.Response>() {
                    @Override
                    public void onNext(Api.Response response) {
                        TurnHelp.main(getContext());
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView ctv = view.findViewById(R.id.name);
        ctv.toggle();
        View flag = view.findViewById(R.id.flag);
        flag.setVisibility(ctv.isChecked() ? View.VISIBLE : View.GONE);
        if (ctv.isChecked()) {
            mCheckedData.add(mData.get(position));
        } else {
            mCheckedData.remove(mData.get(position));
        }
    }

}

package org.linyi.viva.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;
import com.linyi.reader.utils.NovelUtils;


import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.StatusBarUtil2;

import org.linyi.base.utils.UIUtils;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.ReadCatalogContract;
import org.linyi.viva.mvp.presenter.ReadCatalogContractImpl;
import org.linyi.viva.ui.adapter.ReadCatalogAdapter;

import java.util.List;

public class ReadCatalogActivity extends BaseActivity<ReadCatalogContractImpl> implements ReadCatalogContract.View{
    private Toolbar tool_bar;
    private TextView tv_title, tv_sort;

    private RecyclerView recycler_list;
    private TempView tempView;
    private String id, bookName,author,coverImge;
    private ReadCatalogAdapter adapter;
    private View view_line;


    private int position = 0;

    @Override
    protected int setLayout() {
        return R.layout.fragment_read_catalog;
    }

    @Override
    protected void initView() {
        super.initView();
        tool_bar = findViewById(R.id.tool_bar);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title = findViewById(R.id.tv_title);
        tv_sort = findViewById(R.id.tv_sort);
        tv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sortData(id);
            }
        });

        view_line=findViewById(R.id.view_line);
        recycler_list = findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReadCatalogAdapter();
        recycler_list.setAdapter(adapter);
        tempView = findViewById(R.id.tempView);
        tempView.setOnRetryListener(mViewClickListener);
        // 设置toolbar

        showTheme();

    }

    @Override
    public void showLoading() {
        tempView.setType(TempView.STATUS_LOADING);
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        bookName = getIntent().getStringExtra("bookName");
        position = getIntent().getIntExtra("position", 0);
        author = getIntent().getStringExtra("author");
        coverImge = getIntent().getStringExtra("coverImge");
        adapter.setInfos(bookName,author,coverImge);
        presenter.loadData(id);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == com.linyi.viva.extra.R.id.reload && presenter != null) {
            presenter.loadData(id);
        }
    }

    @Override
    protected ReadCatalogContractImpl initPresenter() {
        return new ReadCatalogContractImpl(this);
    }


    @Override
    public void onLoadSuccess(List<BookApi.ChapterMsg> list, int type) {
        adapter.setData(list, type);
        adapter.setSelectedPosition(position);
    }

    @Override
    public void setTemp(int type) {
        if (tempView != null)
            tempView.setType(type);
    }

    @Override
    public List<BookApi.ChapterMsg> getAdapterData() {
        return adapter.getData();
    }

    @Override
    public String getSort() {
        return tv_sort.getText().toString();
    }

    @Override
    public void sortData(int type) {
        if (type == 0) {
            tv_sort.setText(UIUtils.getString(R.string.positive_order));
        } else {
            tv_sort.setText(UIUtils.getString(R.string.inverted_order));
        }
        adapter.setType(type);
        recycler_list.scrollToPosition(adapter.getSetctedPosition());
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        setTemp(TempView.STATUS_DISMISS);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTheme();
        adapter.onResume();
    }

    public void showTheme() {
        if (!NovelUtils.isNightMode()) {
            tool_bar.setBackgroundColor(UIUtils.getColor(R.color.white));
            tv_title.setTextColor(UIUtils.getColor(R.color.text_color));
            tv_sort.setTextColor(UIUtils.getColor(R.color.text_color));
            view_line.setBackgroundColor(UIUtils.getColor(R.color.line_color2));
            StatusBarUtil2.setColor(ReadCatalogActivity.this,UIUtils.getColor(R.color.white),0);
        } else {
            tool_bar.setBackgroundColor(UIUtils.getColor(R.color.read_bg_6));
            tv_title.setTextColor(UIUtils.getColor(R.color.read_font_6));
            tv_sort.setTextColor(UIUtils.getColor(R.color.read_font_6));
            view_line.setBackgroundColor(UIUtils.getColor(R.color.read_font_6));
            StatusBarUtil2.setColor(ReadCatalogActivity.this, UIUtils.getColor(R.color.read_bg_6), 0);
        }
    }
}

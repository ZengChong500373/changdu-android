package org.linyi.viva.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.api.changdu.proto.BookApi;
import org.linyi.base.ui.BaseActivity;
import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.ClassifyContract;
import org.linyi.viva.mvp.presenter.ClassifyPresenterImpl;
import org.linyi.viva.ui.adapter.ClassifyDetailsAdapter;
import org.linyi.viva.ui.adapter.ClassifyTypeAdapter;

/**
* 分类
* */
public class ClassifyActivity extends BaseActivity<ClassifyPresenterImpl> implements ClassifyContract.View {
    Toolbar tool_bar;
    RecyclerView recycler_type ,recycler_detail;
    ClassifyTypeAdapter typeAdapter;
    ClassifyDetailsAdapter detailsAdapter;
    @Override
    protected int setLayout() {
        return R.layout.activity_classify;
    }

    @Override
    protected ClassifyPresenterImpl initPresenter() {
        return new ClassifyPresenterImpl(this);
    }

    @Override
    protected void initView() {
        tool_bar=findViewById(R.id.tool_bar);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recycler_type=findViewById(R.id.recycler_type);
        recycler_type.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        typeAdapter=new ClassifyTypeAdapter();
        recycler_type.setAdapter(typeAdapter);
        typeAdapter.setItemListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                typeAdapter.selected(postion);
                      detailsAdapter.setData(typeAdapter.getData(postion));
            }

            @Override
            public void onItemLongClick(View view, int postion) {

            }
        });
        recycler_detail=findViewById(R.id.recycler_detail);
        recycler_detail.setLayoutManager(new GridLayoutManager(this,2));
        detailsAdapter=new ClassifyDetailsAdapter();
        recycler_detail.setAdapter(detailsAdapter);

        detailsAdapter.setItemListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent=new Intent(ClassifyActivity.this, Classify2Activity.class);
                intent.putExtra("category",detailsAdapter.getName(postion));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int postion) {

            }
        });
    }

    @Override
    protected void initData() {
         presenter.init();
    }

    @Override
    public void onLoadSuccess(BookApi.AckCategoryList data) {
        typeAdapter.setData(data);
        typeAdapter.selected(0);
        if (data!=null&&data.getRankList()!=null&&data.getRankList().size()!=0){
            detailsAdapter.setData(data.getRank(0));
        }

    }
}

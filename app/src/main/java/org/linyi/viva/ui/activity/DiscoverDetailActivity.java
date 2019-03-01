package org.linyi.viva.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.ui.utils.RxRecyclerViewDividerTool;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.CommonContract;
import org.linyi.viva.mvp.presenter.DiscoverDetailPresentImpl;
import org.linyi.viva.ui.adapter.DiscoverDetailAdapter;

public class DiscoverDetailActivity extends BaseActivity<DiscoverDetailPresentImpl> implements CommonContract.View<BookApi.AckBookThemeDetail> {
    public Toolbar tool_bar;
    private TempView tempView;
    public int id;
    public RecyclerView recycler_theme1,recycler_theme2;
    public DiscoverDetailAdapter adapter1,adapter2;
    private TextView tv_title,tv_description,tv_theme_title1,tv_theme_title2;
    private ImageView img_cover;
    private LinearLayout ll_theme1,ll_theme2;
    @Override
    protected int setLayout() {
        return R.layout.activity_discover_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title=findViewById(R.id.tv_title);
        tv_description=findViewById(R.id.tv_description);
        img_cover=findViewById(R.id.img_cover);

        tool_bar = findViewById(R.id.tool_bar);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_theme1=findViewById(R.id.ll_theme1);
        tv_theme_title1=findViewById(R.id.tv_theme_title1);
        recycler_theme1 = findViewById(R.id.recycler_theme1);
        recycler_theme1.setLayoutManager(new LinearLayoutManager(this));
        adapter1=new DiscoverDetailAdapter();
        adapter1.setCurrentType(0);
        recycler_theme1.setAdapter(adapter1);


        ll_theme2=findViewById(R.id.ll_theme2);
        tv_theme_title2=findViewById(R.id.tv_theme_title2);
        recycler_theme2 = findViewById(R.id.recycler_theme2);
        recycler_theme2.setLayoutManager(new GridLayoutManager(this,4));
        recycler_theme2.addItemDecoration(new RxRecyclerViewDividerTool(UIUtils.dp2px(8f)));
        adapter2=new DiscoverDetailAdapter();
        adapter2.setCurrentType(1);
        recycler_theme2.setAdapter(adapter2);

        tempView = findViewById(R.id.tempView);
        tempView.setOnRetryListener(mViewClickListener);

    }

    @Override
    protected void initData() {
        super.initData();
        BookApi.AckBookThemeIntro.Data data= (BookApi.AckBookThemeIntro.Data) getIntent().getSerializableExtra("theme");
        if (data!=null){
            tv_title.setText(data.getTitle());
            tv_description.setText(data.getDescription());
            ImageLoaderUtil.getInstance().loadImage(img_cover,data.getCoverImage());
            presenter.init(data.getId());
        }

    }

    @Override
    protected DiscoverDetailPresentImpl initPresenter() {
        return new DiscoverDetailPresentImpl(this);
    }

    @Override
    public void onLoadSuccess(BookApi.AckBookThemeDetail data) {
      if (data.getTheme(0)!=null){
          ll_theme1.setVisibility(View.VISIBLE);
          tv_theme_title1.setText(data.getTheme(0).getTitle());
          data.getTheme(0).getBookOverViewList();
          adapter1.setData(data.getTheme(0).getBookOverViewList());
      }else {
          ll_theme1.setVisibility(View.GONE);
      }
      if (data.getTheme(1)!=null){
          ll_theme2.setVisibility(View.VISIBLE);
          tv_theme_title2.setText(data.getTheme(1).getTitle());
          adapter2.setData(data.getTheme(1).getBookOverViewList());
      }else {
          ll_theme2.setVisibility(View.GONE);
      }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        setTemp(TempView.STATUS_DISMISS);
    }

    @Override
    public void setTemp(int type) {
        if (tempView != null)
            tempView.setType(type);
    }
}

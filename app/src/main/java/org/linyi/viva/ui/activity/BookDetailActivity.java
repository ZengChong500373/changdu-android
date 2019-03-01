package org.linyi.viva.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.api.changdu.proto.BookApi;

import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.UIUtils;
import org.linyi.base.utils.help.TurnHelp;
import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.ui.utils.RxRecyclerViewDividerTool;
import org.linyi.viva.App;
import org.linyi.viva.R;
import org.linyi.viva.mvp.contract.BookDetailContract;
import org.linyi.viva.mvp.presenter.BookDetailPresentImpl;
import org.linyi.viva.ui.adapter.BookDetailMayLikeAdapter;

public class BookDetailActivity extends BaseActivity<BookDetailPresentImpl> implements BookDetailContract.View {
    public Toolbar tool_bar;
    private ImageView img_cover, img_share;
    private TextView tv_title, tv_book_name, tv_author, tv_status, tv_description, tv_chapter_total, tv_shelf, tv_read;
    private TextView tv_read_total, tv_fans, tv_recommend, tv_words_size;
    public RecyclerView recycler;
    private BookDetailMayLikeAdapter adapter;
    private String id;
    private String bookName;
    private String initStr;
    private TempView tempView;
    private BookApi.AckBookDetail currentBook;
    private NestedScrollView scroll_view;
    @Override
    protected int setLayout() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected BookDetailPresentImpl initPresenter() {
        return new BookDetailPresentImpl(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//必须要写
        if (getIntent()==null)return;
        id = getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(id)){
            presenter.init(id);
        }

    }


    @Override
    protected void initView() {
        tool_bar = findViewById(R.id.tool_bar);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_cover = findViewById(R.id.img_cover);
        img_share = findViewById(R.id.img_share);

        tempView = findViewById(R.id.tempView);
        tempView.setOnRetryListener(mViewClickListener);
        tv_title = findViewById(R.id.tv_title);
        tv_book_name = findViewById(R.id.tv_book_name);
        tv_author = findViewById(R.id.tv_author);
        tv_status = findViewById(R.id.tv_status);
        tv_chapter_total = findViewById(R.id.tv_chapter_total);
        tv_chapter_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailActivity.this, ReadCatalogActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("bookName", currentBook.getBookOverView().getTitle());
                intent.putExtra("position", 0);
                intent.putExtra("author", currentBook.getBookOverView().getAuthorName());
                intent.putExtra("coverImge", currentBook.getBookOverView().getCoverImage());
                startActivity(intent);
            }
        });

        tv_read_total = findViewById(R.id.tv_read_total);
        tv_fans = findViewById(R.id.tv_fans);
        tv_recommend = findViewById(R.id.tv_recommend);
        tv_words_size = findViewById(R.id.tv_words_size);

        tv_description = findViewById(R.id.tv_description);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(this, 4));
        recycler.addItemDecoration(new RxRecyclerViewDividerTool(UIUtils.dp2px(5f)));
        adapter = new BookDetailMayLikeAdapter();
        recycler.setAdapter(adapter);

        tv_shelf = findViewById(R.id.tv_shelf);
        tv_shelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addOrRemoveBook();
            }
        });
        tv_read = findViewById(R.id.tv_read);
        tv_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnHelp.reader(BookDetailActivity.this, id, bookName);
            }
        });
        scroll_view=findViewById(R.id.scroll_view);

    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        presenter.init(id);
    }

    @Override
    public void showLoading() {
        setTemp(TempView.STATUS_LOADING);
    }

    @Override
    public void hideLoading() {
        setTemp(TempView.STATUS_DISMISS);
    }

    @Override
    public void onInitSuccess(BookApi.AckBookDetail data) {
        currentBook = data;
        scroll_view.fling(0);
        scroll_view.scrollTo(0, 0);
        ImageLoaderUtil.getInstance().loadImage(img_cover, data.getBookOverView().getCoverImage());
        bookName = data.getBookOverView().getTitle();
        tv_book_name.setText(bookName);
        tv_title.setText(data.getBookOverView().getCategoryName());
        tv_author.setText(data.getBookOverView().getAuthorName());
        tv_description.setText(data.getBookOverView().getDescription());
        tv_status.setText(data.getBookOverView().getStatus() + "/" + data.getBookOverView().getLastChapterUpdateTime());
//
        String chapterTotal = data.getBookOverView().getLastChapterTitle() + "•更新于" + data.getBookOverView().getLastChapterUpdateTime();
        SpannableString spannableString = new SpannableString(chapterTotal);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        spannableString.setSpan(colorSpan, 0, data.getBookOverView().getLastChapterTitle().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#999999"));
        spannableString.setSpan(colorSpan1, data.getBookOverView().getLastChapterTitle().length() + 1, chapterTotal.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_chapter_total.setText(spannableString);

        String readStr = data.getBookOverView().getReadTotal() + "\n浏览";
        tv_read_total.setText(creatSpan(readStr, data.getBookOverView().getReadTotal() + ""));

        String fansStr = data.getBookOverView().getFansTotal() + "\n在追";
        tv_fans.setText(creatSpan(fansStr, data.getBookOverView().getFansTotal() + ""));

        String recommendStr = data.getBookOverView().getRecommendTotal() + "\n推荐";
        tv_recommend.setText(creatSpan(recommendStr, data.getBookOverView().getRecommendTotal() + ""));

        String wordsStr = data.getBookOverView().getWordsTotal() + "\n字数";
        tv_words_size.setText(creatSpan(wordsStr, data.getBookOverView().getWordsTotal()));
        adapter.setData(data.getSameBookList());
        if (data.getBookOverView().getHasBook()) {
            tv_shelf.setText(UIUtils.getString(R.string.remove_shelf));
            initStr = UIUtils.getString(R.string.remove_shelf);
        } else {
            tv_shelf.setText(UIUtils.getString(R.string.add_shelf));
            initStr = UIUtils.getString(R.string.add_shelf);
        }
    }

    @Override
    public String getId() {
        return id;
    }


    @Override
    public String getShelfText() {
        return tv_shelf.getText().toString();
    }

    @Override
    public void setShelfText(String str,boolean show) {
        tv_shelf.setText(str);

        if (!str.equals(initStr)) {
            App.setIsNeedRefreshShelf(true);
        } else {
            App.setIsNeedRefreshShelf(false);
        }
        if (!show){
            return;
        }
        if (str.equals(UIUtils.getString(R.string.add_shelf))) {
            ToastUtils.showLong("移除成功O(∩_∩)O");
        } else {
            ToastUtils.showLong("加入书架成功O(∩_∩)O");
        }
    }

    @Override
    public void setTemp(int type) {
        if (tempView != null)
            tempView.setType(type);
    }


    public SpannableString creatSpan(String str, String str2) {
        SpannableString spanRead = new SpannableString(str);
        ForegroundColorSpan colorSpanRead = new ForegroundColorSpan(Color.parseColor("#333333"));
        spanRead.setSpan(colorSpanRead, 0, (str2 + "").length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanRead.setSpan(new RelativeSizeSpan(1.2f), 0, (str2 + "").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpanRead2 = new ForegroundColorSpan(Color.parseColor("#999999"));
        spanRead.setSpan(colorSpanRead2, (str2 + "").length() + 1, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanRead.setSpan(new RelativeSizeSpan(0.8f), (str2 + "").length() + 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanRead;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == com.linyi.viva.extra.R.id.reload && presenter != null) {
            presenter.init(id);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume(id);
    }
}

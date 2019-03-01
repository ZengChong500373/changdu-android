package com.linyi.reader.ui;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.api.changdu.proto.BookApi;
import com.linyi.reader.R;
import com.linyi.reader.constant.ReaderRequestCode;
import com.linyi.reader.entity.BlockEntity;
import com.linyi.reader.entity.ReaderConfig;
import com.linyi.reader.entity.event.LoadChapterEvent;
import com.linyi.reader.entity.event.ReaderConfigEvent;
import com.linyi.reader.manager.NovelManager;
import com.linyi.reader.manager.ReaderEventManager;
import com.linyi.reader.net.module.BookModule;
import com.linyi.reader.presenter.ReaderPresenter;
import com.linyi.reader.ui.dialog.MenuDialog;
import com.linyi.reader.ui.dialog.SubscribeDialog;
import com.linyi.reader.utils.ReaderUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.linyi.base.constant.Key;
import org.linyi.base.entity.db.BaseNovelDbEntity;
import org.linyi.base.entity.db.HistoryRecordDbEntity;
import org.linyi.base.entity.event.ActivityResultEvent;
import org.linyi.base.entity.params.MsgDialogEntity;
import org.linyi.base.entity.params.ReaderParams;
import org.linyi.base.inf.CommBiConsumer;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.mvp.module.BookshelfModule;
import org.linyi.base.mvp.module.HistoryRecordModule;
import org.linyi.base.mvp.module.NovelModule;
import org.linyi.base.ui.BaseActivity;
import org.linyi.base.ui.dialog.MsgDialog;
import org.linyi.base.ui.weidgt.TempView;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.PermissionUtils;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.help.DialogHelp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/23 0023 下午 2:35
 */
public class ReaderActivity extends BaseActivity<ReaderPresenter> {

    private static final String TAG = "ReaderActivity";
    private PageWidget mPageWidget;
    private TempView mTempView;
    private PageFactory2 mPageFactory;
    private BaseNovelDbEntity mEntity;
    private int mTurnChapterSort = NovelManager.INVALID_CHAPTER_SORT;//请求到数据后即将要跳转的章节
    private int mTurnPos;//请求到数据后即将要跳转的位置
    private int mInitChapterSort;//初始化需要跳转的章节
    private int mInitPosition;//初始章节中的位置
    private View mEyeCareMask;
    private ReaderParams mReaderParams;

    @Override
    protected int setLayout() {
        return R.layout.activity_reader;
    }

    @Override
    protected ReaderPresenter initPresenter() {
        Intent intent = getIntent();
        mReaderParams = intent.getParcelableExtra(Key.PARCELABLE_ENTITY);
        //从本地获取上次阅读位置
        mEntity = NovelModule.getNovelDbEntity(mReaderParams.getBookID());
        if (mEntity != null) {
            mInitChapterSort = mEntity.getLastChapterSort();
            mInitPosition = (int) mEntity.getPosition();
        } else {
            mEntity = new BaseNovelDbEntity(mReaderParams.getBookID(), null);
        }
        //判断是否需要跳转到某个位置
        if (mReaderParams.getChapterSort() >= 0) {
            mInitChapterSort = mReaderParams.getChapterSort();
            mInitPosition = mReaderParams.getPosition();
        }
        if(mInitChapterSort < 0)
            mInitChapterSort = 0;
        return new ReaderPresenter(this, mReaderParams.getBookID(), mInitChapterSort);
    }

    @Override
    protected void initView() {
        super.initView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mPageWidget = findViewById(R.id.page_widget);
        mTempView = findViewById(R.id.tempView);
        mEyeCareMask = findViewById(R.id.eye_care_mask);
        mTempView.setOnRetryListener(mViewClickListener);
        mPageWidget.setTouchListener(mTouchListener);
        mPageWidget.setOnSizeChangedListener(onSizeChangedListener);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void init() {
        ReaderConfig config = SharePreUtil.getObject(Key.READER_CONFIG, ReaderConfig.class);
        if(config == null)
            config = new ReaderConfig();
        mPageFactory = new PageFactory2(this, mPageWidget, mEntity, config, mReaderParams.getBookName());
        mPageFactory.setPageEvent(presenter.pageEvent);
        //设置屏幕亮度
        ReaderUtils.setWindowBrightness(this, config.getScreenLight());
        initReaderConfig(config);
        NovelManager.getInstance().setChapterEvent(presenter.chapterEvent);
        ReaderEventManager.getInstance().register(this);
        presenter.requestData();
        //保存到历史记录
        presenter.saveHistoryRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPageFactory != null)
            mPageFactory.clear();
        NovelManager.getInstance().clear();
        ReaderEventManager.getInstance().unregister(this);

    }

    private MenuDialog mMenuDialog;

    PageWidget.TouchListener mTouchListener = new PageWidget.TouchListener() {
        @Override
        public void clickBlock(BlockEntity entity) {
            if(entity == null)
                return;
            int type = entity.getType();
            if (type == BlockEntity.MENU) {
                onClickMenu();
            } else if (type == BlockEntity.SUBSCRIBE) {
                onClickSubscribe();
            }
        }

        @Override
        public Boolean prePage() {
            if (mPageFactory != null) {
                mPageFactory.prePage();
                return !mPageFactory.isFirstPage() || NovelManager.getInstance().hasPrevChapter();
            } else {
                return false;
            }
        }

        @Override
        public Boolean nextPage() {
            if (mPageFactory != null) {
                mPageFactory.nextPage();
                return !mPageFactory.isLastPage() || NovelManager.getInstance().hasNextChapter();
            } else {
                return false;
            }
        }

        @Override
        public void cancel() {
            LogUtil.d(TAG, "TouchListener, cancel");
            if(mPageFactory != null)
                mPageFactory.cancelPage();
        }

        @Override
        public void onPageStatic() {

        }
    };

    private void onClickSubscribe() {
        SubscribeDialog dialog = new SubscribeDialog();
        DialogHelp.show(getSupportFragmentManager(), dialog, "SubscribeDialog");
    }

    private void onClickMenu() {
        if (mMenuDialog == null) {
            mMenuDialog = MenuDialog.newInstance(mPageFactory.getConfig());
        }
        if (mMenuDialog.isVisible()) {
            mMenuDialog.dismissAllowingStateLoss();
        } else {
            DialogHelp.show(getSupportFragmentManager(), mMenuDialog, "MenuDialog");
        }
    }

    @Override
    public void themeColorSetting(int color) {
    }

    /** 数据请求完成 **/
    public void onRequestCompleted() {
        if (mTempView != null) {
            mTempView.setType(TempView.STATUS_DISMISS);
        }
        LogUtil.d("onRequestCompleted", "mInitChapterSort = " + mInitChapterSort, "mTurnChapterSort = " + mTurnChapterSort);
        //初始化需要跳转的章节及位置(刚进入阅读器/换源)
        if (mInitChapterSort != NovelManager.INVALID_CHAPTER_SORT && isAlive()) {
            //第一次加载完成, 就开始阅读
            if(mPageFactory != null)
                mPageFactory.startRead(mInitChapterSort, mInitPosition);
            mInitChapterSort = NovelManager.INVALID_CHAPTER_SORT;
            mInitPosition = 0;
        } else if (mTurnChapterSort != NovelManager.INVALID_CHAPTER_SORT && isAlive()) {  //请求到需要跳转的章节
            if (mPageFactory != null && NovelManager.getInstance().isExistChapter(mTurnChapterSort)) {
                mPageFactory.turnChapter(mTurnChapterSort, mTurnPos);
                mTurnChapterSort = NovelManager.INVALID_CHAPTER_SORT;
                mTurnPos = 0;
            }
            hideLoading();
        } else {
            if(mPageFactory != null)
                mPageFactory.onRequestedNewChapter();
            hideLoading();
        }
    }

    public void onRequestError() {
        if(mTempView != null)
            mTempView.setType(TempView.STATUS_ERROR);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.reload) {
            presenter.requestData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionUtils.REQUEST_WRITE_SETTING_PERMISSION) {
            ReaderEventManager.getInstance().onActivityResult(new ActivityResultEvent(requestCode, resultCode));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfigChanged(ReaderConfigEvent event) {
        if (mPageFactory != null && event != null) {
            ReaderConfig config = event.getConfig();
            LogUtil.d(TAG, "onConfigChanged, config = " + config);
            if(config == null)
                return;
            if (event.isReCalcParams()) {
                mPageFactory.modifyReaderConfig(config);
            } else {
                initReaderConfig(config);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadChapter(LoadChapterEvent event) {
        if (event != null && presenter != null) {
            int chapterSort = event.getChapterSort();
            if (chapterSort != NovelManager.INVALID_CHAPTER_SORT) {
                showLoading();
                Set<Integer> set = new HashSet<>();
                set.add(chapterSort);
                presenter.requestChapter(set, true);
            }
        }
    }

    private void initReaderConfig(ReaderConfig config) {
        if(config != null){
            if(mEyeCareMask != null)
                mEyeCareMask.setVisibility(config.isOpenEyeCareMode() ? View.VISIBLE : View.GONE);
            if(mPageWidget != null)
                mPageWidget.setPageMode(config.getPageMode());
            if (mPageFactory != null) {
                mPageFactory.modifyBookBg(config.getSkinType());
                if (config.isOpenAutoRead()) {
                    mPageFactory.startAutoRead();
                } else {
                    mPageFactory.stopAutoRead();
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ReaderParams params = intent.getParcelableExtra(Key.PARCELABLE_ENTITY);
        if(params == null)
            return;
        int chapterSort = params.getChapterSort();
        if(chapterSort < 0)
            chapterSort = NovelManager.INVALID_CHAPTER_SORT;
        int position = params.getChapterSort();
        LogUtil.d(TAG, "onNewIntent", "chapterSort = " + chapterSort, "position = " + position);
        if (chapterSort != NovelManager.INVALID_CHAPTER_SORT && mPageFactory != null) {
            boolean result = mPageFactory.turnChapter(chapterSort, position);
            LogUtil.d(TAG, "onNewIntent", "result=  " + result);
            if (!result) {
                showLoading();
                mTurnChapterSort = chapterSort;
                mTurnPos = position;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (presenter != null && !presenter.isAddBookshelf()) {
            MsgDialog dialog = MsgDialog.newInstance(new MsgDialogEntity(getString(R.string.add_bookshelf),
                    getString(R.string.add_bookshelf_detail), ReaderRequestCode.ADD_BOOKSHELF));
            dialog.setListener(mAddBookshelfListener);
            DialogHelp.show(getSupportFragmentManager(), dialog, "MsgDialog");
        } else {
            super.onBackPressed();
        }
    }

    private CommBiConsumer<MsgDialogEntity, Boolean> mAddBookshelfListener = new CommBiConsumer<MsgDialogEntity, Boolean>() {
        @Override
        public void accept(MsgDialogEntity entity, Boolean result) throws Exception {
            if (result && mReaderParams != null) {
                BookModule.managerBookshelf(mReaderParams.getBookID(), true).subscribe(mAddBookshelfObserver);
            } else {
                finish();
            }
        }
    };

    private CommonObserver mAddBookshelfObserver = new CommonObserver<BookApi.AckBookShelf>() {
        @Override
        public void onNext(BookApi.AckBookShelf ackBookShelf) {
            List<BookApi.BookOverView> list = ackBookShelf.getBookOverViewList();
            if (list != null && list.size() > 0) {
                BookshelfModule.save(list.get(0), false);
                finish();
            }
        }
    };

    /**
     * 当尺寸变化的时候
     */
    private PageWidget.OnSizeChangedListener onSizeChangedListener = new PageWidget.OnSizeChangedListener() {
        @Override
        public void onSizeChanged() {
            init();
        }
    };


}

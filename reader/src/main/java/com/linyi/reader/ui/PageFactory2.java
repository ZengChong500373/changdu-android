package com.linyi.reader.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Window;

import com.linyi.reader.R;
import com.linyi.reader.ReaderConstant;
import com.linyi.reader.entity.BlockEntity;
import com.linyi.reader.entity.ReaderConfig;
import com.linyi.reader.entity.TRPage;
import com.linyi.reader.manager.ChapterManager;
import com.linyi.reader.manager.NovelManager;
import com.linyi.reader.net.module.BookModule;

import org.linyi.base.VivaSdk;
import org.linyi.base.constant.ServerConstant;
import org.linyi.base.entity.db.BaseNovelDbEntity;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.ToastUtils;
import org.linyi.base.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class PageFactory2 {
    private static final String TAG = "PageFactory2";
    private static final long PERIOD_TIME = 5000;
    //章节收尾标识
    private final String CHAPTER_FLAG = "$$";
    //页面宽
    private int mWidth;
    //页面高
    private int mHeight;
    //时间格式
    private SimpleDateFormat sdf;
    //时间
    private String date;
    // 左右与边缘的距离
    private float measureMarginWidth ;
    //文字画笔
    private Paint mPaint;
    //电池画笔
    private TextPaint mChapterPaint;
    //文字颜色
    private int mTextColor = Color.rgb(50, 65, 78);
    // 绘制内容的宽
    private float mVisibleHeight;
    // 绘制内容的宽
    private float mVisibleWidth;
    // 每页最多可以显示的行数
    private int mLineCount;
    //电池画笔
    private Paint mBatteryPaint;
    //背景图片
    private Bitmap m_book_bg = null;
    private Intent batteryInfoIntent;
    //电池电量百分比
    private float mBatteryPercentage;
    //电池外边框
    private RectF rect1 = new RectF();
    //电池内边框
    private RectF rect2 = new RectF();
    //书本widget
    private PageWidget mBookPageWidget;
    //现在的进度
    private float currentProgress;
    //当前电量
    private int level = 0;
    private NovelManager mNovelManager;
    private PageEvent mPageEvent;
    private TRPage currentPage;
    private TRPage cancelPage;
    //请求到数据后是否跳转到最后一页
    private boolean isTurnLastPageIfRequestedData;

    private boolean isShowBattery = true;

    private static Status mStatus = Status.STOP;
    private double mLineHeight; //行高
    private ReaderConfig mConfig;

    public enum Status {
        STOP,            //正在初始化数据/停止向前向后翻页
        NORMAL,          //正常阅读
        FAIL,            //加载失败
        STOP_PREV,       //停止向前翻页
        STOP_NEXT        //停止向后翻页
    }

    public PageFactory2(Context context,PageWidget pageWidget, BaseNovelDbEntity entity,
                        ReaderConfig config, String bookName) {
        mConfig = config;
        mBookPageWidget = pageWidget;
        mNovelManager = NovelManager.getInstance().createNovel(entity, bookName);
//        pageWidget.setOnSizeChangedListener(onSizeChangedListener);
        sdf = new SimpleDateFormat("HH:mm");//HH:mm为24小时制,hh:mm为12小时制
        date = sdf.format(new Date());
        Typeface typeface = null;
        if (config.getTypeface() == ReaderConstant.Typeface.COMPLEX) {
            typeface = Typeface.createFromAsset(VivaSdk.getContext().getAssets(), "jing_dian_song_ti_fan.ttf");
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        mPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        mPaint.setTextSize(config.getFontSize());// 字体大小
        mPaint.setTypeface(typeface);
        mPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        mChapterPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);// 画笔
//        mChapterPaint.setTextAlign(Paint.Align.CENTER);// 左对齐
        mChapterPaint.setTextSize(config.getChapterFontSize());// 字体大小
        mChapterPaint.setTypeface(typeface);
        mChapterPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        mBatteryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBatteryPaint.setTextSize(mConfig.getBatteryFontSize());
//        mBatteryPaint.setTypeface(typeface);
        mBatteryPaint.setTextAlign(Paint.Align.LEFT);
        batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED)) ;//注册广播,随时获取到电池电量信息

        initSizeParams();
        //设置背景
        setBookBg(mConfig.getSkinType());
        //小米SE
        //  {   fontSize=44, batteryFontSize=35, chapterFontSize=66, batteryWidth=55, batteryHeight=27, batteryLineSize=2,
        //      marginHeight=44, marginWidth=44, statusMarginBottom=68, lineSpace=27, paragraphSpace=55, skinType=2}
        //      mWidth = 1080,mHeight = 2028
        //      mVisibleWidth = 992.0,mVisibleHeight = 1931.2,
        //      measureMarginWidth = 56.0,mLineHeight = 59.0,mLineCount = 22

        //模拟器
        //      width = 1080,height = 1920
        //      fontSize=46, batteryFontSize=37, chapterFontSize=69, batteryWidth=57, batteryHeight=28, batteryLineSize=2,
        //      marginHeight=46, marginWidth=46, statusMarginBottom=72, lineSpace=28, paragraphSpace=57, skinType=2
        //      mWidth = 1080,mHeight = 1920
        //      mVisibleWidth = 988.0,mVisibleHeight = 1728.0
        //      measureMarginWidth = 57.0,mLineHeight = 62.0,mLineCount = 19

        //{fontSize=46, batteryFontSize=37, chapterFontSize=69, batteryWidth=57, batteryHeight=28, batteryLineSize=2, marginHeight=46, marginWidth=46, statusMarginBottom=72, lineSpace=28, chapterSpace=28, paragraphSpace=56, skinType=0, isOpenEyeCareMode=false, isOpenAutoRead=false, pageMode=0, skinType=0}
        //mWidth = 1080,mHeight = 2150 (原始2280)
        //2006.0
        //mVisibleWidth = 988.0,mVisibleHeight = 1958.0,
        //measureMarginWidth = 46.0,mLineHeight = 54.0,mLineCount = 23
        //baseline = 2092.4

        //fontSize=42, batteryFontSize=34, chapterFontSize=63, batteryWidth=53, batteryHeight=26, batteryLineSize=2, marginHeight=42, marginWidth=42, statusMarginBottom=66, lineSpace=26, chapterSpace=26, paragraphSpace=52, skinType=0, isOpenEyeCareMode=false, isOpenAutoRead=false, pageMode=0, skinType=0
        //mWidth = 1080,mHeight = 2010
        //mVisibleWidth = 996.0,mVisibleHeight = 1834.0,
        //measureMarginWidth = 42.0,mLineHeight = 50.0,mLineCount = 24
        LogUtil.d(TAG, config);
        LogUtil.d(TAG, "\n mWidth = " + mWidth, "mHeight = " +  mHeight + "\n mVisibleWidth = " + mVisibleWidth, "mVisibleHeight = " + mVisibleHeight,
                "\n measureMarginWidth = " + measureMarginWidth, "mLineHeight = " + mLineHeight, "mLineCount = " + mLineCount);
        //statusHeight = 60
    }

    /**
     * 初始化尺寸参数
     */
    private void initSizeParams() {
        setupScreenSize();
        setupVisibleHeight();
        measureMarginWidth();
        calcLineOfPageCount();
    }

    public ReaderConfig getConfig() {
        return mConfig;
    }

    private void measureMarginWidth(){
        float wordWidth = mPaint.measureText("\u3000");
        float width = mVisibleWidth % wordWidth;
        measureMarginWidth = mConfig.getMarginWidth() + width / 2;
        mVisibleWidth = mWidth - measureMarginWidth * 2;
    }

    /** 设置屏幕宽高 **/
    private void setupScreenSize() {
        if (mBookPageWidget != null) {
            mWidth = mBookPageWidget.getScreenWidth();
            mHeight = mBookPageWidget.getScreenHeight();
        }
    }

    /** 设置屏幕可见高度 **/
    private void setupVisibleHeight() {
        if(mConfig == null)
            return;
        if(isShowBattery)
            mVisibleHeight = mHeight - mConfig.getMarginWidth() * 2 - mConfig.getStatusMarginBottom() - mConfig.getBatteryHeight();
        else
            mVisibleHeight = mHeight - mConfig.getMarginHeight() * 2f;
    }

    /** 计算一页可以展示的行数 **/
    private void calcLineOfPageCount(){
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        mLineHeight = Math.ceil(fm.descent - fm.ascent);
        mLineCount = (int) (mVisibleHeight / (mLineHeight + mConfig.getLineSpace()));// 可显示的行数
    }

    public void onDraw(Bitmap bitmap, TRPage trPage) {
        if (trPage == null || bitmap == null) {
            LogUtil.e("onDraw, trPage = " + trPage, "bitmap = " + bitmap);
            return;
        }
        int marginHeight = mConfig.getMarginHeight();
        List<String> lines = trPage.getLines();
        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
        if (lines != null && lines.size() > 0) {
            mPaint.setTextSize(mConfig.getFontSize());
            mPaint.setColor(getTextColor());
            float y = marginHeight;
            boolean isNewParagraph;
            ChapterManager manager = NovelManager.getInstance().getChapterManager(trPage.getChapterSort());
            //是否支付的章节
            boolean isPayChapter = manager != null && manager.getPayType() == ServerConstant.PayType.PAY;
            boolean initChapterSuccess = manager.isInitChapterSuccess();
            for (String strLine : lines) {
                //表示章节名
                if (trPage.isFirstPage() && strLine.startsWith(CHAPTER_FLAG) && strLine.endsWith(CHAPTER_FLAG)) {
                    strLine = strLine.substring(CHAPTER_FLAG.length(), strLine.length() - CHAPTER_FLAG.length());
                    //支持换行绘制章节标题
                    StaticLayout layout = getChapterNameLayout(strLine);
                    c.save();
                    c.translate(measureMarginWidth, marginHeight);
                    layout.draw(c);
                    c.restore();
                    y += layout.getHeight() + mConfig.getChapterSpace() * 2;
                    if (lines.size() == 1 && !initChapterSuccess) {
                        if (isPayChapter) { //绘制订阅页面
                            drawPay(c);
                        } else { //绘制加载中页面
                            drawLoading(c);
                        }
                        break;
                    } else if(mBookPageWidget != null){
                        mBookPageWidget.setSubscribeBtnRect(null);
                    }
                } else {//普通行
                    //判断是否分段，加入段间距
                    isNewParagraph = strLine != null && strLine.endsWith("\n");
                    if (isNewParagraph) {
                        strLine = strLine.replaceAll("\n", "");
                    }
                    y += mLineHeight;
                    c.drawText(strLine, measureMarginWidth, y, mPaint);
                    y +=  mConfig.getLineSpace() + (isNewParagraph ? mConfig.getParagraphSpace() : 0);
                }
            }
        }
        //是否绘制 进度及时间
        if (isShowBattery) {
            drawPageHeadAndFoot(c);
        }
        if(mBookPageWidget != null)
            mBookPageWidget.postInvalidate();
    }

    private void drawLoading(Canvas c) {
        mPaint.setTextSize(UIUtils.getDimension(R.dimen.text_16));
        //提示文本
        String text = UIUtils.getString(R.string.loading);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float wordHeight = fm.descent - fm.ascent;
        float wordWidth = mPaint.measureText(text);
        c.drawText(text, (mWidth - wordWidth) /2 ,(mHeight - wordHeight) / 2, mPaint);
    }

    private void drawPay(Canvas c) {
        mPaint.setColor(UIUtils.getColor(R.color.colorPrimary));
        mPaint.setTextSize(UIUtils.getDimension(R.dimen.text_16));
        //提示文本
        String text = UIUtils.getString(R.string.not_subscribe_curr_chapter);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float wordHeight = fm.descent - fm.ascent;
        float wordWidth = mPaint.measureText(text);
        float totalHeight = wordHeight + UIUtils.getDimension(R.dimen.v_93);
        float topY = (mHeight - totalHeight) / 2;
        c.drawText(text, (mWidth - wordWidth) / 2, topY + wordHeight, mPaint);
        //按钮
        float btnHeight = UIUtils.getDimension(R.dimen.v_45);
        float margin = UIUtils.getDimension(R.dimen.h_68);
        float bottom = mHeight / 2 + totalHeight / 2;
        BlockEntity rectF = mBookPageWidget == null ? null : mBookPageWidget.getSubscribeBtnRect();
        if (rectF == null) {
            rectF = new BlockEntity(margin, bottom - btnHeight, mWidth - margin, bottom, BlockEntity.SUBSCRIBE);
            mBookPageWidget.setSubscribeBtnRect(rectF);
        }
        float r = UIUtils.getDimension(R.dimen.semicircle);
        c.drawRoundRect(rectF, r, r, mPaint);
        //绘制按钮上的文本
        mPaint.setColor(UIUtils.getColor(R.color.white));
        text = UIUtils.getString(R.string.subscribe);
        wordWidth = mPaint.measureText(text);
        float baseline = rectF.top + (rectF.height() - fm.ascent - fm.descent) / 2;
        c.drawText(text, (mWidth - wordWidth) / 2 , baseline, mPaint);
    }

    /** 绘制章节名 **/
    private void drawChapterName(TRPage trPage, Canvas c) {
        CharSequence chapterName = NovelManager.getInstance().getCurrChapterName();
        if (!TextUtils.isEmpty(chapterName)) {
            mChapterPaint.setColor(getTextColor());
            //支持换行绘制章节标题
            StaticLayout layout = new StaticLayout(chapterName, mChapterPaint, (int)mVisibleWidth,
                    Layout.Alignment.ALIGN_CENTER, 1.5F, 0, true);
            int height = layout.getHeight();
            c.save();
            float y = (mVisibleHeight - height) / 2;
            c.translate(measureMarginWidth, y);
            layout.draw(c);
            c.restore();
        }
    }

    /** 绘制页眉页脚 **/
    private void drawPageHeadAndFoot(Canvas c) {
        mBatteryPaint.setColor(getTextColor());
        // 画电池
        level = batteryInfoIntent.getIntExtra( "level" , 0 );
        int scale = batteryInfoIntent.getIntExtra("scale", 100);
        mBatteryPercentage = (float) level / scale;
        float rect1Left = mConfig.getMarginWidth();//电池外框left位置
        //画电池外框
        float width = mConfig.getBatteryWidth();
        float height = mConfig.getBatteryHeight();
        //底部控件基础性(0.8折减是为了与上面文字保持一点距离)
        float baseline = mHeight - mConfig.getStatusMarginBottom() * 0.8f;
        LogUtil.d(TAG, "baseline = " + baseline);
        rect1.set(rect1Left, baseline - height,rect1Left + width, baseline);
        int borderWidth = mConfig.getBatteryLineSize();
        rect2.set(rect1Left + borderWidth, baseline - height + borderWidth, rect1Left + width - borderWidth, baseline - borderWidth);
        c.save();
        c.clipRect(rect2, Region.Op.DIFFERENCE);
        c.drawRect(rect1, mBatteryPaint);
        c.restore();
        //画电量部分
        rect2.left += borderWidth;
        rect2.right -= borderWidth;
        rect2.right = rect2.left + rect2.width() * mBatteryPercentage;
        rect2.top += borderWidth;
        rect2.bottom -= borderWidth;
        c.drawRect(rect2, mBatteryPaint);
        //画电池头
        int poleHeight = mConfig.getBatteryHeight() / 2;
        rect2.left = rect1.right;
        rect2.top = rect2.top + poleHeight / 4;
        rect2.right = rect1.right + borderWidth * 2;
        rect2.bottom = rect2.bottom - poleHeight/4;
        c.drawRect(rect2, mBatteryPaint);

        //画进度及时间
//        int dateWith = (int) (mBatteryPaint.measureText(date) + borderWidth);//时间宽度
        //绘制进度
//        String strPercent = df.format(fPercent * 100) + "%";//进度文字
//        int nPercentWidth = (int) mBatteryPaint.measureText("999.9%") + 1;  //Paint.measureText直接返回參數字串所佔用的寬度
//        c.drawText(strPercent, mWidth - nPercentWidth, baseline, mBatteryPaint);//x y为坐标值
        //绘制时间
        int marginWidth = mConfig.getMarginWidth();
        float timeLeft = marginWidth + rect1.width() + UIUtils.getDimension(R.dimen.h_6);
        c.drawText(date, timeLeft , baseline, mBatteryPaint);
        //绘制章节位置
        String numOfChapter = String.format(UIUtils.getString(R.string.num_of_chapter_default), getCurrentCharter() + 1, getChapterCount());
        float numOfChapterWidth = mBatteryPaint.measureText(numOfChapter);
        c.drawText(numOfChapter, (mWidth - numOfChapterWidth) / 2, baseline, mBatteryPaint);
        //绘制当前页位置
        ChapterManager manager = NovelManager.getInstance().getCurrChapterManager();
        int pageCount = manager == null ? 0 : manager.getPageCount();
        String numOfPage = String.format(UIUtils.getString(R.string.num_of_page_default), currentPage.getPage() + 1, pageCount);
        float numOfPageWidth = mBatteryPaint.measureText(numOfPage);
        c.drawText(numOfPage, mWidth - numOfPageWidth - marginWidth, baseline, mBatteryPaint);//x y为坐标值
        //画书名
        /*c.drawText(CommUtils.subString(bookName,12), marginWidth ,statusMarginBottom + mBatteryFontSize, mBatteryPaint);
        //画章节名
        if (mNovelManager.getChapterCount() > 0) {
            String charterName = CommUtils.subString(mNovelManager.getCurrChapterName(), 12);
            int nChapterWidth = (int) mBatteryPaint.measureText(charterName) + 1;
            c.drawText(charterName, mWidth - marginWidth - nChapterWidth, statusMarginBottom  + mBatteryFontSize, mBatteryPaint);
        }*/
    }

    /**
     * 向前翻页
     * @return 当前页是否章节头
     */
    public boolean prePage(){
        if(currentPage == null)
            return false;
        isTurnLastPageIfRequestedData = false;
        if (currentPage.isFirstPage()) {
            if (NovelManager.getInstance().isFirstChapter()) {
                ToastUtils.getInstance().showShortSingle("已经是第一页了");
                setStatus(Status.STOP_PREV);
                return false;
            } else {
                isTurnLastPageIfRequestedData = !mNovelManager.turnPrevChapter();
                initChapter();
            }
        }
        setStatus(Status.NORMAL);
        LogUtil.d(TAG, "prePage", "CurrChapterSort = " + mNovelManager.getCurrChapterSort());
        cancelPage = currentPage;
        //绘制的是当前显示的静态页面(当前页)
        onDraw(mBookPageWidget.getCurPage(), currentPage);
        //本章最后一页
        int page;
        if (this.currentPage.isFirstPage()) {//如果是第一页，需要查找当前章节的最后一页
            ChapterManager manager = NovelManager.getInstance().getCurrChapterManager();
            page = manager == null ? 0 : manager.getPageCount() - 1;
        } else {
            page = this.currentPage.getPage() - 1;
        }
        TRPage currentPage = getTRPage(page);
        if(currentPage == null)
            return false;
        this.currentPage = currentPage;
        //绘制的是拖动的上一页面(目标页)
        onDraw(mBookPageWidget.getNextPage(), currentPage);
        updateProgress();
        return true;
    }

    //向后翻页
    public boolean nextPage(){
        if(currentPage == null)
            return false;
        isTurnLastPageIfRequestedData = false;
        boolean isLastPage = currentPage.isLastPage();
        if (isLastPage) {
            if (NovelManager.getInstance().isLastChapter()) {
                ToastUtils.getInstance().showShortSingle("已经是最后一页了");
                setStatus(Status.STOP_NEXT);
                stopAutoRead();
                return false;
            } else {
                mNovelManager.turnNextChapter();
                initChapter();
            }
        }
        setStatus(Status.NORMAL);
        cancelPage = currentPage;
        //绘制的是当前显示的拖动页面(当前页)
        onDraw(mBookPageWidget.getCurPage(), currentPage);
        TRPage currPage = getTRPage(isLastPage ? 0 : this.currentPage.getPage() + 1);
        LogUtil.d(TAG, "nextPage", "nextPage = " + currPage.getPage(),
                "currPage = " + this.currentPage.getPage(), "isLastPage = " + isLastPage);
        if(currPage == null)
            return false;
        this.currentPage = currPage;
        //绘制的是即将显示的下一静态页面(目标页)
        onDraw(mBookPageWidget.getNextPage(), this.currentPage);
        updateProgress();
        return true;
    }

    private void updateProgress() {
        currentProgress = mNovelManager.getProgress();//进度
        if (mPageEvent != null){
            mPageEvent.changeProgress(currentProgress, currentPage == null ? 0 : currentPage.getPage());
        }
    }

    //取消翻页
    public void cancelPage(){
        currentPage = cancelPage;
    }

    /**
     * 开始阅读
     */
    public void startRead(int chapterSort, int position){
        PageFactory2.mStatus = PageFactory2.Status.NORMAL;
        boolean result = turnChapter(chapterSort, position);
        LogUtil.d(TAG, "startRead", "drawCurrentPage = " + currentPage, "chapterSort = " + chapterSort, "position = " + position, "result = " + result);
    }

    public TRPage getPageForBegin(long begin){
        ChapterManager manager = NovelManager.getInstance().getCurrChapterManager();
        if (manager != null) {
            int page = manager.getPageIndex((int) begin);
            return getTRPage(page);
        }
        LogUtil.d(TAG, "ChapterManager is null", "CurrChapterSort = " + NovelManager.getInstance().getCurrChapterSort());
        return null;
    }

    private void initChapter() {
        initSizeParams();
        NovelManager.getInstance().setPosition(-1);
        List<Integer> pageBeginPosList = new ArrayList<>();
        List<String> nextLines;
        int page = 0;
        do {
            pageBeginPosList.add(NovelManager.getInstance().getPosition());
            nextLines = getPageLines(page++);
        } while (nextLines != null && !nextLines.isEmpty());
        ChapterManager manager = NovelManager.getInstance().getCurrChapterManager();
        if (manager != null) {
            int size = pageBeginPosList.size();
            if (size > 0) {//移出最后一个
                pageBeginPosList.remove(size - 1);
            }
            manager.setPageBeginPosList(pageBeginPosList);
            LogUtil.d(TAG, "pageBeginPosList = " + pageBeginPosList);
        } else {
            LogUtil.e(TAG, "initChapter, manager is null");
        }
        LogUtil.d(TAG, "分页完成", manager != null);
    }

    /**
     * 根据页数获取页面内容
     */
    private TRPage getTRPage(int page) {
        LogUtil.d(TAG, "getTRPage", "page = " + page);
        ChapterManager manager = NovelManager.getInstance().getCurrChapterManager();
        if (manager != null) {
            //改变字体/行距等情况可能造成page越界
            if (page >= manager.getPageCount())
                page = manager.getPageCount() - 1;
            if(page < 0)
                page = 0;
            int pos = manager.getPageBeginPos(page);
            NovelManager.getInstance().setPosition(pos);
            List<String> nextLines = getPageLines(page);
            return new TRPage(page, nextLines, manager.getChapterSort());
        }
        LogUtil.d(TAG, "ChapterManager is null", "CurrChapterSort = " + NovelManager.getInstance().getCurrChapterSort());
        return null;
    }

    /** 获取下一页的所有行 **/
    public List<String> getPageLines(int page){
        List<String> lines = new ArrayList<>();
        if(mConfig == null)
            return lines;
        mPaint.setTextSize(mConfig.getFontSize());
        int paragraphSpace = mConfig.getParagraphSpace();
        double lineHeightAndSpace = mLineHeight + mConfig.getLineSpace();
        float width = 0;
        float height = 0;
        if (page == 0) {
            //添加一个章节行
            String chapterName = NovelManager.getInstance().getCurrChapterName();
            if (!TextUtils.isEmpty(chapterName)) {
                height = getChapterNameLayout(chapterName).getHeight() + mConfig.getChapterSpace() * 2;
                lines.add(CHAPTER_FLAG + chapterName + CHAPTER_FLAG);
            }
        }
        StringBuilder line = new StringBuilder();
        //while只进行判断下一个字符是否存在，并不改变指针位置(即下一次next获取的值不变)
        while (mNovelManager.next(true) != -1){
            String word = String.valueOf((char) mNovelManager.next(false));
            //判断是否换行
            if (("\n").equals(word)){
//                mNovelManager.next(false);
                if (line.length() > 0){
                    //添加换行符，是为了绘制的时候添加段间距
                    line.append("\n");
                    lines.add(line.toString());
                    line.setLength(0);
                    width = 0;
                    height += lineHeightAndSpace + paragraphSpace;
                    //文字与段落空隙超过页面高度中断
//                    LogUtil.d("getPageLines, 分段", "size = " + lines.size(), "height = " + height);
                    if (lines.size() == mLineCount || height >= mVisibleHeight - lineHeightAndSpace){
//                        LogUtil.d("getPageLines, 【分行完成】", "size = " + lines.size(), "height = " + height, mVisibleHeight - lineHeightAndSpace);
                        break;
                    }
                }
            }else {
                float charWidth = mPaint.measureText(word);
                width += charWidth;
                line.append(word);
                if (width > mVisibleWidth - charWidth) {
                    lines.add(line.toString());
                    width = 0;
                    line.setLength(0);
                    height += lineHeightAndSpace;
//                    LogUtil.d("getPageLines, 分行", "size = " + lines.size(), "height = " + height);
                    //文字与段落空隙超过页面高度中断
                    if (lines.size() == mLineCount || height >= mVisibleHeight - lineHeightAndSpace){
                        //LogUtil.d("getPageLines, 【分行完成2】", "size = " + lines.size(), "height = " + height);
                        break;
                    }
                }
            }
        }
        //每章最后一行while中断时
        if (line.length() > 0 && lines.size() < mLineCount){
            lines.add(line.toString());
        }
        return lines;
    }

    /**
     * @return 获取一个章节标题的布局
     */
    private StaticLayout getChapterNameLayout(String text) {
        mChapterPaint.setColor(mTextColor);
        StaticLayout layout = new StaticLayout(text, mChapterPaint, (int)mVisibleWidth,
                Layout.Alignment.ALIGN_CENTER, 1.5F, 0, true);
        return layout;
    }

    /** 获取当前章节序号 **/
    public int getCurrentCharter(){
        return mNovelManager.getCurrChapterSort();
    }

    /** 获取总章节数 **/
    public int getChapterCount(){
        return mNovelManager.getChapterCount();
    }

    //绘制当前页面
    public void drawCurrentPage(Boolean updateChapter){
        if (mBookPageWidget != null) {
            onDraw(mBookPageWidget.getCurPage(),currentPage);
            onDraw(mBookPageWidget.getNextPage(),currentPage);
        }
    }

    //更新电量
    public void updateBattery(int mLevel){
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
            if (level != mLevel) {
                level = mLevel;
                drawCurrentPage(false);
            }
        }
    }

    public void updateTime(){
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
            String mDate = sdf.format(new java.util.Date());
            if (date != mDate) {
                date = mDate;
                drawCurrentPage(false);
            }
        }
    }

    //改变进度
    public void changeChapter(long begin){
        currentPage = getPageForBegin(begin);
        drawCurrentPage(true);
        //更新进度
        updateProgress();
    }

    /**
     * 跳转该章节
     * @param chapterSort 加载的序号
     * @param position 页首在章节位置， 若 <= 0则无效
     */
    public boolean turnChapter(int chapterSort, int position) {
        isTurnLastPageIfRequestedData = false;
        boolean result = mNovelManager != null && mNovelManager.turnChapter(chapterSort);
        LogUtil.d(TAG, "turnChapter", "chapterSort = " + chapterSort, "position = " + position,
                "result = " + result);
        if (result) {
            initChapter();
            mStatus = Status.NORMAL;
            currentPage = getPageForBegin(position);
            if(currentPage == null)
                currentPage = getPageForBegin(0);
            drawCurrentPage(true);
            //更新进度
            updateProgress();
        }
        return result;
    }

    public void modifyReaderConfig(ReaderConfig config) {
        if (config != null) {
            int configTypeface = config.getTypeface();
            Typeface typeface = null;
            if (configTypeface == ReaderConstant.Typeface.COMPLEX) {
                typeface = Typeface.createFromAsset(VivaSdk.getContext().getAssets(), "jing_dian_song_ti_fan.ttf");
            }
            mPaint.setTypeface(typeface);
            mChapterPaint.setTypeface(typeface);
            this.mConfig = config;
            mPaint.setTextSize(mConfig.getFontSize());
            initChapter();
            currentPage = getTRPage(currentPage == null ? 0 : currentPage.getPage());
            drawCurrentPage(true);
            //更新进度
            updateProgress();
        }
    }

    //改变背景
    public void modifyBookBg(int type){
        if (mConfig != null) {
            setBookBg(type);
            drawCurrentPage(false);
        }
    }

    //设置页面的背景
    private void setBookBg(int type){
        int[] readBgArr = UIUtils.getIntArr(R.array.read_bg);
        int[] readFontArr = UIUtils.getIntArr(R.array.read_font);
        LogUtil.d(TAG, "setBookBg", "type = " + type);
        if (readBgArr == null || readBgArr.length <= type)
            return;
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight , Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(readBgArr[type]);
        setBookPageBg(readBgArr[type]);
        setBgBitmap(bitmap);
        //设置字体颜色
        this.mTextColor = readFontArr[type];
    }

    public void setBookPageBg(int color){
        if (mBookPageWidget != null) {
            mBookPageWidget.setBgColor(color);
        }
    }

    public void clear(){
        mBookPageWidget = null;
        mPageEvent = null;
        cancelPage = null;
        currentPage = null;
        stopAutoRead();
    }

    public static Status getStatus(){
        return mStatus;
    }

    public static void setStatus(Status mStatus) {
        PageFactory2.mStatus = mStatus;
    }

    public long getBookLen(){
        return mNovelManager.getChapterLen();
    }

    public TRPage getCurrentPage(){
        return currentPage;
    }

    public boolean isExistChapter(int chapterSort) {
        return mNovelManager != null && mNovelManager.isExistChapter(chapterSort);
    }

    //是否是第一页
    public boolean isFirstPage() {
        return currentPage == null || currentPage.isFirstPage();
    }
    //是否是最后一页
    public boolean isLastPage() {
        return currentPage == null || currentPage.isLastPage();
    }
    //设置页面背景
    public void setBgBitmap(Bitmap BG) {
        m_book_bg = BG;
    }
    //设置页面背景
    public Bitmap getBgBitmap() {
        return m_book_bg;
    }

    //获取文字颜色
    public int getTextColor() {
        return this.mTextColor;
    }

    public void setPageEvent(PageEvent pageEvent){
        this.mPageEvent = pageEvent;
    }

    public interface PageEvent{
        void changeProgress(float progress, int page);
    }

    private CommonObserver<Long> mAutoReadObserver = new CommonObserver<Long>() {
        @Override
        public void onNext(Long aLong) {
            nextPage();
        }
    };

    /** 开始自动阅读 **/
    public void startAutoRead() {
        stopAutoRead();
        Observable.interval(PERIOD_TIME, PERIOD_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAutoReadObserver);
    }

    public void stopAutoRead() {
        if(mAutoReadObserver != null)
            mAutoReadObserver.destroy();
    }

    /**
     * 当请求到新的章节
     */
    public boolean onRequestedNewChapter() {
        ChapterManager manager = NovelManager.getInstance().getCurrChapterManager();
        boolean result = manager != null && !manager.isInitChapterSuccess();
        LogUtil.d(TAG, "onRequestedNewChapter", "result = " + result,
                NovelManager.getInstance().getCurrChapterSort(), manager.isInitChapterSuccess());
        if (result) {
            initChapter();
            int pageCount = manager.getPageCount();
            int page = isTurnLastPageIfRequestedData ? pageCount - 1 : 0;
            isTurnLastPageIfRequestedData = false;
            TRPage currPage = getTRPage(page);
            if (currPage == null) {
                LogUtil.e("onRequestedNewChapter, currPage is null", "page = " + page);
                return false;
            }
            this.currentPage = currPage;
            drawCurrentPage(false);
            return true;
        }
        return false;
    }



}

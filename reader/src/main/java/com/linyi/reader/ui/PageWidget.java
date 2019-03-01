package com.linyi.reader.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.linyi.reader.anim.AnimationProvider;
import com.linyi.reader.anim.CoverAnimation;
import com.linyi.reader.anim.NoneAnimation;
import com.linyi.reader.anim.SimulationAnimation;
import com.linyi.reader.constant.PageMode;
import com.linyi.reader.entity.BlockEntity;

import org.linyi.base.VivaConstant;
import org.linyi.base.VivaSdk;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.ScreenUtils;
import org.linyi.base.utils.SharePreUtil;
import org.linyi.base.utils.UIUtils;


/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class PageWidget extends View {
    private final static String TAG = "PageWidget";
    private int mScreenWidth = VivaConstant.ScreenWidth; // 屏幕宽
    private int mScreenHeight = VivaConstant.ScreenHeight; // 屏幕高
    private Context mContext;

    //是否移动了
    private Boolean isMove = false;
    //是否翻到下一页
    private Boolean isNext = false;
    //是否取消翻页
    private Boolean cancelPage = false;
    //是否没下一页或者上一页
    private Boolean noNext = false;
    private int downX = 0;
    private int downY = 0;

    private int moveX = 0;
    private int moveY = 0;
    //翻页动画是否在执行
    private Boolean isRunning =false;

    Bitmap mCurPageBitmap = null; // 当前页
    Bitmap mNextPageBitmap = null;
    private AnimationProvider mAnimationProvider;

    Scroller mScroller;
    private int mBgColor = Color.WHITE;
    private TouchListener mTouchListener;
    //订阅按钮绘制区域
    private BlockEntity mSubscribeBtnRect;
    //中间菜单按钮
    private BlockEntity mMenuRect;

    public PageWidget(Context context) {
        this(context,null);
    }

    public PageWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PageWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPage();
        mScroller = new Scroller(getContext(),new LinearInterpolator());
        mAnimationProvider = new CoverAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
    }

    private boolean isInit;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
        if (onSizeChangedListener != null && !isInit) {
            isInit = true;
            onSizeChangedListener.onSizeChanged();
        }
        LogUtil.d(TAG, "onSizeChanged", "mScreenHeight = " + mScreenHeight);
    }

    private void initPage(){
        mScreenWidth = UIUtils.getScreenWidth(VivaSdk.getContext());
        mScreenHeight = ScreenUtils.getScreenRealHeight(VivaSdk.getContext());
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
        mMenuRect = new BlockEntity(mScreenWidth / 3, mScreenHeight / 5, mScreenWidth * 2 / 3, mScreenHeight * 4 / 5, BlockEntity.MENU);
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    private int mPageMode;
    public void setPageMode(int pageMode){
        this.mPageMode = pageMode;
        LogUtil.d(TAG, "setPageMode", "pageMode = " + pageMode);
        switch (pageMode){
            case PageMode.SIMULATION:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
                break;
            case PageMode.COVER:
                mAnimationProvider = new CoverAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
                break;
//            case PageMode.SLIDE:
//                mAnimationProvider = new SlideAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
//                break;
            case PageMode.NONE:
                mAnimationProvider = new NoneAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
                break;
            default:
                mAnimationProvider = new CoverAnimation(mCurPageBitmap,mNextPageBitmap,mScreenWidth,mScreenHeight);
        }
    }

    public int getPageMode() {
        return mPageMode;
    }

    public Bitmap getCurPage(){
        return mCurPageBitmap;
    }

    public Bitmap getNextPage(){
        return mNextPageBitmap;
    }

    public void setBgColor(int color){
        mBgColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(0xFFAAAAAA);
        canvas.drawColor(mBgColor);
        if (isRunning) {
            mAnimationProvider.drawMove(canvas);
        } else {
            mAnimationProvider.drawStatic(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        PageFactory2.Status status = PageFactory2.getStatus();
//        LogUtil.d(TAG, "onTouchEvent", "action = " + event.getAction(), "status = " + status);
        if (status == PageFactory2.Status.STOP || status == PageFactory2.Status.FAIL){
            return true;
        }

        int x = (int)event.getX();
        int y = (int)event.getY();

        mAnimationProvider.setTouchPoint(x,y);
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            downX = (int) event.getX();
            downY = (int) event.getY();
            moveX = 0;
            moveY = 0;
            isMove = false;
//            cancelPage = false;
            noNext = false;
            isNext = false;
            isRunning = false;
            mAnimationProvider.setStartPoint(downX,downY);
            abortAnimation();
//            LogUtil.d(TAG, "ACTION_DOWN", "mAnimationProvider = " + mAnimationProvider);
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            final int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            //判断是否移动了
            if (!isMove) {
                isMove = Math.abs(downX - x) > slop || Math.abs(downY - y) > slop;
            }

            if (isMove){
                isMove = true;
                if (moveX == 0 && moveY == 0) {
//                    LogUtil.d(TAG,"isMove");
                    //判断翻得是上一页还是下一页
                    if (x - downX > 0){
                        isNext = false;
                    }else{
                        isNext = true;
                    }
                    //触发某一方向(向前/向后)的禁止翻页
                    if (isForbidTurnPage(isNext))
                        return false;

                    cancelPage = false;
                    if (isNext) {
                        Boolean isNext = mTouchListener.nextPage();
//                        calcCornerXY(downX,mScreenHeight);
                        mAnimationProvider.setDirection(AnimationProvider.Direction.next);

                        if (!isNext) {
                            noNext = true;
                            return true;
                        }
                    } else {
                        Boolean isPre = mTouchListener.prePage();
                        mAnimationProvider.setDirection(AnimationProvider.Direction.pre);

                        if (!isPre) {
                            noNext = true;
                            return true;
                        }
                    }
//                    LogUtil.d(TAG,"isNext:" + isNext);
                }else{
                    //触发某一方向(向前/向后)的禁止翻页
                    if (isForbidTurnPage(isNext))
                        return false;

                    //判断是否取消翻页
                    if (isNext){
                        if (x - moveX > 0){
                            cancelPage = true;
                            mAnimationProvider.setCancel(true);
                        }else {
                            cancelPage = false;
                            mAnimationProvider.setCancel(false);
                        }
                    }else{
                        if (x - moveX < 0){
                            mAnimationProvider.setCancel(true);
                            cancelPage = true;
                        }else {
                            mAnimationProvider.setCancel(false);
                            cancelPage = false;
                        }
                    }
//                    LogUtil.d(TAG,"cancelPage:" + cancelPage);
                }
                moveX = x;
                moveY = y;
                isRunning = true;
                this.postInvalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            LogUtil.d(TAG,"ACTION_UP", "mAnimationProvider = " + mAnimationProvider);
            if (!isMove){
                cancelPage = false;
                //点击了订阅按钮
                if (mSubscribeBtnRect != null && mSubscribeBtnRect.contains(downX, downY)) {
                    if (mTouchListener != null)
                        mTouchListener.clickBlock(mSubscribeBtnRect);
                    LogUtil.d(TAG,"clickBlock, mSubscribeBtnRect");
                    return true;
                }else if (mMenuRect != null && mMenuRect.contains(downX, downY)){ //是否点击了中间
                    if (mTouchListener != null)
                        mTouchListener.clickBlock(mMenuRect);
                    LogUtil.d(TAG,"clickBlock, mMenuRect");
                    return true;
                }else if (x < mScreenWidth / 2){
                    isNext = false;
                }else{
                    isNext = true;
                }

                //触发某一方向(向前/向后)的禁止翻页
                if (isForbidTurnPage(isNext))
                    return false;

                if (isNext) {
                    Boolean isNext = mTouchListener.nextPage();
                    mAnimationProvider.setDirection(AnimationProvider.Direction.next);
                    if (!isNext) {
                        return true;
                    }
                } else {
                    Boolean isPre = mTouchListener.prePage();
                    mAnimationProvider.setDirection(AnimationProvider.Direction.pre);
                    if (!isPre) {
                        return true;
                    }
                }
            }


            //触发某一方向(向前/向后)的禁止翻页
            if (isForbidTurnPage(isNext)) {
                isRunning = false;
                return false;
            }

            if (cancelPage && mTouchListener != null){
                mTouchListener.cancel();
            }

//            LogUtil.d(TAG,"isNext:" + isNext);
            if (!noNext) {
                isRunning = true;
                boolean isPerformAnim = mAnimationProvider.startAnimation(mScroller);
                this.postInvalidate();
                if (!isPerformAnim && mTouchListener != null) {
                    isRunning = false;
                    mTouchListener.onPageStatic();
                }
            } else if (mTouchListener != null){
                isRunning = false;
                mTouchListener.onPageStatic();
            }
        }
        return true;
    }

    /** 触发某一方向(向前/向后)的禁止翻页 **/
    private boolean isForbidTurnPage(boolean isNext) {
        PageFactory2.Status status = PageFactory2.getStatus();
        if (status == PageFactory2.Status.STOP_NEXT)
            return isNext;
        if(status == PageFactory2.Status.STOP_PREV)
            return !isNext;
        return false;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            mAnimationProvider.setTouchPoint(x, y);
            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y){
                isRunning = false;
                if(mTouchListener != null)
                    mTouchListener.onPageStatic();
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    public void abortAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            mAnimationProvider.setTouchPoint(mScroller.getFinalX(),mScroller.getFinalY());
            postInvalidate();
        }
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void setTouchListener(TouchListener mTouchListener){
        this.mTouchListener = mTouchListener;
    }

    public interface TouchListener{

        void clickBlock(BlockEntity entity);

        void cancel();

        //当页面动画停止的时候回调
        void onPageStatic();

        /**
         * @return 是否有上一页数据(有上一章仍为true)
         */
        Boolean prePage();

        /**
         * @return 是否有下一页数据(有下一章仍为true)
         */
        Boolean nextPage();
    }

    private OnSizeChangedListener onSizeChangedListener;
    public interface OnSizeChangedListener{
        void onSizeChanged();
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        this.onSizeChangedListener = listener;
    }

    public BlockEntity getSubscribeBtnRect() {
        return mSubscribeBtnRect;
    }

    public void setSubscribeBtnRect(BlockEntity mSubscribeBtnRect) {
        this.mSubscribeBtnRect = mSubscribeBtnRect;
    }



}

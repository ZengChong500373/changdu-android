package com.linyi.viva.extra.ui.weidgt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.linyi.viva.extra.R;

import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自动滚动view
 */
public class AutoScrollView extends View {
    public static final String TAG = "AutoScrollView";

    //忽视的item类型
    public static final String ITEM_IGNORE = null;
    //循环滚动
    public static final String ITEM_CIRCLE = "AUTO_SCROLL_VIEW_CIRCLE";
    //停止滚动
    public static final String ITEM_STOP = "AUTO_SCROLL_VIEW_STOP";


    private final int MSG_MOVE = 1;
    private final int MSG_STOP = 2;
    //text之间间距和minTextSize之比
    public static final float MARGIN_ALPHA = 1.3f;
    //自动回滚到中间的速度
    public static final float SPEED = 2;
    //滚动速度
    public float MOVE_SPEED = 16;
    //是否朝上滚动
    private boolean isUpOrientation;

    private List<String> mDataList;
    //选中的位置，这个位置是mDataList的中心位置，一直不变
    private int mCurrentSelected;
    private Paint mPaint;

    private float mMaxTextSize = 130;
    private float mMinTextSize = 100;

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 200;

    private int mColorText = UIUtils.getColor(R.color.colorPrimary);

    private int mViewHeight;
    private int mViewWidth;

    public void setTextSize(int mMaxTextSize,int mMinTextSize){
        this.mMaxTextSize = mMaxTextSize;
        this.mMinTextSize = mMinTextSize;
    }
    //滑动的距离
    private float mMoveLen = 0;
    private boolean isInit = false;
    private AutoScrollView.onSelectListener mSelectListener;
    private Timer timer;
    private AutoScrollView.MyTimerTask mTask;

    Handler updateHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_STOP) {
                if (Math.abs(mMoveLen) < SPEED) {
                    mMoveLen = 0;
                    if (mTask != null) {
                        mTask.cancel();
                        mTask = null;
                        performSelect();
                    }
                } else {
                    // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                    mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
                }
                invalidate();
            } else if (msg.what == MSG_MOVE) {
                currTime += UPDATE_PERIOD;
//                LogUtil.d(TAG, "mMoveLen = " + mMoveLen, "stopItemText = " + stopItemText);
                if (currTime >= MAX_SCROLL_TIME) {
                    doUp();
                } else {
                    if (mDataList != null && mDataList.size() > mCurrentSelected &&
                            TextUtils.equals(mDataList.get(mCurrentSelected), stopItemText)) {
                        doUp();
                    } else {
                        doMove(getParseSpeed(MOVE_SPEED, currTime * 1f / MAX_SCROLL_TIME) * (isUpOrientation ? -1 : 1));
                    }
                }
            }
            return false;
        }
    });

    /**
     * 修改每次更新的速度，线性递减
     */
    private float getParseSpeed(float speed, float progress) {
        return SPEED + (speed - SPEED) * (1 - progress);
    }

    public AutoScrollView(Context context) {
        super(context);
        init();
    }

    public AutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSelectListener(AutoScrollView.onSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null){
            //mCurrentSelected == size()/2;
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
        }
    }

    public void setData(List<String> data){
        mDataList = data;
        mCurrentSelected = data.size() / 2;
        invalidate();
    }

    /**
     * 选择选中的item的index
     * @param selected
     */
    public void setSelected(int selected){
        mCurrentSelected = selected;
        int distance = mDataList.size() / 2 - mCurrentSelected;
        if (distance < 0)
            for (int i = 0; i < -distance; i++) {
                moveHeadToTail();
                mCurrentSelected--;
            }
        else if (distance > 0)
            for (int i = 0; i < distance; i++) {
                moveTailToHead();
                mCurrentSelected++;
            }
        invalidate();
    }

    /**
     * 选择选中的内容
     *
     * @param mSelectItem
     */
    public void setSelected(String mSelectItem) {
        for (int i = 0; i < mDataList.size(); i++)
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i);
                break;
            }
    }

    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight()-10;
        mViewWidth = getMeasuredWidth();
        // 按照View的高度计算字体大小
        if(mMaxTextSize == 0)
            mMaxTextSize = mViewHeight / 5.0f;
        if(mMinTextSize == 0)
            mMinTextSize = mMaxTextSize / 3f;
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mColorText);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setTypeface(font);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit)
            drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }
    }

    /**
     * @param position 距离mCurrentSelected的差值
     * @param type 1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = (MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen);
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                (float) (mViewWidth / 2.0), baseline, mPaint);
    }

    /**
     * 抛物线
     * @param zero 零点坐标
     * @param x 偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    private void doDown() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    private void doMove(float date) {
        mMoveLen += date;
        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }
        invalidate();
    }

    private void doUp() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        mTask = new AutoScrollView.MyTimerTask(updateHandler, MSG_STOP);
        timer.schedule(mTask, 0, 10);
    }
    //当前滚动的时间
    private int currTime;
    //最多滚动时长
    private int MAX_SCROLL_TIME;
    //每次更新的间隔时长
    private final int UPDATE_PERIOD = 16;
    public void start(int millSecond, boolean isUpOrientation) {
        doDown();
        this.isUpOrientation = isUpOrientation;
        this.MAX_SCROLL_TIME = millSecond;
        this.currTime = 0;
        this.mTask = new AutoScrollView.MyTimerTask(updateHandler, MSG_MOVE);
        this.timer.schedule(mTask, 0, UPDATE_PERIOD);
    }

    //停止滑动的item
    private String stopItemText;
    /**
     * 滚动到指定数据
     * @param item 详成员变量ITEM...
     */
    public void start(String item, boolean isUpOrientation, int speed) {
        LogUtil.d(TAG, "start", "item = " + item, "isUpOrientation = " + isUpOrientation);
        if(item == ITEM_IGNORE)
            return;
        if(speed > 0)
            this.MOVE_SPEED = speed;
        if (TextUtils.equals(item, ITEM_STOP)) {
            doDown();
        } else if (TextUtils.equals(item, ITEM_CIRCLE)) {
            start(Integer.MAX_VALUE, isUpOrientation);
            stopItemText = null;
        } else {
            start(Integer.MAX_VALUE, isUpOrientation);
            stopItemText = item;
        }
    }

    class MyTimerTask extends TimerTask {
        Handler handler;
        int what;

        public MyTimerTask(Handler handler, int what) {
            this.handler = handler;
            this.what = what;
        }

        @Override
        public void run() {
            Message msg = handler.obtainMessage();
            msg.what = this.what;
            handler.sendMessage(msg);
        }

    }

    public interface onSelectListener {
        void onSelect(String text);
    }
}

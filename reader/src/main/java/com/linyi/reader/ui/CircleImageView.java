package com.linyi.reader.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.linyi.reader.R;


/**
 * Created by rwz on 2017/2/15.
 * 带圆形背景TextView
 */

public class CircleImageView extends AppCompatImageView {

    private Paint mPaint;
    private int mCircleColor;

    public CircleImageView(Context context) {
        super(context);
        init(context,null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mCircleColor);
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        mPaint.setColor(circleColor);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCircleColor != 0) {
            int x = getWidth()/2;
            int y = getHeight()/2;
            int r = Math.min(x, y);
            canvas.drawCircle(x , y , r , mPaint);
        }
        super.onDraw(canvas);
    }
}

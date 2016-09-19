package com.cn.guojinhu.systemwidget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.cn.guojinhu.systemwidget.R;

/**
 * Created by guojin.hu on 2016/9/12.
 */

public class CustomVolumeView extends View {

    private int mHintColor;
    private int mBgColor;
    private int mDotCount;
    private int mSplitSize;
    private float mRingWidth;
    private Drawable mBgDrawable;

    private Paint mPaint;

    private int mCurrentSize;

    public CustomVolumeView(Context context) {
        this(context, null);
    }

    public CustomVolumeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CustomVolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomVolumeView);
        mHintColor = ta.getColor(R.styleable.CustomVolumeView_hintColor, Color.DKGRAY);
        mBgColor = ta.getColor(R.styleable.CustomVolumeView_bgColor, Color.LTGRAY);
        mDotCount = ta.getInt(R.styleable.CustomVolumeView_dotCount, 12);
        mCurrentSize = mDotCount/2;
        mSplitSize = ta.getInt(R.styleable.CustomVolumeView_splitSize, 5);
        mRingWidth = ta.getDimension(R.styleable.CustomVolumeView_ringWidth,
                getResources().getDimension(R.dimen.ring_width));
        mBgDrawable = ta.getDrawable(R.styleable.CustomVolumeView_bg);
        ta.recycle();
        init();
    }

    public CustomVolumeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mRingWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int center = getWidth() / 2;
        float radius = center - mRingWidth / 2;
        /**
         * 画音量块
         */
        drawOval(canvas, center, radius);


        
    }

    private void drawOval(Canvas canvas, int center, float radius) {
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        float itemSize = (360 * 1.0F - mDotCount * mSplitSize) / mDotCount;//算出数量

        mPaint.setColor(mBgColor);
        for (int i = 0; i < mDotCount; i++) {
            canvas.drawArc(oval,i*(itemSize+mSplitSize),itemSize,false,mPaint);
        }

        mPaint.setColor(mHintColor);
        for (int i = 0; i < mCurrentSize; i++) {
            canvas.drawArc(oval,i*(itemSize+mSplitSize),itemSize,false,mPaint);
        }
    }

    public int getCurrentSize() {
        return mCurrentSize;
    }

    public void setCurrentSize(int currentSize) {
        mCurrentSize = currentSize;
        invalidate();
    }
}

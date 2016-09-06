package com.cn.guojinhu.systemwidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class VolumeView extends View {

    private Paint mPaint;
    private LinearGradient mLinearGradient;

    private int mRefCount;
    private int mWidth;
    private int mRectHeight;
    private int mRectWidth;
    private double mRandom;
    private int offset = 5;

    public VolumeView(Context context) {
        super(context);
        initView();
    }

    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public VolumeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        mRefCount = 12;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();//获得控件的宽度

        mRectHeight = getHeight();//获得控件的高度 设置渐变器高度终点
        mRectWidth = (int) (mWidth * 0.6 / mRefCount);//设置渐变器宽度终点
        Log.d("Vo7ice", "mWidth:" + mWidth+",mRectWidth:"+mRectWidth);
        mLinearGradient = new LinearGradient(//设置渐变器
                0,
                0,
                mRectWidth,
                mRectHeight,
                Color.YELLOW,
                Color.BLUE,
                Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mRefCount; i++) {
            mRandom = Math.random();
            float currentHeight = (float) (mRectHeight * mRandom);
            canvas.drawRect(
                    (float) (mWidth * 0.4 / 2 + mRectWidth * i + offset),//左边坐标
                    currentHeight,//上边坐标
                    (float) (mWidth * 0.4 / 2 + mRectWidth * (i + 1)),//右边坐标
                    mRectHeight,//底边坐标
                    mPaint);
        }
        postInvalidateDelayed(300);
    }
}

package com.cn.guojinhu.systemwidget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by guojin.hu on 2016/9/20.
 * url:http://blog.csdn.net/zhangml0522/article/details/52556418
 */

public class WinXLoadingView extends View {

    private Paint mPaint;

    private Path mPath;
    private Path dst;

    private PathMeasure mPathMeasure;

    private ValueAnimator valueAnimator;

    private float progress;

    private int mWidth, mHeight;


    public WinXLoadingView(Context context) {
        this(context, null);
    }

    public WinXLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WinXLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        valueAnimator.start();
    }

    public WinXLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(15);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPath = new Path();
        RectF oval = new RectF(-150, -150, 150, 150);
        mPath.addArc(oval, -90, 359.9f);
        mPathMeasure = new PathMeasure(mPath, false);
        dst = new Path();

        initAnimate();
    }

    private void initAnimate() {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(3000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);

        if (progress > 0.95) {
            canvas.drawPoint(0, -150, mPaint);//在快结束的时候先绘制一个点
        }

        dst.reset();
        drawEachPath(canvas);

        /*mPathMeasure.getSegment(mPathMeasure.getLength() * progress,
                mPathMeasure.getLength() * progress + 1, dst, true);*/
        canvas.drawPath(dst, mPaint);
    }

    private void drawEachPath(Canvas canvas) {
        int num = (int) (progress / (0.05f));
        float x, y, s;
        switch (num) {
            default:
            case 3:
                x = progress - (0.15f) * (1 - progress);//偏移方程 让最后点集合在一起 y=k*x+b
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * x * s;//抛物线方程 速度不断增加 y=a*x*x+b*x+c
                mPathMeasure.getSegment(y, y + 1, dst, true);
                //break;//不加会只有一部分
            case 2:
                x = progress - (0.10f) * (1 - progress);
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * x * s;
                mPathMeasure.getSegment(y, y + 1, dst, true);
                //break;
            case 1:
                x = progress - (0.05f) * (1 - progress);
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * x * s;
                mPathMeasure.getSegment(y, y + 1, dst, true);
                //break;
            case 0:
                x = progress;
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * x * s;
                mPathMeasure.getSegment(y, y + 1, dst, true);
                break;
        }
        canvas.drawPath(dst, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

}

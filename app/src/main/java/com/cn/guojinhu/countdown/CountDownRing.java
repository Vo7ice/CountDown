package com.cn.guojinhu.countdown;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import android.os.Handler;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CountDownRing extends View {

    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Paint mRingPaint;

    private int mCircleX;
    private int mCircleY;

    private RectF mRingRectF;//外接矩形
    private float mStartSweepValue;//开始角度
    private float mCurrentAngle;//当前角度

    private int mCurrentTime;
    private int sumTime = 15000;

    private int mRadius;
    private int mCircleBackground;
    private int mRingStartColor;
    private int mRingEndColor;
    private int mTextColor;
    private int mTextSize;

    private static final int TIME_CHANGE = 10;

    private onChangeListener mListener;
    private int mIndex;
    private int mSize;


    public CountDownRing(Context context) {
        this(context, null);
    }

    public CountDownRing(Context context, AttributeSet attrs) {
        super(context, attrs);
        //自定义属:value/attr
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountDownRing);
        //中间背景颜色 默认为灰色
        mCircleBackground = ta.getColor(R.styleable.CountDownRing_circleBackground, 0xFF616161);
        //外圆环开始颜色 默认为绿色
        mRingStartColor = ta.getColor(R.styleable.CountDownRing_ringStartColor, 0XFF00FF00);
        //外圆环结束颜色 默认为红色
        mRingEndColor = ta.getColor(R.styleable.CountDownRing_ringEndColor, 0XFFFF0000);
        //文字颜色 默认为黑色
        mTextColor = ta.getColor(R.styleable.CountDownRing_textColor, 0XFFFFFFFF);
        //中间圆的半径 默认为60
        mRadius = ta.getInt(R.styleable.CountDownRing_radius, 60);
        ta.recycle();
        //初始化
        init(context);
    }

    public CountDownRing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CountDownRing(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mStartSweepValue = -90;//-90度 正北方向
        mCurrentAngle = 0;//当前角度
        mCurrentTime = 0;//当前时间

        //设置中心圆画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleBackground);
        mCirclePaint.setStyle(Paint.Style.FILL);

        //设置文字画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth((float) (0.025 * mRadius));
        mTextPaint.setTextSize(mRadius / 2);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        //设置圆环画笔
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingStartColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth((float) (0.075 * mRadius));
        mRingPaint.setStrokeCap(Paint.Cap.ROUND);//头部为圆形
//        mRingPaint.setShader(new LinearGradient())

        //获取文字字号 保证在圆心
        mTextSize = (int) mTextPaint.getTextSize();
    }

    @SuppressWarnings("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int startRed = Color.red(mRingStartColor);
            int startGreen = Color.green(mRingStartColor);
            int startBlue = Color.blue(mRingStartColor);
            int endRed = Color.red(mRingEndColor);
            int endGreen = Color.green(mRingEndColor);
            int endBlue = Color.blue(mRingEndColor);

            postInvalidate();

        }
    };

    private int getRightColor(int startColor, int endColor) {
        int color;
        int delta = startColor - endColor;
        if (delta > 0) {
            color = startColor - delta * mCurrentTime / sumTime;
        } else if (delta < 0) {
            color = endColor - delta * mCurrentTime / sumTime;
        } else {
            color = startColor;
        }
        return color;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(widthMeasureSpec));
        //设置圆心坐标
        mCircleX = getMeasuredWidth() / 2;
        mCircleY = getMeasuredHeight() / 2;

        //如果半径大于圆心横坐标，需要手动缩小半径的值，否则就画到外面去了
        if (mRadius > mCircleX) {
            //设置半径大小为圆心横坐标到原点的距离
            mRadius = mCircleX;
            mRadius = (int) (mCircleX - 0.075 * mRadius);
            //因为半径改变了，所以要重新设置一下字体宽度
            mTextPaint.setStrokeWidth((float) (0.025 * mRadius));
            //重新设置字号
            mTextPaint.setTextSize(mRadius / 2);
            //重新设置外圆环宽度
            mRingPaint.setStrokeWidth((float) (0.075 * mRadius));
            //重新获得字号大小
            mTextSize = (int) mTextPaint.getTextSize();
        }
        //画中心园的外接矩形，用来画圆环用
        mRingRectF = new RectF(mCircleX - mRadius + mRingPaint.getStrokeWidth(),
                mCircleY - mRadius + mRingPaint.getStrokeWidth(),
                mCircleX + mRadius - mRingPaint.getStrokeWidth(),
                mCircleY + mRadius - mRingPaint.getStrokeWidth());
    }

    //当wrap_content的时候，view的大小根据半径大小改变，但最大不会超过屏幕
    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (1.075 * mRadius * 2);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画中间圆
        canvas.drawCircle(mCircleX, mCircleY, mRadius, mCirclePaint);
        //画圆环
        canvas.drawArc(mRingRectF, mStartSweepValue, mCurrentAngle, false, mRingPaint);
        //画文字
        canvas.drawText(String.valueOf(mCurrentTime / 1000), mCircleX, mCircleY + mTextSize / 4, mTextPaint);
        double a = 360 * TIME_CHANGE;

        if (mCurrentTime < sumTime) {
            mCurrentTime += TIME_CHANGE;
            mCurrentAngle += a / sumTime;
            //每1s重画一次
            postInvalidateDelayed(TIME_CHANGE);
        }
        if (mCurrentTime == sumTime) {
            if (mListener != null) {
                if (mIndex < mSize) {
                    mIndex++;
                } else {
                    mIndex = 0;
                }
                mListener.onChangeImage(mIndex);
            }
            mCurrentTime = 0;
            mCurrentAngle = 0;
        }

    }

    private float getAngle() {
        BigDecimal bg = new BigDecimal(360 * TIME_CHANGE / sumTime);
        float d = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return d;
    }

    public int getSumTime() {
        return sumTime;
    }

    public void setSumTime(int sumTime) {
        this.sumTime = sumTime;
    }

    public void setListener(onChangeListener listener) {
        this.mListener = listener;
    }

    public int getCurrentIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public interface onChangeListener {
        void onChangeImage(int index);
    }
}

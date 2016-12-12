package com.cn.guojinhu.systemwidget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cn.guojinhu.systemwidget.R;

/**
 * Created by guojin.hu on 2016/9/6.
 */

public class CustomTitleView extends View {

    private String mTitleText;//文字

    private int mTitleColor;//文字颜色

    private float mTitleSize;//文字大小

    private Rect mBounds;//矩形

    private Paint mPaint;//画笔

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 自定义属性获得构造方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleView);
        mTitleText = ta.getString(R.styleable.CustomTitleView_customTitleText);
        mTitleColor = ta.getColor(R.styleable.CustomTitleView_customTitleTextColor, Color.BLACK);
        mTitleSize = ta.getDimension(R.styleable.CustomTitleView_customTitleTextSize, 16);
        ta.recycle();

        init();
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(mTitleSize);
        mBounds = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBounds);//获取文本区域大小
        Log.d("Vo7ice", "mBounds-->" + mBounds.width() + "," + mBounds.height());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);//绘制所在区域的矩形
        mPaint.setColor(mTitleColor);
        Log.d("Vo7ice", "mText-->" + mTitleText + ",mColor-->" + mPaint.getColor());
        //绘制文本
        canvas.drawText(mTitleText, getWidth() / 2 - mBounds.width() / 2, getHeight() / 2 + mBounds.height() / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;//实际宽度
        int height;//实际高度
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBounds);
            float textWidth = mBounds.width();
            int desired = (int) (textWidth + getPaddingLeft() + getPaddingRight());
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBounds);
            float textHeight = mBounds.height();
            int desired = (int) (textHeight + getPaddingTop() + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
}

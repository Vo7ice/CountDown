package com.cn.guojinhu.systemwidget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cn.guojinhu.systemwidget.R;

/**
 * Created by guojin.hu on 2016/9/6.
 */

public class CustomImageView extends View {

    private String mTitleText;//文字

    private int mTitleColor;//文字颜色

    private float mTitleSize;//文字大小

    private Bitmap mImage;//图片

    private int mImageScaleType;//图片类型

    private static final int IMAGE_TYPE_FILLXY = 0;

    private Paint mPaint;
    private Rect mRect;
    private Rect mTextBound;


    private int mWidth;
    private int mHeight;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        mTitleText = ta.getString(R.styleable.CustomImageView_customImageText);
        mTitleColor = ta.getColor(R.styleable.CustomImageView_customImageTextColor, Color.RED);
        mTitleSize = ta.getDimension(R.styleable.CustomImageView_customImageTextSize,
                getResources().getDimension(R.dimen.text_size));
        mImage = BitmapFactory.decodeResource(getResources(), ta.getResourceId(R.styleable.CustomImageView_customImage, 0));
        mImageScaleType = ta.getInt(R.styleable.CustomImageView_customImageScaleType, 0);
        ta.recycle();

        init();
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(mTitleSize);
        mRect = new Rect();//画图中心区域
        mTextBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 设置宽度
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            int desiredByImage = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            int desiredByText = getPaddingLeft() + getPaddingRight() + mTextBound.width();
            if (widthMode == MeasureSpec.AT_MOST) {//wrap content
                int desire = Math.max(desiredByImage, desiredByText);
                mWidth = Math.min(desire, widthSize);
            }
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            int desire = getPaddingBottom() + getPaddingTop() + mImage.getHeight() + mTextBound.height();
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(heightSize, desire);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //画边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.top = getPaddingTop();
        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTitleColor);
        mPaint.setStyle(Paint.Style.FILL);

        if (mWidth < mTextBound.width()) {
            TextPaint paint = new TextPaint(mPaint);
            String text = TextUtils.ellipsize(mTitleText, paint, mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(text, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        } else {
            /*正常情况*/
            canvas.drawText(mTitleText, mWidth / 2 - mTextBound.width() / 2, mHeight - getPaddingBottom(), mPaint);
        }

        mRect.bottom -= mTextBound.height();//减掉text所占的矩形

        if (mImageScaleType == IMAGE_TYPE_FILLXY) {
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        } else {
            mRect.left = (mWidth - mImage.getWidth()) / 2;
            mRect.right = (mWidth + mImage.getWidth()) / 2;
            mRect.top = (mHeight - mTextBound.height() - mImage.getHeight()) / 2;
            mRect.bottom = (mHeight - mTextBound.height() + mImage.getHeight()) / 2;
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }
    }
}

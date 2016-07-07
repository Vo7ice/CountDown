package com.cn.guojinhu.systemwidget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView{

    private float mStoke;

    private final int def_outlineColor = 0Xb0120a;
    private final int def_inlineColor = 0Xe51c23;
    private int mOutlineColor;
    private int mInlineColor;

    private Paint mOutlinePaint;
    private Paint mInlinePaint;

    public MyTextView(Context context) {
        super(context);
        initView();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ty = context.obtainStyledAttributes(attrs,R.styleable.MyTextView);
        mStoke = ty.getDimension(R.styleable.MyTextView_stoke, R.dimen.def_stoke);
        mOutlineColor = ty.getColor(R.styleable.MyTextView_outlineColor, def_outlineColor);
        mInlineColor = ty.getColor(R.styleable.MyTextView_inlineColor, def_inlineColor);
        ty.recycle();
        initView();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        mInlinePaint = new Paint();
        mInlinePaint.setColor(mInlineColor);
        mInlinePaint.setStyle(Paint.Style.FILL);
        mOutlinePaint = new Paint();
        mOutlinePaint.setColor(mOutlineColor);
        mInlinePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制外部矩形
        canvas.drawRect(
                0,
                0,
                getMeasuredWidth(),
                getMeasuredHeight(),
                mOutlinePaint
        );

        //绘制内部矩形
        canvas.drawRect(
                mStoke,
                mStoke,
                getMeasuredWidth()-mStoke,
                getMeasuredHeight()-mStoke,
                mInlinePaint
        );
        canvas.translate(mStoke,0);
        super.onDraw(canvas);
        canvas.restore();
    }
}

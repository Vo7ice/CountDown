package com.cn.guojinhu.systemwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopBar extends RelativeLayout {

    private static final int DEF_COLOR = 0Xff0000;

    private Button mLeftButton, mRightButton;
    private TextView mTextView;

    //text 属性
    private String titleText;
    private String leftText;
    private String rightText;

    //color 属性
    private int titleTextColor;
    private int leftTextColor;
    private int rightTextColor;

    //drawable属性
    private Drawable leftBackground;
    private Drawable rightBackground;

    private float titleTextSize;
    private LayoutParams mLeftParams;
    private LayoutParams mRightParams;
    private LayoutParams mTitleParams;

    private OnTopBarClickListener mListener;

    @IntDef({SHOW_ALL, SHOW_LEFT, SHOW_RIGHT, SHOW_NONE})
    public @interface FLAG {
    }

    public static final int SHOW_ALL = 0;
    public static final int SHOW_LEFT = 1;
    public static final int SHOW_RIGHT = 2;
    public static final int SHOW_NONE = -1;

    private int mViewFlag = SHOW_ALL;

    public TopBar(Context context) {
        super(context);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xFFF59563);
        //配置属性
        TypedArray ty = context.obtainStyledAttributes(attrs,R.styleable.TopBar);
        titleText = ty.getString(R.styleable.TopBar_titleText);
        leftText = ty.getString(R.styleable.TopBar_leftText);
        rightText = ty.getString(R.styleable.TopBar_rightText);

        titleTextColor = ty.getColor(R.styleable.TopBar_titleTextColors, 0);
        leftTextColor = ty.getColor(R.styleable.TopBar_leftTextColor, 0);
        rightTextColor = ty.getColor(R.styleable.TopBar_rightTextColor, 0);

        leftBackground = ty.getDrawable(R.styleable.TopBar_leftBackground);
        rightBackground = ty.getDrawable(R.styleable.TopBar_rightBackground);

        titleTextSize = ty.getDimension(R.styleable.TopBar_titleTextSize, 16);

        ty.recycle();

        Log.d("Vo7ice", "titleText:" + titleText);

        //为每个控件匹配对应属性
        mTextView = new TextView(context);
        mLeftButton = new Button(context);
        mRightButton = new Button(context);

        mTextView.setTextSize(titleTextSize);
        mTextView.setTextColor(titleTextColor);
        mTextView.setText(titleText);
        mTextView.setGravity(Gravity.CENTER);


        mLeftButton.setTextColor(leftTextColor);
        mLeftButton.setBackground(leftBackground);
        mLeftButton.setText(leftText);


        mRightButton.setTextColor(rightTextColor);
        mRightButton.setBackground(rightBackground);
        mRightButton.setText(rightText);

        mLeftParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        mLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(mLeftButton, mLeftParams);

        mRightParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        mRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(mRightButton, mRightParams);

        mTitleParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        mTitleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(mTextView, mTitleParams);

        mLeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onLeftButtonClick(v);
                }
            }
        });
        mRightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightButtonClick(v);
                }
            }
        });
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFlag(@FLAG int flag) {
        mViewFlag = flag;
        switch (mViewFlag) {
            case SHOW_LEFT:
                mLeftButton.setVisibility(VISIBLE);
                mRightButton.setVisibility(GONE);
                break;
            case SHOW_RIGHT:
                mLeftButton.setVisibility(GONE);
                mRightButton.setVisibility(VISIBLE);
                break;
            case SHOW_NONE:
                mLeftButton.setVisibility(GONE);
                mRightButton.setVisibility(GONE);
                break;
            case SHOW_ALL:
            default:
                mLeftButton.setVisibility(VISIBLE);
                mRightButton.setVisibility(VISIBLE);
                break;
        }
        invalidate();
    }

    @FLAG
    public int getFlag() {
        return mViewFlag;
    }


    public void setOnTopBarClickListener(OnTopBarClickListener listener) {
        mListener = listener;
    }

    interface OnTopBarClickListener {
        void onLeftButtonClick(View v);

        void onRightButtonClick(View v);
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        invalidate();
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
        invalidate();
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        invalidate();
    }
}

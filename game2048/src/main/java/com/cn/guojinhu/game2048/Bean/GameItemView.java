package com.cn.guojinhu.game2048.Bean;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;


public class GameItemView extends FrameLayout {

    private Context mContext;
    private TextView mTextNum;
    private int mCardNum;

    public GameItemView(Context context, int cardNum) {
        super(context);
        //初始化item
        initView(context, cardNum);
    }

    private void initView(Context context, int cardNum) {
        this.mContext = context;
        setBackgroundColor(Color.GRAY);
        mTextNum = new TextView(mContext);
        setCardNum(cardNum);//设置数字
        mTextNum.getPaint().setFakeBoldText(true);//设置粗体
        mTextNum.setGravity(Gravity.CENTER);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);//设置布局参数
        layoutParams.setMargins(5, 5, 5, 5);
        addView(mTextNum, layoutParams);
    }

    public int getCardNum() {
        return mCardNum;
    }

    public void setCardNum(int cardNum) {
        mCardNum = cardNum;
        if (mCardNum == 0) {
            mTextNum.setText("");
        } else {
            mTextNum.setText(String.valueOf(mCardNum));
        }
        // 设置背景颜色
        switch (mCardNum) {
            case 0:
                mTextNum.setBackgroundColor(0x00000000);
                break;
            case 2:
                mTextNum.setBackgroundColor(0xffeee5db);
                break;
            case 4:
                mTextNum.setBackgroundColor(0xffeee0ca);
                break;
            case 8:
                mTextNum.setBackgroundColor(0xfff2c17a);
                break;
            case 16:
                mTextNum.setBackgroundColor(0xfff59667);
                break;
            case 32:
                mTextNum.setBackgroundColor(0xfff68c6f);
                break;
            case 64:
                mTextNum.setBackgroundColor(0xfff66e3c);
                break;
            case 128:
                mTextNum.setBackgroundColor(0xffedcf74);
                break;
            case 256:
                mTextNum.setBackgroundColor(0xffedcc64);
                break;
            case 512:
                mTextNum.setBackgroundColor(0xffedc854);
                break;
            case 1024:
                mTextNum.setBackgroundColor(0xffedc54f);
                break;
            case 2048:
                mTextNum.setBackgroundColor(0xffedc32e);
                break;
            default:
                mTextNum.setBackgroundColor(0xff3c4a34);
                break;
        }
    }

    public TextView getItemView() {
        return mTextNum;
    }
}

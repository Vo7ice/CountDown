package com.cn.guojinhu.game2048.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout;

import com.cn.guojinhu.game2048.Base.BaseApplication;
import com.cn.guojinhu.game2048.Bean.GameItemView;
import com.cn.guojinhu.game2048.R;
import com.cn.guojinhu.game2048.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GridLayout implements View.OnTouchListener {

    // GameView对应矩阵
    private GameItemView[][] mGameMatrix;

    //空格数组
    private List<Point> mBlanks;

    //历史记录数组
    private int[][] mGameMatrixHistory;

    //辅助计算数组
    private List<Integer> mCalList;
    private int mKeyItemNum = -1;

    //矩阵行列数
    private int mGameLines;

    //记录坐标
    private int mStartX, mStartY, mEndX, mEndY;

    //目标分数
    private int mTarget;

    //历史记录分数
    private int mScoreHistory;

    //历史最高得分
    private int mHighScore;

    private Context mContext;

    public GameView(Context context) {
        super(context);
        mContext = context;
        mTarget = (int) SPUtils.get(mContext, BaseApplication.KEY_GAME_GOAL, 2048);
        initGameMatrix();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initGameMatrix();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initGameMatrix();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                saveHistoryMatrix();
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mEndX = (int) event.getX();
                mEndY = (int) event.getY();

                break;
            default:
                break;
        }
        return true;
    }

    private void initGameView(int cardSize) {
        removeAllViews();
        GameItemView card;
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                card = new GameItemView(mContext, 0);
                addView(card, cardSize, cardSize);
                //初始化GameMatrix全部为0,空格list为所有
                mGameMatrix[i][j] = card;
                mBlanks.add(new Point(i, j));
            }
        }

    }

    private void initGameMatrix() {
        removeAllViews();//移除所有控件
        BaseApplication.SCORE = 0;//重置得分
        BaseApplication.mGameLines = (int) SPUtils.get(mContext, BaseApplication.KEY_GAME_LINES, 4);
        mGameLines = BaseApplication.mGameLines;
        mGameMatrix = new GameItemView[mGameLines][mGameLines];//初始化矩阵
        mGameMatrixHistory = new int[mGameLines][mGameLines];//初始化历史矩阵
        mCalList = new ArrayList<>();//初始化辅助数组
        mBlanks = new ArrayList<>();//初始化空格
        mHighScore = (int) SPUtils.get(mContext, BaseApplication.KEY_HIGH_SCROE, 0);//初始化最高得分
        setColumnCount(mGameLines);//设置行个数
        setRowCount(mGameLines);//设置列个数
        setOnTouchListener(this);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        BaseApplication.mItemSize = metrics.widthPixels / mGameLines;
        Log.d("Vo7ice", "mItemSize-->" + BaseApplication.mItemSize);
        addRandomNum();
        addRandomNum();
    }

    public void startGame() {
        initGameMatrix();
        initGameView(BaseApplication.mItemSize);
    }

    /**
     * 添加随机数字
     */
    private void addRandomNum() {
        getBlanks();
        if (mBlanks.size() > 0) {
            int randomNum = (int) (Math.random() * mBlanks.size());
            Point point = mBlanks.get(randomNum);
            mGameMatrix[point.x][point.y].setCardNum(Math.random() > 0.2d ? 2 : 4);
            animCreate(mGameMatrix[point.x][point.y]);
        }
    }

    /**
     * 获取空格Item数组
     */
    private void getBlanks() {
        mBlanks.clear();
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                if (mGameMatrix[i][j].getCardNum() == 0) {
                    mBlanks.add(new Point(i, j));
                }
            }
        }
    }

    /**
     * 生成动画
     *
     * @param target target
     */
    private void animCreate(GameItemView target) {
        ScaleAnimation sa = (ScaleAnimation) AnimationUtils.loadAnimation(mContext, R.anim.anim_create);
        target.setAnimation(null);
        target.getItemView().startAnimation(sa);
    }

    /**
     * 保存历史数组
     */
    private void saveHistoryMatrix() {
        mScoreHistory = BaseApplication.SCORE;
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                mGameMatrixHistory[i][j] = mGameMatrix[i][j].getCardNum();
            }
        }
    }


    private int getDeviceDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return (int) metrics.density;
    }


    /**
     * 根据偏移量判断移动方向
     *
     * @param offsetX offsetX
     * @param offsetY offsetY
     */
    private void judgeDirection(int offsetX, int offsetY) {
        int density = getDeviceDensity();
        int slideDis = 5 * density;
        int maxDis = 200 * density;
        boolean flagNormal = Math.abs(offsetX) > slideDis
                || Math.abs(offsetY) > slideDis
                && Math.abs(offsetX) < maxDis
                && Math.abs(offsetY) < maxDis;
        boolean flagSuper = Math.abs(offsetX) > maxDis ||
                Math.abs(offsetY) > maxDis;
        if (flagNormal && !flagSuper) {
            if (Math.abs(offsetX) > Math.abs(offsetY)) {
                if (offsetX > slideDis) {
                    //往右滑
                    swipeRight();
                } else {
                    //往左滑
                    swipeLeft();
                }
            } else {
                if (offsetY > slideDis) {
                    //往下滑
                    swipeDown();
                } else {
                    //往上滑
                    swipeUp();
                }
            }
        }
    }

    /**
     * 滑动事件: 上
     */
    private void swipeUp() {

    }

    /**
     * 滑动事件: 下
     */
    private void swipeDown() {


    }

    /**
     * 滑动事件: 左
     */
    private void swipeLeft() {

    }


    /**
     * 滑动事件: 右
     */
    private void swipeRight() {
        for (int i = mGameLines - 1; i >= 0; i--) {
            for (int j = mGameLines - 1; j >= 0; j--) {
                int currentNum = mGameMatrix[i][j].getCardNum();
                if (currentNum != 0){
                }
            }
        }
    }
}

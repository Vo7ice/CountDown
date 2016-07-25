package com.cn.guojinhu.game2048.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    private onUIChangeListener mListener;


    public void setOnUIChangeListener(onUIChangeListener listener) {
        mListener = listener;
    }

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
                judgeDirection(mEndX - mStartX, mEndY - mStartY);
                if (isMoved()) {
                    addRandomNum();//新增item
                    //通知activity刷新分数
                    if (null != mListener) {
                        mListener.onScoreChanged(BaseApplication.SCORE);
                    }
                }
                checkCompleted();

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
        addRandomNum();
        addRandomNum();
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

        initGameView(BaseApplication.mItemSize);
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

    /**
     * 撤销上次移动
     */
    public void revertGame() {
        int sum = 0;
        for (int[] element : mGameMatrixHistory) {
            for (int i : element) {
                sum += i;
            }
        }

        if (sum != 0) {
            if (null != mListener) {
                mListener.onScoreChanged(mScoreHistory);
                BaseApplication.SCORE = mScoreHistory;
            }
            for (int i = 0; i < mGameLines; i++) {
                for (int j = 0; j < mGameLines; j++) {
                    mGameMatrix[i][j].setCardNum(mGameMatrixHistory[i][j]);
                }
            }
        }
    }

    public boolean isRevertEnable() {
        return false;
    }

    /**
     * 获取设备的像素密度
     *
     * @return
     */
    private int getDeviceDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return (int) metrics.density;
    }

    /**
     * 判断是否移动过(是否需要新增item)
     */
    private boolean isMoved() {
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                if (mGameMatrixHistory[i][j] != mGameMatrix[i][j].getCardNum()) {
                    return true;
                }
            }
        }
        return false;
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

    private static final int FINISH = 0;
    private static final int GO_ON = 1;
    private static final int SUCCESS = 2;

    /**
     * 检测所有数字 看是否有满足条件的
     *
     * @return 0:结束 1:正常 2:成功
     */
    private int checkNums() {
        getBlanks();
        if (mBlanks.size() == 0) {
            for (int i = 0; i < mGameLines; i++) {
                for (int j = 0; j < mGameLines; j++) {
                    if (j < mGameLines - 1) {
                        //横向可以继续滑动
                        if (mGameMatrix[i][j].getCardNum() == mGameMatrix[i][j + 1].getCardNum()) {
                            return GO_ON;
                        }
                    }
                    if (i < mGameLines - 1) {
                        //竖向可以继续滑动
                        if (mGameMatrix[i][j].getCardNum() == mGameMatrix[i + 1][j].getCardNum()) {
                            return GO_ON;
                        }
                    }
                }
            }
            //没有可滑动的item
            return FINISH;
        }
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                if (mGameMatrix[i][j].getCardNum() == mTarget) {
                    //达到目标分数
                    return SUCCESS;
                }
            }
        }
        return GO_ON;
    }

    private void checkCompleted() {
        int result = checkNums();
        if (result == FINISH) {
            if (BaseApplication.SCORE > mHighScore) {
                SPUtils.put(mContext, BaseApplication.KEY_HIGH_SCROE, BaseApplication.SCORE);
                //刷新最高分
                if (null != mListener) {
                    mListener.onHighScoreChanged(BaseApplication.SCORE);
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Game Over!!!")
                    .setNegativeButton("Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //重新开始
                            startGame();
                        }
                    }).create().show();
            BaseApplication.SCORE = 0;
            if (null != mListener) {
                mListener.onScoreChanged(BaseApplication.SCORE);
            }
        } else if (result == SUCCESS) {
            //达到目标 1.重新开始 2.继续游戏
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Mission Completed!!!")
                    .setPositiveButton("Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //重新开始
                            startGame();
                        }
                    })
                    .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //继续游戏 修改target
                            SPUtils.put(mContext, BaseApplication.KEY_GAME_GOAL, mTarget * 2);
                            mTarget = mTarget * 2;
                            //刷新目标分数UI
                            if (null != mListener) {
                                mListener.onTargetScoreChanged(mTarget);
                            }
                        }
                    }).create().show();
            BaseApplication.SCORE = 0;
        }

    }


    /**
     * 滑动事件: 上
     */
    private void swipeUp() {
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                int currentNum = mGameMatrix[j][i].getCardNum();
                if (currentNum != 0) {//有数字的view
                    if (mKeyItemNum == -1) {//如果还未赋值
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {//当数值相同,加到总分
                            mCalList.add(mKeyItemNum * 2);
                            BaseApplication.SCORE += mKeyItemNum * 2;
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            //改变item值
            for (int j = 0; j < mCalList.size(); j++) {
                mGameMatrix[j][i].setCardNum(mCalList.get(j));
            }

            for (int m = mCalList.size(); m < mGameLines; m++) {
                mGameMatrix[m][i].setCardNum(0);
            }
            mKeyItemNum = -1;
            mCalList.clear();
        }
    }

    /**
     * 滑动事件: 下
     */
    private void swipeDown() {
        for (int i = mGameLines - 1; i >= 0; i--) {
            for (int j = mGameLines - 1; j >= 0; j--) {
                int currentNum = mGameMatrix[j][i].getCardNum();
                if (currentNum != 0) {//有数字的view
                    if (mKeyItemNum == -1) {//如果还未赋值
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {//当数值相同,加到总分
                            mCalList.add(mKeyItemNum * 2);
                            BaseApplication.SCORE += mKeyItemNum * 2;
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            //改变item的值
            for (int j = 0; j < mGameLines - mCalList.size(); j++) {
                mGameMatrix[j][i].setCardNum(0);
            }
            int index = mCalList.size() - 1;
            Log.d("Vo7ice", "Down---index-->" + index);
            for (int m = mGameLines - mCalList.size(); m < mGameLines; m++) {
                mGameMatrix[m][i].setCardNum(mCalList.get(index));
                index--;
            }
            //重置行参数
            mKeyItemNum = -1;
            mCalList.clear();
            index = 0;
        }
    }

    /**
     * 滑动事件: 左
     */
    private void swipeLeft() {
        for (int i = 0; i < mGameLines; i++) {
            for (int j = 0; j < mGameLines; j++) {
                int currentNum = mGameMatrix[i][j].getCardNum();
                if (currentNum != 0) {//有数字的view
                    if (mKeyItemNum == -1) {//如果还未赋值
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {//当数值相同,加到总分
                            mCalList.add(mKeyItemNum * 2);
                            BaseApplication.SCORE += mKeyItemNum * 2;
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            //改变item值
            for (int j = 0; j < mCalList.size(); j++) {
                mGameMatrix[i][j].setCardNum(mCalList.get(j));
            }

            for (int m = mCalList.size(); m < mGameLines; m++) {
                mGameMatrix[i][m].setCardNum(0);
            }
            mKeyItemNum = -1;
            mCalList.clear();
        }
    }


    /**
     * 滑动事件: 右
     */
    private void swipeRight() {
        for (int i = mGameLines - 1; i >= 0; i--) {
            for (int j = mGameLines - 1; j >= 0; j--) {
                int currentNum = mGameMatrix[i][j].getCardNum();
                if (currentNum != 0) {//有数字的view
                    if (mKeyItemNum == -1) {//如果还未赋值
                        mKeyItemNum = currentNum;
                    } else {
                        if (mKeyItemNum == currentNum) {//当数值相同,加到总分
                            mCalList.add(mKeyItemNum * 2);
                            BaseApplication.SCORE += mKeyItemNum * 2;
                            mKeyItemNum = -1;
                        } else {
                            mCalList.add(mKeyItemNum);
                            mKeyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (mKeyItemNum != -1) {
                mCalList.add(mKeyItemNum);
            }
            //改变item的值
            for (int j = 0; j < mGameLines - mCalList.size(); j++) {
                mGameMatrix[i][j].setCardNum(0);
            }
            int index = mCalList.size() - 1;
            Log.d("Vo7ice", "Right---index-->" + index);
            for (int m = mGameLines - mCalList.size(); m < mGameLines; m++) {
                mGameMatrix[i][m].setCardNum(mCalList.get(index));
                index--;
            }
            //重置行参数
            mKeyItemNum = -1;
            mCalList.clear();
            index = 0;
        }
    }

    public interface onUIChangeListener {

        void onScoreChanged(int score);

        void onHighScoreChanged(int score);

        void onTargetScoreChanged(int target);

    }
}

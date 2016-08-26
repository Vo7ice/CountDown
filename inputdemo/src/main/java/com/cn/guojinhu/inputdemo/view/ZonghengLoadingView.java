package com.cn.guojinhu.inputdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojin.hu on 2016/8/22.
 */

public class ZonghengLoadingView extends View {

    private Paint mCellPaint;
    private Cell[][] mCells = Cell.sCell;
    private int row_num = 3;
    private int column_num = 3;
    private int square = 10;
    private List<Rect> mRectList;

    public ZonghengLoadingView(Context context) {
        super(context);
        initView();
    }

    public ZonghengLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ZonghengLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ZonghengLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        mCellPaint = new Paint();
        mCellPaint.setStyle(Paint.Style.FILL);
        //mCellPaint.setColor(Color.RED);
        mRectList = new ArrayList<>();
        Rect rect0 = new Rect(-60, 20, -40, 40);
        Rect rect1 = new Rect(-20, 20, 0, 40);

        Rect rect2 = new Rect(20, 20, 40, 40);
        Rect rect3 = new Rect(60, 20, 80, 40);
        mRectList.add(rect0);
        mRectList.add(rect1);
        mRectList.add(rect2);
        mRectList.add(rect3);


    }

    private int cur = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.translate(getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < mRectList.size(); i++) {
            if (i == cur) {
                mCellPaint.setColor(Color.parseColor("#ffff8800"));
            } else {
                mCellPaint.setColor(Color.parseColor("#ffffbb33"));
            }
            canvas.drawRect(mRectList.get(i), mCellPaint);
        }
        if (cur < mRectList.size()) {
            postInvalidateDelayed(2000);
            cur++;
            
        }

    }

    private static class Cell {
        protected int row;
        protected int column;

        public static final Cell[][] sCell = createCells();

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public static Cell[][] createCells() {
            Cell[][] cells = new Cell[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    cells[i][j] = new Cell(i, j);
                }
            }
            return cells;
        }
    }

    public static class CellState {
        int row;
        int col;
        boolean isChecked;
        int square;
    }

    private int getCellColor(CellState cellState) {
        if (cellState.isChecked) {
            return android.R.color.holo_orange_dark;
        } else {
            return android.R.color.holo_orange_light;
        }
    }
}

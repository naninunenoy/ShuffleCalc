package com.example.owner.shufflecalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.GridLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnLongClickListener {

    private TextView mTvPreview;
    private Calculator mCalculator = new Calculator();
    private int[] mBtnResIds = {R.id.button7, R.id.button8, R.id.button9, R.id.button_dev,
                                R.id.button4, R.id.button5, R.id.button6, R.id.button_mul,
                                R.id.button1, R.id.button2, R.id.button3, R.id.button_dif,
                                R.id.button_dot, R.id.button0, R.id.button_eq, R.id.button_add,
                                R.id.button_del};
    private boolean mIslongClick = false;
    private MoveButton mMoveButton = new MoveButton();
    private PuzzleGridView mPuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //インスタンスを取得
        mTvPreview = (TextView) findViewById(R.id.preview);
        for(int btnID: mBtnResIds){
            findViewById(btnID).setOnTouchListener(this);
            findViewById(btnID).setOnLongClickListener(this);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // GridLayout内のアイテムをレイアウトサイズに合わせてストレッチ
        final GridLayout gl = (GridLayout) findViewById(R.id.calc_frame);
        int childWidth = gl.getWidth() / gl.getColumnCount();
        int childHeight = gl.getHeight() / gl.getRowCount();
        for(int i=0;i < gl.getChildCount(); i++){
            gl.getChildAt(i).setMinimumWidth(childWidth);
            gl.getChildAt(i).setMinimumHeight(childHeight);
        }
        PuzzleGridController pzlController = new PuzzleGridController(4,4,mBtnResIds);
        final int[] onScreen = new int[2];
        (findViewById(R.id.calc_frame)).getLocationOnScreen(onScreen);
        mPuzzle = new PuzzleGridView(gl, pzlController, onScreen[1]);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
            if(v.getId() != R.id.button_del) {
                mMoveButton.setStartPosition(v, x, y);
                mPuzzle.setOriginLayoutParams((findViewById(R.id.calc_frame)).getLayoutParams());
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if(mIslongClick) {
                if(v.getId() != R.id.button_del) {
                    mMoveButton.move(v, x, y);
                }
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
             if(mIslongClick) {
                 if(v.getId() == R.id.button_del) {
                     mPuzzle.revertButtonPosition(this);
                 } else {
                     mMoveButton.endMove(v);
                     mPuzzle.changeGridLayout(v, x, y, this);
                     mPuzzle.cancelAroundButtonsColor(this);
                 }
             } else {
                 onCalcButtonClick(v);
             }
             mIslongClick = false;
             break;
        }
        return false;
    }

    /**
     * 長押し時
     */
    @Override
    public boolean onLongClick(View v) {
        mIslongClick = true;
        if(v.getId() != R.id.button_del) {
            mPuzzle.colorAroundButtons(v, this);
            mMoveButton.setMovingAnimation(v);
        }
        return false;
    }

    public void onCalcButtonClick(View v) {
        if (v.getId() == R.id.button_del) {
            //計算をリセット
            mCalculator.reset();
            mTvPreview.setText("0");
        } else {
            //入力した値を元に計算
            String input = ((Button) v).getText().toString();
            String dispText = mCalculator.input(input);
            //計算結果をTextViewに表示
            if (!TextUtils.isEmpty(dispText)) {
                mTvPreview.setText(dispText);
            }
        }
    }
}
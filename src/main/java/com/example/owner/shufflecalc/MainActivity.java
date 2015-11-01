package com.example.owner.shufflecalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.GridLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnLongClickListener {

    private TextView mTvPreview;
    private Calculator mCalculator = new Calculator();
    private int[] mBtnResIds = {R.id.button0, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.button_dot, R.id.button_eq, R.id.button_add, R.id.button_dif, R.id.button_mul, R.id.button_dev,
            R.id.button_del};
    private boolean longClickFlg = false;
    int currentX;
    int currentY;
    int offsetX;
    int offsetY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //インスタンスを取得
        mTvPreview = (TextView) findViewById(R.id.preview);
        for(int i : mBtnResIds){
            findViewById(mBtnResIds[i]).setOnTouchListener(this);
            findViewById(mBtnResIds[i]).setOnLongClickListener(this);
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
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
             currentX = v.getLeft();
             currentY = v.getTop();
             offsetX = x;
             offsetY = y;
             break;
        case MotionEvent.ACTION_MOVE:
             if(longClickFlg) {
                 int diffX = offsetX - x;
                 int diffY = offsetY - y;
                 currentX = currentX - diffX;
                 currentY = currentY - diffY;
                 v.layout(currentX, currentY, currentX + v.getWidth(), currentY + v.getHeight());
                 offsetX = x;
                 offsetY = y;
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
             if(longClickFlg) {
                 v.setBackgroundResource(R.color.base_color);
             } else {
                 onCalcButtonClick(v);
             }
             longClickFlg = false;
             break;
        }
        return false;
    }

    /**
     * 長押し時
     */
    @Override
    public boolean onLongClick(View v) {
        longClickFlg = true;
        // 状態の変化を視覚的に分かるように色の変更
        v.setBackgroundResource(R.color.selected_color);
        //アニメーションの設定
        AnimationSet animationSet = new AnimationSet(false);
        // スケール
        ScaleAnimation scale = new ScaleAnimation(1,1.2f,1,1.2f, 0, 0);
        scale.setDuration(300);
        animationSet.addAnimation(scale);
        // 透過
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(300);
        animationSet.addAnimation(alpha);
        v.startAnimation(animationSet);
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
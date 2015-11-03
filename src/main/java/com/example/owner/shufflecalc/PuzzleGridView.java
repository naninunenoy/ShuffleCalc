package com.example.owner.shufflecalc;

import java.util.ArrayList;
import java.util.Objects;

import android.support.v7.widget.GridLayout;
//import android.support.v7.widget.GridLayout.LayoutParams;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by Nakano Yosuke on 2015/11/02.
 */
public class PuzzleGridView {
    private GridLayout mGridLayout;
    private PuzzleGridController mController;
    private View mMovingButton;
    private ViewGroup.LayoutParams mOriginLayoutParam;
    private ViewGroup.LayoutParams mLayoutParam;
    private int mNormalButtonBackGround;
    private int mMovingButtonBackGround;

    PuzzleGridView(GridLayout gridLayout, PuzzleGridController controller){
        mGridLayout = gridLayout;
        mController = controller;
        mNormalButtonBackGround = R.drawable.selector_button;
        mMovingButtonBackGround = R.color.hilight_color;
    }

    // 移動を開始したボタンの周りのボタンをハイライトする
    public void colorAroundButtons(View button, Context context){
        int btnID = button.getId();
        ArrayList<Integer> idList = mController.getAroundButtonIDs(btnID);
        for(int id : idList){
            View btn = ((com.example.owner.shufflecalc.MainActivity) context).findViewById(id);
            btn.setBackgroundResource(mMovingButtonBackGround);
        }
    }

    // 移動でオーバーされたボタンの位置を入れ替える
    public void changeGridLayout(View button, final int x, final int y, Context context){
        int gridWidth = mGridLayout.getMinimumWidth();
        int gridHeight = mGridLayout.getMinimumHeight();
        int gridX = x / gridWidth;
        int gridY = y / gridHeight;
        // 移動中に下に来たボタンのIDを取得
        int underButtonID = mController.getButtonIDByGridXY(gridX, gridY);
        if(underButtonID == mController.INVALID_VAL){
            return;
        }
        int buttonID = button.getId();
        if(buttonID != underButtonID){
            if(mController.isAroundButton(underButtonID, buttonID)){
                mController.swapID(underButtonID, buttonID);
                renewLayoutParam(context);
            }
        }

    }

    // ボタンの位置を入れ替える

    // 移動をキャンセルして元の位置に戻す
    public void cancelAroundButtonsColor(Context context){
        // ボタンの色を戻す
        for(int id : mController.getAllButtonIDs()){
            View btn = ((com.example.owner.shufflecalc.MainActivity) context).findViewById(id);
            btn.setBackgroundResource(mNormalButtonBackGround);
        }
        // 元の場所に戻す
        ((com.example.owner.shufflecalc.MainActivity) context).findViewById(R.id.calc_frame).setLayoutParams(mOriginLayoutParam);
    }

    // 移動前の位置を保存
    public void setOriginLayoutParams(ViewGroup.LayoutParams params){
        mOriginLayoutParam = params;
        mLayoutParam = params;
    }

    private void renewLayoutParam(Context context){
//        for(int i=0; i < mController.getButtonMap().size(); i++){
//            int row = mController.getButtonMap().get(i).gridX;
//            int col = mController.getButtonMap().get(i).gridY;
//            int id = mController.getButtonMap().get(i).viewID;
//            mLayoutParam.rowSpec = GridLayout.spec(row);
//            mLayoutParam.columnSpec = GridLayout.spec(col);
//            ((com.example.owner.shufflecalc.MainActivity) context).findViewById(id).setLayoutParams(mLayoutParam);
//
//        }
    }
}

package com.example.owner.shufflecalc;

import java.util.ArrayList;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.content.Context;

/**
 * Created by Nakano Yosuke on 2015/11/02.
 */
public class PuzzleGridView {
    private GridLayout mGridLayout;
    private PuzzleGridController mController;
    private View mMovingButton;
    private int mOriginRow;
    private int mOriginCol;
    private int mNormalButtonBackGround;
    private int mMovingButtonBackGround;

    PuzzleGridView(GridLayout gridLayout, PuzzleGridController controller){
        mGridLayout = gridLayout;
        mController = controller;
        mOriginRow = 0;
        mOriginCol = 0;
        mNormalButtonBackGround = R.drawable.selector_button;
        mMovingButtonBackGround = R.color.selected_color;

    }

    // 移動を開始したボタンの周りのボタンをハイライトする
    public void colorAroundButtons(View button, Context context){
        int btnID = button.getId();
        ArrayList<Integer> idList = mController.getAroundButtonIDs(btnID);
        for(int id : idList){
            View btn = ((com.example.owner.shufflecalc.MainActivity) context).findViewById(id);
            btn.setBackgroundColor(mMovingButtonBackGround);
        }
    }

    // 移動でオーバーされたボタンの位置を入れ替える

    // ボタンの位置を入れ替える

    // 移動をキャンセルして元の位置に戻す
}

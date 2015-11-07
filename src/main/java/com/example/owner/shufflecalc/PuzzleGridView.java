package com.example.owner.shufflecalc;

import java.util.ArrayList;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Nakano Yosuke on 2015/11/02.
 */
public class PuzzleGridView {
    private GridLayout mGridLayout;
    private PuzzleGridController mController;
    private LayoutParams mOriginLayoutParam;
    private int mNormalButtonBackGround;
    private int mMovingButtonBackGround;
    private int mOffsetYOnScreen;

    /**
     * コンストラクタ
     * @param gridLayout もとになるグリッドレイアウト
     * @param controller グリッドレイアウトの位置とボタンの対応を計算するコントローラ
     * @param offsetY 画面の縦方向のオフセット
     */
    PuzzleGridView(GridLayout gridLayout, PuzzleGridController controller, int offsetY){
        mGridLayout = gridLayout;
        mController = controller;
        mNormalButtonBackGround = R.drawable.selector_button;
        mMovingButtonBackGround = R.color.hilight_color;
        mOffsetYOnScreen = offsetY;
    }

    /**
     * 指定されたボタンの周辺のボタンをハイライトする
     * @param button 指定されたボタン
     * @param context Activityのコンテクスト
     */
    public void colorAroundButtons(View button, Context context){
        int btnID = button.getId();
        ArrayList<Integer> idList = mController.getAroundButtonIDs(btnID);
        for(int id : idList){
            View btn = ((MainActivity) context).findViewById(id);
            btn.setBackgroundResource(mMovingButtonBackGround);
        }
    }

    /**
     * ボタンとボタンの位置からグリッドレイアウトの更新を行う
     * @param button ボタン
     * @param x スクリーン上の横位置
     * @param y スクリーン上の縦位置
     * @param context Activityのコンテクスト
     */
    public void changeGridLayout(View button, final int x, final int y, Context context){
        int gridWidth = mGridLayout.getWidth() / mGridLayout.getColumnCount();
        int gridHeight = mGridLayout.getHeight() / mGridLayout.getRowCount();
        int gridX = x / gridWidth;
        int gridY = (y - mOffsetYOnScreen) / gridHeight;
        // 移動中に下に来たボタンのIDを取得
        int underButtonID = mController.getButtonIDByGridXY(gridX, gridY);
        if(underButtonID == mController.INVALID_VAL){
            return;
        }
        int buttonID = button.getId();
        if(buttonID != underButtonID){
            if(mController.isAroundButton(underButtonID, buttonID)){
                if(mController.swapID(underButtonID, buttonID) != false) {
                    renewLayoutParam(context);
                }
            }
        }
    }

    /**
     * ボタンの位置を最初の配置に戻す
     * @param context Activityのコンテクスト
     */
    public void revertButtonPosition(Context context){
        mController.revertButtonMapping();
        renewLayoutParam(context);
    }

    /**
     * 移動を元に戻し、ハイライトしたボタンの色をもとに戻す
     * @param context Activityのコンテクスト
     */
    public void cancelAroundButtonsColor(Context context){
        // ボタンの色を戻す
        for(int id : mController.getAllButtonIDs()){
            View btn = ((MainActivity) context).findViewById(id);
            btn.setBackgroundResource(mNormalButtonBackGround);
        }
        // 元の場所に戻す
        ((MainActivity) context).findViewById(R.id.calc_frame).setLayoutParams(mOriginLayoutParam);
    }

    /**
     * 移動前の位置を保存する
     * @param params 初期状態のレイアウトパラメータ
     */
    public void setOriginLayoutParams(LayoutParams params){
        mOriginLayoutParam = params;
    }

    /**
     * グリッドレイアウトのパラメータを更新
     * @param context Activityのコンテクスト
     */
    private void renewLayoutParam(Context context){
        for(int i=0; i < mController.getButtonMap().size(); i++){
            int x = mController.getButtonMap().get(i).gridX;
            int y = mController.getButtonMap().get(i).gridY;
            int id = mController.getButtonMap().get(i).viewID;
            GridLayout.LayoutParams gparam = new GridLayout.LayoutParams();
            gparam.rowSpec = GridLayout.spec(y);
            gparam.columnSpec = GridLayout.spec(x);
            ((MainActivity) context).findViewById(id).setLayoutParams(gparam);
        }
    }
}

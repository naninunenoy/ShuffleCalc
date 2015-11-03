package com.example.owner.shufflecalc;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * Created by Nakno Yousuke on 2015/11/01.
 */
public class MoveButton {
    private int mOffsetX;
    private int mOffsetY;
    private int mButtonPositionX;
    private int mButtonPositionY;
    private int mNormalButtonBackGround;
    private int mMovingButtonBackGround;

    /**
     * コンストラクタ
     */
    MoveButton(){
        mOffsetX = 0;
        mOffsetY = 0;
        mButtonPositionX = 0;
        mButtonPositionY = 0;
        mNormalButtonBackGround = R.drawable.selector_button;
        mMovingButtonBackGround = R.color.selected_color;
    }
    /**
     * 移動開始位置を登録する
     * @param button [in] 移動を開始するボタン
     * @param x [in] スクリーン原点の移動開始横位置
     * @param y [in] スクリーン原点の移動開始縦位置
     */
    public void setStartPosition(final View button, final int x, final int y){
        mButtonPositionX = (int) button.getX();
        mButtonPositionY = (int) button.getY();
        mOffsetX = x;
        mOffsetY = y;
    }
    /**
     * ボタン移動処理
     * @param button [in] 移動をするボタン
     * @param x [in] スクリーン原点の移動開始横位置
     * @param y [in] スクリーン原点の移動開始縦位置
     */
    public void move(View button, int x, int y){
        int diffX = mOffsetX - x;
        int diffY = mOffsetY - y;
        mButtonPositionX -= diffX;
        mButtonPositionY -= diffY;
        button.bringToFront();
        button.layout(mButtonPositionX, mButtonPositionY,
                mButtonPositionX + button.getWidth(),
                mButtonPositionY + button.getHeight());
        mOffsetX = x;
        mOffsetY = y;
    }

    /**
     * 移動の後処理
     * @param button 移動を完了したボタン
     */
    public void endMove(View button){
        button.setBackgroundResource(mNormalButtonBackGround);
    }
    /**
     * ボタン移動するときの描画を設定する
     * @param button 移動するボタン
     */
    public void setMovingAnimation(View button){
        // 状態の変化を視覚的に分かるように色の変更
        button.setBackgroundResource(mMovingButtonBackGround);
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
        button.startAnimation(animationSet);
    }
}

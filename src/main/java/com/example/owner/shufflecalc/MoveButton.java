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
    private int mNormalColor;
    private int mMovingColor;

    MoveButton(int defaultColor, int movingColor){
        mOffsetX = 0;
        mOffsetY = 0;
        mButtonPositionX = 0;
        mButtonPositionY = 0;
        mNormalColor = defaultColor;
        mMovingColor = movingColor;
    }

    public void SetStartPosition(View button, int x, int y){
        mButtonPositionX = (int) button.getX();
        mButtonPositionY = (int) button.getY();
        mOffsetX = x;
        mOffsetY = y;
        return;
    }

    public void Move(View button, int x, int y){
        int diffX = mOffsetX - x;
        int diffY = mOffsetY - y;
        mButtonPositionX -= diffX;
        mButtonPositionY -= diffY;
        button.layout(mButtonPositionX, mButtonPositionY,
                mButtonPositionX + button.getWidth(),
                mButtonPositionY + button.getHeight());
        mOffsetX = x;
        mOffsetY = y;
        return;
    }

    public void EndMove(View button){
        button.setBackgroundResource(mNormalColor);
        return;
    }

    public void SetMovingAnimation(View button){
        // 状態の変化を視覚的に分かるように色の変更
        button.setBackgroundResource(mMovingColor);
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
        return;
    }
}

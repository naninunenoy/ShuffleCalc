package com.example.owner.shufflecalc;

import java.util.ArrayList;

/**
 * Created by owner on 2015/11/01.
 */
public class PuzzleGrid {
    class ButtonPosition {
        public int viewID;
        public int gridX;
        public int gridY;
        ButtonPosition(){
            viewID = INVALID_VAL;
            gridX = 0;
            gridY = 0;
        }
        ButtonPosition(int id, int x, int y){
            viewID = id;
            gridX = x;
            gridY = y;
        }
    }

    private int mGirdSizeX;
    private int mGirdSizeY;
    private ArrayList<ButtonPosition> mButtonMapping;
    private final int INVALID_VAL = -1;

    PuzzleGrid(int gridRowSize, int gridColSize, int ids[]){
        mGirdSizeX = gridRowSize;
        mGirdSizeY = gridColSize;
        // idsの順番とボタンの配置が一致している前提
        int cell = 0;
        for(int col=0; col < mGirdSizeY; col++) {
            for(int row=0; row < mGirdSizeX; row++){
                ButtonPosition btnPos = new ButtonPosition(ids[cell], row, col);
                mButtonMapping.add(btnPos);
                cell++;
            }
        }
    }






    private boolean swapID(int id1, int id2){
        int index1 = searchButtonMappingID(id1);
        int index2 = searchButtonMappingID(id2);
        if((index1 == INVALID_VAL) || (index2 == INVALID_VAL)){
            return false;
        }
        mButtonMapping.get(index1).viewID = id2;
        mButtonMapping.get(index2).viewID = id1;
        return true;
    }

    private ArrayList<Integer> getAroundButtonIDs(ButtonPosition pos){
        ArrayList<Integer> idList = new ArrayList<Integer>(4);  // 上左右下の順
        // リスト初期化
        for (int id: idList) {
            id = INVALID_VAL;
        }
        // 上のIDを取得
        if(pos.gridY - 1 > 0){
            int mapIndex = getButtonMappingIndex(pos.gridX - 1, pos.gridY);
            if(mapIndex!= INVALID_VAL) {
                idList.set(0, mButtonMapping.get(mapIndex).viewID);
            }
        }
        // 左のIDを取得
        if(pos.gridX - 1 > 0){
            int mapIndex = getButtonMappingIndex(pos.gridX, pos.gridY - 1);
            if(mapIndex!= INVALID_VAL) {
                idList.set(1, mButtonMapping.get(mapIndex).viewID);
            }
        }
        // 右のIDを取得
        if(pos.gridX + 1 < mGirdSizeX){
            int mapIndex = getButtonMappingIndex(pos.gridX + 1, pos.gridY);
            if(mapIndex!= INVALID_VAL) {
                idList.set(2, mButtonMapping.get(mapIndex).viewID);
            }
        }
        // 下のIDを取得
        if(pos.gridY + 1 < mGirdSizeY){
            int mapIndex = getButtonMappingIndex(pos.gridX, pos.gridY + 1);
            if(mapIndex!= INVALID_VAL) {
                idList.set(3, mButtonMapping.get(mapIndex).viewID);
            }
        }
        return idList;
    }

    private int getButtonMappingIndex(int x, int y){
        int cellIndex = INVALID_VAL;
        for(ButtonPosition pos : mButtonMapping){
            if((pos.gridX == x) && (pos.gridY == y)){
                cellIndex = mButtonMapping.indexOf(pos);
                break;
            }
        }
        return cellIndex;
    }

    private int searchButtonMappingID(int id){
        int cellIndex = INVALID_VAL;
        for(ButtonPosition pos : mButtonMapping){
            if(pos.viewID == id){
                cellIndex = mButtonMapping.indexOf(pos);
                break;
            }
        }
        return cellIndex;
    }
}

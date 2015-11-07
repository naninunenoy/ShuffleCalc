package com.example.owner.shufflecalc;

import java.io.IOError;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Yosuke Nakano on 2015/11/01.
 */
public class PuzzleGridController {
    class ButtonPosition implements Cloneable {
        public int viewID;
        public int gridX;
        public int gridY;

        ButtonPosition() {
            viewID = INVALID_VAL;
            gridX = 0;
            gridY = 0;
        }

        ButtonPosition(final int id, final int x, final int y) {
            viewID = id;
            gridX = x;
            gridY = y;
        }

        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError(e.toString());
            }
        }
    }

    private int mGirdSizeX;
    private int mGirdSizeY;
    private ArrayList<ButtonPosition> mButtonMapping = new ArrayList<>();
    private final ArrayList<ButtonPosition> mInitialButtonMapping;
    protected final int INVALID_VAL = -1;

    /**
     * コンストラクタ
     * @param gridRowSize [in] 作成するボタン群の列数
     * @param gridColSize [in] 作成するボタン群の行数
     * @param ids [in] 登録するボタンIDの配列(左上からの配置順になっている想定)
     */
    PuzzleGridController(final int gridRowSize, final int gridColSize, final int ids[]){
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
        // 初期設定を保存しておく
        mInitialButtonMapping = new ArrayList<>(mButtonMapping.size());
        for(ButtonPosition btn : mButtonMapping){
            mInitialButtonMapping.add((ButtonPosition)btn.clone());
        }
    }

    /**
     * 配置されているボタンのIDを交換する
     * @param id1 [in] ViewのID
     * @param id2 [in] ViewのID
     * @return 交換の成否
     */
    public boolean swapID(final int id1, final int id2){
        int index1 = searchButtonMappingID(id1);
        int index2 = searchButtonMappingID(id2);
        if((index1 == INVALID_VAL) || (index2 == INVALID_VAL)){
            return false;
        }
        mButtonMapping.get(index1).viewID = id2;
        mButtonMapping.get(index2).viewID = id1;
        return true;
    }

    /**
     * IDで指定されたボタンの周りにあるボタンのIDの配列を取得する
     * @param id [in]
     * @return 周りにあるボタンのIDの配列
     */
    public ArrayList<Integer> getAroundButtonIDs(final int id){
        ButtonPosition btnPos = mButtonMapping.get(searchButtonMappingID(id));
        return getAroundButtonIDs(btnPos);
    }

    /**
     * 登録されたボタンの全ID取得
     * @return 登録されたボタンの全ID
     */
    public  ArrayList<Integer> getAllButtonIDs() {
        ArrayList<Integer> idList = new ArrayList<>();
        for(int i = 0; i < mButtonMapping.size(); i++){
            idList.add(mButtonMapping.get(i).viewID);
        }
        return idList;
    }

    /**
     * グリッドの座標を指定して該当するボタンのIDを取得する
     * @param gridX 横の位置
     * @param gridY 縦の位置
     * @return 該当するボタンのID
     */
    public int getButtonIDByGridXY(final int gridX, final int gridY){
        int index = getButtonMappingIndex(gridX, gridY);
        if(index != INVALID_VAL) {
            return mButtonMapping.get(index).viewID;
        }
        return INVALID_VAL;
    }

    /**
     * 中心となるボタンの周りに該当するボタンがあるかを判定する
     * @param buttonID 周辺にあるかの判定対象になるボタンのID
     * @param centerButtonID 中心にあるボタンのID
     * @return 周辺にあるかの判定結果
     */
    public boolean isAroundButton(final int buttonID, final int centerButtonID){
        ArrayList<Integer> idList;
        idList = getAroundButtonIDs(centerButtonID);
        for(int id : idList){
            if(id == buttonID){
                return true;
            }
        }
        return false;
    }

    /**
     * グリッドの位置とボタンIDの対応を取得する
     * @return グリッドの位置とボタンIDの対応
     */
    public ArrayList<ButtonPosition> getButtonMap(){
        return mButtonMapping;
    }

    /**
     * ボタンの配置を初期状態に戻す
     */
    public void revertButtonMapping(){
        for(int i=0; i < mButtonMapping.size(); i++) {
            mButtonMapping.set(i, (ButtonPosition) mInitialButtonMapping.get(i).clone());
        }
    }
    /**
     * 指定されたIDの周辺にあるボタンのIDの配列を取得する
     * @param pos 中心となるボタンの配置とID
     * @return 周辺にあるボタンのIDの配列
     */
    private ArrayList<Integer> getAroundButtonIDs(final ButtonPosition pos){
        ArrayList<Integer> idList = new ArrayList<>();
        // 上のIDを取得
        if(pos.gridY - 1 >= 0){
            int mapIndex = getButtonMappingIndex(pos.gridX, pos.gridY - 1);
            if(mapIndex != INVALID_VAL) {
                idList.add(mButtonMapping.get(mapIndex).viewID);
            }
        }
        // 左のIDを取得
        if(pos.gridX - 1 >= 0){
            int mapIndex = getButtonMappingIndex(pos.gridX - 1, pos.gridY);
            if(mapIndex!= INVALID_VAL) {
                idList.add(mButtonMapping.get(mapIndex).viewID);
            }
        }
        // 右のIDを取得
        if(pos.gridX + 1 <= mGirdSizeX){
            int mapIndex = getButtonMappingIndex(pos.gridX + 1, pos.gridY);
            if(mapIndex!= INVALID_VAL) {
                idList.add(mButtonMapping.get(mapIndex).viewID);
            }
        }
        // 下のIDを取得
        if(pos.gridY + 1 <= mGirdSizeY){
            int mapIndex = getButtonMappingIndex(pos.gridX, pos.gridY + 1);
            if(mapIndex!= INVALID_VAL) {
                idList.add(mButtonMapping.get(mapIndex).viewID);
            }
        }
        return idList;
    }

    /**
     * xyの位置からmButtonMapping内の配列のインデックスを取得する
     * @param x [in] 横位置
     * @param y [in] 縦位置
     * @return mButtonMapping内の配列のインデックス
     */
    private int getButtonMappingIndex(final int x, final int y){
        int cellIndex = INVALID_VAL;
        for(ButtonPosition pos : mButtonMapping){
            if((pos.gridX == x) && (pos.gridY == y)){
                cellIndex = mButtonMapping.indexOf(pos);
                break;
            }
        }
        return cellIndex;
    }

    /**
     * mButtonMappingから指定されたIDのインデックスを取得する
     * @param id [in] ViewのID
     * @return mButtonMapping内の配列のインデックス
     */
    private int searchButtonMappingID(final int id){
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

package com.leo.minecleaning.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.leo.minecleaning.activity.MainActivity;
import com.leo.minecleaning.beans.GameItem;
import com.leo.minecleaning.util.ActivityUtils;
import com.leo.minecleaning.util.Constans;
import com.leo.minecleaning.util.SharePreferenceUtil;
import com.leo.minecleaning.util.Util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by leo on 2017/12/15.
 * @author leo
 */

public class GameView extends GridLayout implements View.OnTouchListener{

    private static final String TAG = "GameView";

    /**每个小card的大小*/
    private int mCardSize ;
    /**每行或每列卡片的个数*/
    private int mGridNumbers ;
    /**SharedPreference工具类*/
    private SharePreferenceUtil mSpUtils;
    /**雷的个数*/
    private int mMineNumbers;
    /**地雷的位置*/
    private boolean[][] mMineLocation;
    /**没有地雷位置的数字*/
    private int[][] mNumbersLocation;
    /**全部的card view*/
    private GameItem[][] mGameItems;

    public GameView(Context context) {
        super(context);
        initView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        mSpUtils = SharePreferenceUtil.getInstance();
        //GridLayout布局初始化，设置行列
        mGridNumbers = Constans.GRID_SIZE_VALUE[mSpUtils.getInt(SharePreferenceUtil.SP_GRID_NUMBERS,0)];
        mCardSize = Util.getMerticsWidthPixels()/mGridNumbers;
        //removeAllViews()必须放在setColumnCount()之前，否则当设置一个更小的时会error
        removeAllViews();
        setColumnCount(mGridNumbers);
        setRowCount(mGridNumbers);
        //地雷位置初始化
        initMineLocation();
        //计算数字大小
        initNumberLocation();
        //onTouch
        setOnTouchListener(this);
        //初始化分数
        Message m = new Message();
        m.what = 1;
        MainActivity.sHandler.sendMessage(m);
        mGameItems = new GameItem[mGridNumbers][mGridNumbers];
        for(int i = 0 ; i < mGridNumbers; i++){
            for(int j = 0 ; j < mGridNumbers ; j++){
                if(mMineLocation[i][j]){
                    mGameItems[i][j] = new GameItem(getContext());
                }else {
                    mGameItems[i][j] = new GameItem(getContext(),mNumbersLocation[i][j]);
                }
                addView(mGameItems[i][j],mCardSize,mCardSize);
            }
        }
    }

    private void initNumberLocation(){
        mNumbersLocation = new int[mGridNumbers][mGridNumbers];
        initNumberLocationNegative();
        //中心区域
        for(int i = 1 ; i < mGridNumbers-1 ;i++){
            for(int j = 1 ; j < mGridNumbers-1 ;j++){
                mNumbersLocation[i][j] = haveMineNumbers(i,j);
            }
        }
        int temp;
        //第一行
        for(int j = 1 ; j< mGridNumbers-1 ;j++){
            mNumbersLocation[0][j] = haveMineNumbers(0,j);
        }
        //最后一行
        for(int j = 1 ; j< mGridNumbers-1 ;j++){
            mNumbersLocation[mGridNumbers-1][j] = haveMineNumbers(mGridNumbers-1,j);
        }
        //第一列
        for(int i = 1; i < mGridNumbers -1 ;i++){
            mNumbersLocation[i][0] = haveMineNumbers(i,0);
        }
        //最后一列
        for(int i = 1; i < mGridNumbers -1 ;i++){
            mNumbersLocation[i][mGridNumbers-1] = haveMineNumbers(i, mGridNumbers-1);
        }
        //四个角
        mNumbersLocation[0][0] = haveMineNumbers(0,0);
        mNumbersLocation[0][mGridNumbers-1] = haveMineNumbers(0,mGridNumbers-1);
        mNumbersLocation[mGridNumbers-1][0] = haveMineNumbers(mGridNumbers-1,0);
        mNumbersLocation[mGridNumbers-1][mGridNumbers-1] = haveMineNumbers(mGridNumbers-1,mGridNumbers-1);

    }

    /**检查(i,j)周围有几个地雷*/
    private int haveMineNumbers(int x, int y) {
        int temp = 0;
        for(int i = x-1 ; i < x+2 ;i++){
            if(i<0 || i >= mGridNumbers){
                //第一行或最后一行只会遍历两行
            }else{

                for(int j = y - 1 ; j < y+2 ;j++){
                    if(j<0 || j>= mGridNumbers){
                        //第一列或最后一列只遍历两列
                    }else{
                        if(mMineLocation[i][j]){
                            temp++;
                        }
                    }
                }

            }
        }
        return temp;
    }

    /**周围地雷数目全部设置为-1*/
    private void initNumberLocationNegative(){
        for(int i = 0 ; i < mGridNumbers ; i++){
            for(int j = 0 ; j < mGridNumbers ;j++){
                mNumbersLocation[i][j] = -1;
            }
        }
    }

    private void initMineLocation(){
        mMineNumbers = (int) (mGridNumbers*mGridNumbers* Constans.MINE_PERCENT_VALUE[mSpUtils.getInt(SharePreferenceUtil.SP_MINE_NUMBERS,0)]);
        mMineLocation = new boolean[mGridNumbers][mGridNumbers];
        randomLocation();
    }

    /**随机选取地雷位置*/
    private void randomLocation() {
        initMineLocationFalse();
        Set<Integer> set = new HashSet<>(mMineNumbers+1);
        int size = mGridNumbers*mGridNumbers;
        while (set.size() < mMineNumbers){
            set.add((int) (Math.random() * size));
        }
        for(Integer i : set){
            mMineLocation[i/mGridNumbers][i%mGridNumbers] = true;
        }
    }

    /**地雷位置全部设为为false*/
    private void initMineLocationFalse(){
        for(int i = 0 ; i < mGridNumbers ; i++){
            for (int j = 0 ; j < mGridNumbers ;j++){
                mMineLocation[i][j] = false;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int rawY = (int) event.getRawY() - Constans.MAIN_VIEW_DISTACE_PHONETOP;
                int rawX = (int) event.getRawX();
                if(rawY/mCardSize < 0 || rawY/mCardSize > mGridNumbers-1){
                    break;
                }
                if(rawX/mCardSize < 0 || rawX/mCardSize > mGridNumbers-1){
                    break;
                }
                if(mGameItems[rawY/mCardSize][rawX/mCardSize].isMine()){
                    removeAllLayer();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("你失败了，再来一局？")
                            .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    initView();
                                }
                            })
                            .setNegativeButton("不玩了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityUtils.finishAll();
                                }
                            })
                            .setCancelable(false);
                    builder.show();
                }else {
                    if(!mGameItems[rawY/mCardSize][rawX/mCardSize].isNoLayer){
                        MainActivity.sHandler.sendMessage(new Message());
                    }
                }
                mGameItems[rawY/mCardSize][rawX/mCardSize].removeLayer();
                if(isWin()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("你真厉害，恭喜你成功了  :")
                            .setCancelable(false)
                            .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        initView();
                                    }
                            });
                    builder.create().show();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void removeAllLayer(){
        for(int i = 0 ; i < mGridNumbers; i++){
            for(int j = 0 ; j < mGridNumbers; j++){
                if(mGameItems[i][j].isMine()) {
                    mGameItems[i][j].removeLayer();
                }
            }
        }
    }

    private boolean isWin(){
        for(int i = 0 ; i < mGridNumbers ; i++){
            for(int j = 0 ; j < mGridNumbers ; j++){
                if(mGameItems[i][j].mCardShowNum!=-1 && !mGameItems[i][j].isNoLayer){
                    return false;
                }
            }
        }
        return true;
    }

    public int getmMineNumbers() {
        return mMineNumbers;
    }
}

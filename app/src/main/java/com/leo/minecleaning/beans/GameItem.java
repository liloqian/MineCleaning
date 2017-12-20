package com.leo.minecleaning.beans;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.minecleaning.R;

/**
 * Created by leo on 2017/12/15.
 * @author leo
 */
public class GameItem extends FrameLayout{
    private static final String TAG = "GameItem";

    public int mCardShowNum = -1;
    private TextView mTv;
    private ImageView mIv;
    /**掩层*/
    private View mView;
    private  LayoutParams mParams;
    private boolean isMine;
    /**掩层是否去掉*/
    public boolean isNoLayer = false;

    public GameItem(@NonNull Context context,int number) {
        super(context);
        this.mCardShowNum = number;
        initCardView();
        isMine = false;
    }

    public GameItem(@NonNull Context context){
        super(context);
        mIv = new ImageView(context);
        initCardView();
        isMine = true;
    }

    private void initCardView() {
       setBackgroundColor(Color.YELLOW);
        mParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mParams.setMargins(5,5,5,5);

        //数字或者地雷
        if(mCardShowNum != -1){
            mTv = new TextView(getContext());
            mTv.setBackgroundColor(0xfff2c17a);
            mTv.setText(mCardShowNum+"");
            mTv.setTextSize(20);
            mTv.getPaint().setFakeBoldText(true);
            mTv.setGravity(Gravity.CENTER);
            addView(mTv,mParams);
        }else {
            mIv = new ImageView(getContext());
            mIv.setBackgroundColor(0xfff2c17a);
            mIv.setImageResource(R.drawable.item_mine);
            addView(mIv,mParams);
        }
        //加上掩层
        mView = new View(getContext());
        mView.setBackgroundColor(Color.GRAY);
        addView(mView,mParams);
    }

    /**去掉掩层*/
    public void removeLayer(){
        removeView(mView);
        isNoLayer = true;
    }

    public boolean isMine() {
        return isMine;
    }

}

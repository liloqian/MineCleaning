package com.leo.minecleaning.util;

import android.content.SharedPreferences;

import com.leo.minecleaning.activity.BaseApplication;

/**
 * Created by leo on 2017/12/15.
 */

/**
 * Created by leo on 2017/12/15.
 * @author leo
 * */
public class SharePreferenceUtil {
    private static SharePreferenceUtil mInstance;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    private String spName = "MineCleaningSetting";
    public static final String SP_GRID_NUMBERS = "SP_GRID_NUMBERS";
    public static final String SP_MINE_NUMBERS = "SP_MINE_NUMBERS";

    private SharePreferenceUtil() {
        mSp = BaseApplication.getAppContext().getSharedPreferences(spName,0);
        mEditor = mSp.edit();
    }

    /**单例模式*/
    public static SharePreferenceUtil getInstance(){
        if(mInstance == null){
            synchronized (SharePreferenceUtil.class){
                if(mInstance == null){
                    mInstance = new SharePreferenceUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * by leo
     * save String type sp
     * */
    public void putString(String sp,String value){
        mEditor.putString(sp,value);
        mEditor.apply();
    }

    /**
     * by leo
     * get String type sp
     * */
    public String getString(String sp,String defalut){
        return mSp.getString(sp,defalut);
    }

    /**
     * by leo
     * save Int type sp
     * */
    public void putInt(String sp,int value){
        mEditor.putInt(sp,value);
        mEditor.apply();
    }

    /**
     * by leo
     * get Int type sp
     * */
    public int getInt(String sp,int defalut){
        return mSp.getInt(sp,defalut);
    }



}

package com.leo.minecleaning.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/12/19.
 */

public class ActivityUtils {

    private static List<Activity> mList = new ArrayList<>();

    public static void add(Activity activity){
        mList.add(activity);
    }

    public static void remove(Activity activity){
        if(mList.contains(activity)){
            mList.remove(activity);
        }
    }

    public static void finishAll(){
        for(Activity activity : mList){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }

}

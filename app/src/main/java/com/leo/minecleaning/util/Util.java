package com.leo.minecleaning.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.leo.minecleaning.activity.BaseApplication;

/**
 * Created by leo on 2017/12/15.
 * @author leo
 */
public class Util {

    /**保存一个手机屏幕像素值备份*/
    private static int windowsWidthPixels = -1;
    /**
     * 得到手机屏幕宽度像素值
     * */
    public static int getMerticsWidthPixels(){
        if(windowsWidthPixels == -1){
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) BaseApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getMetrics(metrics);
            windowsWidthPixels = metrics.widthPixels;
        }
        return windowsWidthPixels;
    }

}

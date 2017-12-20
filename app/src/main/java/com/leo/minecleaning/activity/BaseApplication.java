package com.leo.minecleaning.activity;

import android.app.Application;
import android.content.Context;

import com.leo.minecleaning.util.CrashUtils;

/**
 * Created by leo on 2017/12/15.
 */

public class BaseApplication extends Application {

    private static Context context;

    public static Context getAppContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        //crash deal
        CrashUtils.getInstance(this).init();
    }
}

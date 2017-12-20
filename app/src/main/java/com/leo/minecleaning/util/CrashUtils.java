package com.leo.minecleaning.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leo on 2017/12/15.
 */

public class CrashUtils implements Thread.UncaughtExceptionHandler{
    private static final String TAG = "CrashUtils";

    private volatile static CrashUtils mInstance;

    private Thread.UncaughtExceptionHandler mHandler;
    private Context mContext;
    private boolean mInitialized;
    private String crashDir;
    private String versionName;
    private int versionCode;
    private int crashInfo;

    private CrashUtils(Context context) {
        this.mContext = context;
    }

    public static CrashUtils getInstance(Context context){
        if(mInstance == null){
            synchronized (CrashUtils.class){
                if(mInstance == null){
                    mInstance = new CrashUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * by leo
     * init crashUtil ,this will do someWorks when crash
     * */
    public boolean init(){
        if(mInitialized){
            Log.e(TAG, "you already init once and do not init second..." );
            return true;
        }

        //get a crash dir and create new
        File baseCache = mContext.getCacheDir();
        if(baseCache == null){
            Log.e(TAG, "getCacheDir() get a null file " );
            return false;
        }

        crashDir = baseCache.getPath() + File.separator + "crash" + File.separator;
        File dir = new File(crashDir);
        if(!dir.exists()){
            dir.mkdir();
        }

        //get versionCode and version Name
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0);
            versionCode = info.versionCode;
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread t, final Throwable e) {
        String now = new SimpleDateFormat("yy-MM-dd-HH-mm-ss").format(new Date());
        final String fullPath = crashDir+now+".crash";
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, false));
                    pw.write(getCrashInfo());
                    pw.write("\n\n**********Crash Info Content ***************\n");
                    pw.write(e.toString());
                    pw.write("\n**********Crash Info Content ****************\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    pw.close();
                }
            }
        }).start();
        Log.e(TAG, "crash log dir :  " + fullPath );
        //TODO deal with crash info
        if(mHandler != null){
            mHandler.uncaughtException(t,e);
        }
    }

    public String getCrashInfo() {
        return "\n************Crash Info Head ******************" +
                "\n* Device Manufacturer: " + Build.MANUFACTURER +
                "\n* Device Model: " + Build.MODEL +
                "\n* Android Version: " + Build.VERSION.RELEASE +
                "\n* Android SDK: " + Build.VERSION.SDK_INT +
                "\n* App VersionName: " + versionName +
                "\n* App VersionCode: " + versionCode +
                "\n*************Crash Head Log******************\n";
    }
}

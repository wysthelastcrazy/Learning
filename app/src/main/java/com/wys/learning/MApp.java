package com.wys.learning;

import android.app.Application;
import android.content.Context;

import com.example.commonlib.crash.CrashHandler;
import com.example.commonlib.log.LogUtil;

public class MApp extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        String path = getExternalFilesDir("").getAbsolutePath() + "/log/";
        LogUtil.INSTANCE.initLogger(path, LogUtil.Level.VERBOSE);
        context = this;
    }
    public static Context getContext(){
        return context;
    }
}

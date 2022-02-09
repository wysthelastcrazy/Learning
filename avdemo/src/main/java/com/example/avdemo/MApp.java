package com.example.avdemo;

import android.app.Application;
import android.view.OrientationEventListener;

import com.example.commonlib.crash.CrashHandler;
import com.example.commonlib.log.LogUtil;

public class MApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        String path = getExternalFilesDir("").getAbsolutePath() + "/log/";
        LogUtil.INSTANCE.initLogger(path, LogUtil.Level.VERBOSE);

    }
}

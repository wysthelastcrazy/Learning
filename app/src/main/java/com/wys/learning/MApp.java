package com.wys.learning;

import android.app.Application;

import com.example.commonlib.crash.CrashHandler;

public class MApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}

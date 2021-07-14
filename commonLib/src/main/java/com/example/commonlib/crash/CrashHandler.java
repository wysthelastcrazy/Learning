package com.example.commonlib.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    private static String FILE_PATH = "/CrashTest/log/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;
    private boolean isInit;

    private CrashHandler() {
    }

    private static class CrashHandlerHolder {
        private static CrashHandler instance = new CrashHandler();
    }

    public static CrashHandler getInstance() {
        return CrashHandlerHolder.instance;
    }

    public void init(@NonNull Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
        FILE_PATH = mContext.getExternalFilesDir("").getAbsolutePath() + FILE_PATH;
        isInit = true;
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        if (isInit) {
            //导入异常信息到存储
            dumpExceptionToSDCard(e);
            //上传异常信息到服务器
            uploadExceptionToServer();
        }
    }
    private void dumpExceptionToSDCard(Throwable ex){
        File dir = new File(FILE_PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        File file = new File(FILE_PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
        //app 版本
        pw.print("APP Version: ");
        pw.print("versionName = " + pi.versionName);
        pw.println(",versionCode = "+pi.versionCode);
        //Android 版本
        pw.print("OS Version: ");
        pw.print("Android version = " + Build.VERSION.RELEASE);
        pw.println(",api level = " + Build.VERSION.SDK_INT);
        //手机制作商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    private void uploadExceptionToServer(){

    }
}

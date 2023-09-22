package com.example.commonlib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

/**
 *@author wangyasheng
 *@date 2023/8/28
 */
object WakeLockUtil {
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    fun wakeUpScreen(activity: Activity, isKeepOn: Boolean) {
        if (isWakeUp(activity)) {
            return
        }

        val timeOut = getScreenOffTimeOutFromSetting(activity)
        val pm = activity.getSystemService(AppCompatActivity.POWER_SERVICE) as PowerManager
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        @SuppressLint("InvalidWakeLockTag") val wl = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
            "bright"
        )
        wl.acquire(timeOut.toLong())
        wl.release()
        if (isKeepOn) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    fun wakeUpScreen(context: Context) {
        if (isWakeUp(context)) {
            return
        }
        val timeOut = getScreenOffTimeOutFromSetting(context)
        val pm = context.getSystemService(AppCompatActivity.POWER_SERVICE) as PowerManager
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        @SuppressLint("InvalidWakeLockTag") val wl = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
            "bright"
        )
        wl.acquire(timeOut.toLong())
        wl.release()
    }

    fun reset(context: Context){
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        @SuppressLint("InvalidWakeLockTag") val wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK or
                PowerManager.ON_AFTER_RELEASE or  PowerManager.ACQUIRE_CAUSES_WAKEUP, "bright")
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    fun isWakeUp(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isInteractive
    }

    private fun getScreenOffTimeOutFromSetting(context: Context): Int {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT
        )
    }
}
package com.wys.learning.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.CopyOnWriteArrayList

/**
 *@author wangyasheng
 *@date 2023/8/22
 * 屏幕静置超时
 */
object ScreenSittingTimeoutHelper{
    private const val TIME_OUT = 30 * 1000L
    private val mHandler = Handler(Looper.getMainLooper())
    private val TAG = "ScreenSittingTimeoutHelper"

    private val listeners = ArrayList<IScreenSittingTimeOutListener>()
    /**
     * 超时任务
     */
    private val operationTimeoutRunnable = Runnable {
            listeners.forEach {listener ->
                listener.onScreenTimeout()
            }
    }

    /**
     * 添加超时监听
     */
    fun addScreenTimeoutListener(listener: IScreenSittingTimeOutListener){
        listeners.add(listener)
    }

    /**
     * 移除超时监听
     */
    fun removeScreenTimeoutListener(listener: IScreenSittingTimeOutListener){
        listeners.remove(listener)
    }

    fun startOperationTimeTimer(){
        mHandler.removeCallbacks(operationTimeoutRunnable)
        mHandler.postDelayed(operationTimeoutRunnable, TIME_OUT)
    }
    fun stopOperationTimeoutTimer(){
        mHandler.removeCallbacks(operationTimeoutRunnable)
    }

    interface IScreenSittingTimeOutListener {
        fun onScreenTimeout()
    }
}
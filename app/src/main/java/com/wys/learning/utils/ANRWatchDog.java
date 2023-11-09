package com.wys.learning.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

/**
 * @author wangyasheng
 * @date 2023/11/6
 */
public class ANRWatchDog extends Thread {
    private static final String TAG = "ANRWatchDog";
    private int timeOut = 10 * 1000;
    static ANRWatchDog sWatchdog;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private ANRChecker anrChecker = new ANRChecker();
    private ANRListener anrListener;

    public static ANRWatchDog getInstance() {
        if (sWatchdog == null) {
            sWatchdog = new ANRWatchDog();
        }
        return sWatchdog;
    }

    private ANRWatchDog() {
        super("ANR-WatchDog-Thread");
    }

    public ANRWatchDog setANRListener(ANRListener listener) {
        this.anrListener = listener;
        return this;
    }

    @Override
    public void run() {
        //如果线程中断.则直接退出线程
        while (!isInterrupted()) {
            synchronized (this) {
                //任务完成变量改为false并用主线程post修改此值
                anrChecker.schedule();
                try {
                    //等待指定时间
                    wait(timeOut);
                } catch (InterruptedException e) {
                    if (anrListener != null){
                        anrListener.onInterrupted(e);
                    }
                    return;
                }
                //判断主线程修改标标志位是否成功
                if (anrChecker.isBlocked()){
                    if (anrListener != null){
                        anrListener.onAnrHappened();
                    }
                }
            }
        }
    }

    private class ANRChecker implements Runnable {
        private boolean mCompleted;

        @Override
        public void run() {
            synchronized (ANRWatchDog.this) {
                mCompleted = true;
            }
        }

        void schedule() {
            mCompleted = false;
            mainHandler.post(this);
        }

        boolean isBlocked() {
            return !mCompleted;
        }
    }

    public interface ANRListener {
        void onAnrHappened();
        void onInterrupted(InterruptedException e);
    }
}

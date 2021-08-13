package com.example.androidbase.media;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CommonSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Thread mThread;
    private boolean isRunning;

    public CommonSurfaceView(Context context) {
        this(context,null);
    }

    public CommonSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CommonSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHolder = getHolder();
        mHolder.addCallback(this);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning){
            draw();
        }
    }
    private void draw(){
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null){
                //do something
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }
}

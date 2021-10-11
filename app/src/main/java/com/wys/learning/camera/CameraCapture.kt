package com.wys.learning.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.Size
import com.example.commonlib.log.LogUtil
import kotlin.math.abs


abstract class CameraCapture constructor(val context: Context): ICapture {
    companion object{
        private const val TAG = "CameraCapture"
    }
    enum class State{
        IDLE,
        OPENING,
        OPENED,
        STARTING,
        STARTED,
        STOPPING,
        STOPPED,
        CLOSING
    }
    //状态
    var mState: State = State.IDLE
    //surfaceTexture
    protected var mSurfaceTexture: SurfaceTexture? = null
    //listener
    var mCaptureListener : ICaptureListener? = null

    //默认理想宽高
    private var mWantedWidth: Int = 1080
    private var mWantedHeight: Int = 720

    //真实的摄像头预览、采集宽高
    protected var mActualWidth = 0
    protected var mActualHeight = 0

    //是否使用前置摄像头，默认false即默认使用后置摄像头
    protected var mFacing: Boolean = false
    //是否设置镜像
    protected var mMirror: Boolean = false

    fun setSurfaceTexture(surfaceTexture: SurfaceTexture){
        mSurfaceTexture = surfaceTexture
    }

    fun setCaptureListener(listener: ICaptureListener){
        mCaptureListener = listener
    }

    /**
     * 设置想要的预览宽高
     */
    fun setWantedSize(wantedWidth: Int, wantedHeight: Int){
        LogUtil.i(TAG,"setWantedSize wantedWidth = $wantedWidth, wantedHeight = $wantedHeight")
        mWantedWidth = wantedWidth
        mWantedHeight = wantedHeight
    }
    fun setFacing(facing: Boolean){
        LogUtil.i(TAG,"setFacing facing = $facing")
        mFacing = facing
    }
    fun setMirror(mirror: Boolean){
        mMirror = mirror
    }

    override fun open() {
        LogUtil.i(TAG,"open++++++++++++++++")
        if (mState != State.IDLE){
            LogUtil.e(TAG,"state is not IDLE  currState = $mState")
            return
        }
        mState = State.OPENING
        onOpening()
    }
    /**
     * 真正的open操作在此方法进行，不同的设备操作不同
     */
    abstract fun onOpening()

    /**
     * open成功之后的处理
     */
    fun onOpened(){
        LogUtil.i(TAG,"onOpened+++++++++++++++++")
        if (mState != State.OPENING){
            LogUtil.e(TAG,"state is not OPENING  currState = $mState")
            return
        }
        mState = State.OPENED
        mCaptureListener?.onOpened()
    }

    override fun start() {
        LogUtil.i(TAG,"start+++++++++++++++++")
        if (mState != State.OPENED){
            LogUtil.e(TAG,"state is not OPENED  currState = $mState")
            return
        }
        mState = State.STARTING
        onStarting()
    }

    /***
     * 正在的start操作，由具体子类实现
     */
    abstract fun onStarting()

    /**
     * start成功之后的处理
     */
    fun onStarted(){
        LogUtil.i(TAG,"onStarted+++++++++++")
        if (mState != State.STARTING){
            LogUtil.e(TAG,"state is not STARTING  currState = $mState")
            return
        }
        mState = State.STARTED
        mCaptureListener?.onStarted()
    }

    override fun stop() {
        LogUtil.i(TAG,"stop++++++++++++++++")
        if (mState != State.STARTING && mState != State.STARTED){
            LogUtil.e(TAG,"state is not STARTING or STARTED  currState = $mState")
            return
        }
        mState = State.STOPPING
        onStopping()
    }

    /**
     * 正在的stop操作，由具体子类实现
     */
    abstract fun onStopping()

    /**
     * stop成功之后的处理
     */
    fun onStopped(){
        LogUtil.i(TAG,"onStopped+++++++++++++++++")
        if (mState != State.STOPPING){
            LogUtil.e(TAG,"state is not STOPPING  currState = $mState")
            return
        }
        mState = State.STOPPED
        mCaptureListener?.onStopped()
    }

    override fun close() {
        LogUtil.i(TAG,"close+++++++++++++++++++++")
        if (mState == State.IDLE || mState == State.CLOSING){
            LogUtil.e(TAG,"state is IDLE or CLOSING  currState = $mState")
            return
        }
        mState = State.CLOSING
        onClosing()
    }

    /**
     * 真正的close操作，由具体子类实现
     */
    abstract fun onClosing()

    /**
     * close成功后的处理
     */
    fun onClosed(){
        LogUtil.i(TAG,"onClosed+++++++++++++")
        if (mState != State.CLOSING){
            LogUtil.e(TAG,"state is not CLOSING  currState = $mState")
            return
        }
        mState = State.IDLE
        mCaptureListener?.onClosed()
    }

    /**
     * 预览数据回调
     */
    fun onPreviewFrame(frameDate: ByteArray){
    }

    protected fun calculateSize(sizes: Array<Size?>): Size{
        var minDiff = Int.MAX_VALUE
        var matchedWidth = mWantedWidth
        var matchedHeight = mWantedHeight

        sizes.forEach { size ->
            size?.let {
                val diff = abs(size.height - mWantedHeight) + abs(size.width - mWantedWidth)
                if (diff < minDiff){
                    minDiff = diff
                    matchedWidth = size.width
                    matchedHeight = size.height
                }
            }
        }
        if (minDiff == Int.MAX_VALUE){
            LogUtil.e(TAG,"Couldn't find resolution close to ( $mWantedWidth + x + $mWantedHeight )")
            return sizes[0]!!
        }
        LogUtil.i(TAG,"allocate: matched ($matchedWidth x $matchedHeight)")

        return Size(matchedWidth,matchedHeight)
    }

    /**
     * 获取摄像头方向
     */
    abstract fun getCameraOrientation(): Int


}
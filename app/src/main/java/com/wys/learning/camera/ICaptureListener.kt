package com.wys.learning.camera

interface ICaptureListener {

    fun onOpened()

    fun onStarted()

    fun onStopped()

    fun onClosed()

    /**
     * 预览数据回调
     */
    fun onPreviewFrame(frameDate: ByteArray, type: Int, width: Int, height: Int)
}
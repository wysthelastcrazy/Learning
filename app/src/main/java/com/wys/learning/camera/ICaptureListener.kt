package com.wys.learning.camera

interface ICaptureListener {

    fun onOpened()

    fun onStarted()

    fun onStopped()

    fun onClosed()
}
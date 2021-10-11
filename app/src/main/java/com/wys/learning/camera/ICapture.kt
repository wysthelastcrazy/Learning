package com.wys.learning.camera

/**
 * 采集
 */
interface ICapture {
    /**open camera or other devices */
    fun open()

    /**start capture */
    fun start()

    /**stop capture */
    fun stop()

    /**close camera or other devices */
    fun close()
}
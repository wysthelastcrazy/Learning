package com.wys.learning.openGL.render

/**
 * 渲染器，用于将获取到的帧数据绘制到Surface上
 */
abstract class BaseEGLRender {

    /**
     * Surface被创建时调用，用来初始化一些参数
     */
    fun onSurfaceCreated(){
        onCreated()
    }

    /**
     * Surface大小发生变化时调用
     */
    fun onSurfaceChanged(width: Int, height: Int){

    }

    /**
     * Surface更新每一帧时调用，用来绘制（显示）画面
     */
    fun onSurfaceDrawFrame(){
        onDrawFrame()
    }

    protected abstract fun onCreated()

    protected abstract fun onDrawFrame()

}
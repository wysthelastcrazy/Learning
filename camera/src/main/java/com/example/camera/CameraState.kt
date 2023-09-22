package com.example.camera

/**
 *@author wangyasheng
 *@date 2023/9/13
 * 相机状态枚举类
 */
enum class CameraState {
    //空闲
    IDLE,
    //正在打开camera
    OPENING,
    //camera已打开
    OPENED,
    //正在开启预览
    STARTING,
    //预览已开启
    STARTED,
    //正在停止预览
    STOPPING,
    //预览已停止
    STOPPED,
    //正在关闭camera
    CLOSING
}
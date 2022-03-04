package com.example.avdemo.utils

import java.text.SimpleDateFormat
import java.util.*

object DataUtils {
    fun longToString(timestamp: Long): String {
//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return "${sdf.format(Date(timestamp))}-$timestamp" // 时间戳转换成时间
    }
}
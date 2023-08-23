package com.wys.learning.utils

import android.content.Intent
import androidx.core.view.ContentInfoCompat.Flags
import com.wys.learning.MApp

/**
 *@author wangyasheng
 *@date 2023/8/22
 */
object IntentUtils {
    fun goToKeyboardActivity(){

        val intent = Intent("com.keyboard")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        MApp.getContext().startActivity(intent)
    }
}
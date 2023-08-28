package com.wys.learning

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.commonlib.views.FaceView
import com.example.commonlib.views.KeyboardView
import com.wys.learning.utils.IntentUtils
import com.wys.learning.utils.ScreenSittingTimeoutHelper

class KeyboardActivity : AppCompatActivity(),
    ScreenSittingTimeoutHelper.IScreenSittingTimeOutListener {
    private lateinit var faceView: FaceView
    private lateinit var keyboardView:KeyboardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        faceView = findViewById(R.id.faceView)
        keyboardView = findViewById(R.id.keyboard)


        val rect = Rect(200,100,500,300)

        faceView.addRect(rect)

        val btn:Button = findViewById(R.id.btn_goto)
//        btn.visibility = View.GONE
        btn.setOnClickListener {
//            IntentUtils.goToKeyboardActivity()
//            val rect2 = Rect(300,500,500,900)
//            faceView.clear()
//            faceView.addRect(rect2)
//            faceView.invalidate()
            keyboardView.performClick('1')
        }
        ScreenSittingTimeoutHelper.addScreenTimeoutListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ScreenSittingTimeoutHelper.removeScreenTimeoutListener(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val pressedChar = event!!.unicodeChar.toChar()
        keyboardView.performClick(pressedChar)
        return true
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN){
            ScreenSittingTimeoutHelper.stopOperationTimeoutTimer()
        }else if (ev?.action == MotionEvent.ACTION_UP || ev?.action == MotionEvent.ACTION_CANCEL){
            ScreenSittingTimeoutHelper.startOperationTimeTimer()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onScreenTimeout() {
        Log.d("KeyboardActivity","onScreenTimeout++")
    }
}
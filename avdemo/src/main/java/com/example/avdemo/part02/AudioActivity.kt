package com.example.avdemo.part02

import android.Manifest
import android.media.AudioFormat
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.avdemo.R
import com.example.avdemo.part04.MediaHelper
import com.example.commonlib.utils.Rx

class AudioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
        requestPermissions()

//        Rx.runIO {
//            val helper = MediaHelper()
//            helper.init("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4")
//        }
    }


    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 0x100)
        }
    }

    fun startRecord(view: View) {
        val path = getExternalFilesDir("")?.absolutePath + "/audio/"
        Log.d("AudioActivity", "path = $path")
        val fileName = "pcm001_${System.currentTimeMillis()}"
        Log.d("AudioActivity", "fileName = $fileName")
        AudioRecordHelperV2.initRecorder(
            path, fileName, MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        AudioRecordHelperV2.startRecord()
    }

    fun stopRecord(view: View) {
        AudioRecordHelperV2.stopRecord()
    }
    private val mThread = HandlerThread("audio")

}
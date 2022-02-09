package com.example.avdemo.part03

import android.media.AudioFormat
import android.media.AudioTrack
import com.example.commonlib.log.LogUtil
import java.io.File


/**
 * 使用AudioTrack播放PCM音频数据
 */
object AudioTrackHelper {
    private const val TAG = "AudioTrackHelper"

    enum class State {
        IDLE,
        INITIALIZING,
        INITIALIZED,
        PLAYING,
        STOPPING,
        STOPPED
    }
    @Volatile
    private var mState: State = State.IDLE

    fun initAudioPlayer(
        filePath: String,
        streamType: Int,
        sampleRateInHz: Int,
        channelConfig: Int,
        audioFormat: Int,
        playMode: Int
    ) {

    }

    fun startPlay() {

    }

    fun stopPlay() {

    }

    fun release() {

    }

    private class AudioPlayThread(
        val filePath: String,
        val streamType: Int,
        val sampleRateInHz: Int,
        val channelConfig: Int,
        val audioFormat: Int,
        val playMode: Int
    ) : Thread() {
        private companion object{
            const val TAG = "AudioPlayThread"
        }
        private val minBufferSize =
            AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)
        private var mAudioTrack: AudioTrack = AudioTrack(
            streamType,
            sampleRateInHz,
            channelConfig,
            audioFormat,
            minBufferSize,
            playMode
        )

        override fun run() {
            mAudioTrack.play()
        }
        private fun play(data:ByteArray, offsetInBytes: Int, sizeInBytes: Int){
            if (mAudioTrack != null && mState == AudioTrackHelper.State.PLAYING){
                mAudioTrack.write(data,offsetInBytes,sizeInBytes)
            }
        }
        fun startPlay(){
           val pcmFile = File(filePath)
            if (!pcmFile.exists()){
                LogUtil.e(TAG,"pcmFile is not exists! filePath = $filePath")
                return
            }
        }
        fun stopPlay(){
            mAudioTrack.stop()
            mAudioTrack.release()
        }
        fun release(){

        }
    }
}
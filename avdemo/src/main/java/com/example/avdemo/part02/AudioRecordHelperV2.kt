package com.example.avdemo.part02

import android.media.AudioFormat
import android.media.AudioRecord
import com.example.commonlib.log.LogUtil
import com.example.commonlib.utils.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * 使用AudioRecord录制PCM音频数据
 */
object AudioRecordHelperV2 {
    private const val TAG = "AudioRecordHelperV2"

    enum class State {
        IDLE,
        INITIALIZING,
        INITIALIZED,
        RECORDING,
        STOPPING,
        STOPPED
    }

    @Volatile
    private var mState: State = State.IDLE

    private var mRecordThread: RecordThread? = null

    fun initRecorder(
        path: String,
        fileName: String,
        audioSource: Int,
        sampleRateInHz: Int,
        channelConfig: Int,
        audioFormat: Int
    ) {
        if (mState != State.IDLE) {
            LogUtil.e(TAG, "initRecorder state is invalid. currState is $mState")
            return
        }
        mState = State.INITIALIZING
        val bufferSizeInBytes = AudioRecord.getMinBufferSize(
            sampleRateInHz, channelConfig,
            audioFormat
        )
        mRecordThread = RecordThread(
            audioSource,
            sampleRateInHz,
            channelConfig,
            audioFormat,
            bufferSizeInBytes,
            path,
            fileName
        )
        mState = State.INITIALIZED
    }

    fun startRecord(){
        if (mState != State.INITIALIZED){
            LogUtil.e(TAG, "startRecord state is invalid. currState is $mState")
            return
        }
        if (mRecordThread == null){
            LogUtil.e(TAG, "startRecord recordThread is null")
            mState = State.IDLE
            return
        }
        LogUtil.i(TAG, "startRecord ++++++++++++")
        mRecordThread?.startRecord()
        mState = State.RECORDING
    }
    fun stopRecord(){
        if (mState != State.RECORDING){
            LogUtil.e(TAG, "stopRecord state is invalid. currState is $mState")
            return
        }
        if (mRecordThread == null){
            LogUtil.e(TAG, "stopRecord recordThread is null")
            mState = State.IDLE
            return
        }
        LogUtil.i(TAG, "stopRecord+++++")
        mRecordThread?.stopRecord()
        mState = State.STOPPING
    }
    private fun release(){
        LogUtil.i(TAG, "stopRecord+++++")
        mRecordThread = null
        mState = State.IDLE
    }

    private class RecordThread(
        val audioSource: Int,
        val sampleRateInHz: Int,
        val channelConfig: Int,
        val audioFormat: Int,
        val bufferSizeInBytes: Int,
        val savePath: String,
        val fileName: String
    ) : Thread() {
        private companion object {
            const val TAG: String = "RecordThread"
        }

        private val mAudioRecord: AudioRecord =
            AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes)
        private val mBuffer: ByteArray = ByteArray(bufferSizeInBytes)
        private var isStopped: Boolean = false
        override fun run() {
            try {
                mAudioRecord.startRecording()
                while (!isStopped) {
                    val result = mAudioRecord.read(mBuffer, 0, mBuffer.size)
                    if (result >= 0) {
                        FileUtils.saveFile("$savePath$fileName.PCM",mBuffer,true)
                    }
                }
                mState = AudioRecordHelperV2.State.STOPPED
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                release()
            }

        }

        fun startRecord() {
            LogUtil.i(TAG, "startRecord+++++")
            start()
        }

        fun stopRecord() {
            LogUtil.i(TAG, "stopRecord+++++")
            isStopped = true
        }

        fun release() {
            LogUtil.i(TAG, "release+++++")
            if (mAudioRecord != null && mAudioRecord.state == AudioRecord.STATE_INITIALIZED){
                mAudioRecord.stop()
                mAudioRecord.release()
            }
            convertPCMToWAV(
                savePath,
                fileName,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                bufferSizeInBytes
            )
            AudioRecordHelperV2.release()
        }

        private fun convertPCMToWAV(
            path: String,
            fileName: String,
            sampleRateInHz: Int,
            channelConfig: Int,
            audioFormat: Int,
            bufferSize: Int
        ) {
            val wavFile = File(path, "$fileName.wav")
            if (wavFile.exists()) {
                wavFile.delete()
            }
            val pcmFile = File(path, "$fileName.PCM")

            val fileInputStream = FileInputStream(pcmFile)
            val fileOutputStream = FileOutputStream(wavFile)

            val audioByteLen = fileInputStream.channel.size()
            val wavByteLen = audioByteLen + 36

            addWavHeader(
                fileOutputStream,
                audioByteLen,
                wavByteLen,
                sampleRateInHz,
                channelConfig,
                audioFormat
            )

            val buffer = ByteArray(bufferSize)
            while (fileInputStream.read(buffer) != -1) {
                fileOutputStream.write(buffer)
                fileOutputStream.flush()
            }

            fileInputStream.close()
            fileOutputStream.close()
        }

        private fun addWavHeader(
            fileOutputStream: FileOutputStream,
            audioByteLen: Long,
            wavByteLen: Long,
            sampleRateInHz: Int,
            channelConfig: Int,
            audioFormat: Int
        ) {
            val header = ByteArray(44)

            // RIFF/WAVE header chunk
            header[0] = 'R'.toByte()
            header[1] = 'I'.toByte()
            header[2] = 'F'.toByte()
            header[3] = 'F'.toByte()
            header[4] = (wavByteLen and 0xff).toByte()
            header[5] = (wavByteLen shr 8 and 0xff).toByte()
            header[6] = (wavByteLen shr 16 and 0xff).toByte()
            header[7] = (wavByteLen shr 24 and 0xff).toByte()

            //WAVE
            header[8] = 'W'.toByte()
            header[9] = 'A'.toByte()
            header[10] = 'V'.toByte()
            header[11] = 'E'.toByte()

            // 'fmt ' chunk 4 个字节
            header[12] = 'f'.toByte()
            header[13] = 'm'.toByte()
            header[14] = 't'.toByte()
            header[15] = ' '.toByte()
            // 4 bytes: size of 'fmt ' chunk（格式信息数据的大小 header[20] ~ header[35]）
            header[16] = 16
            header[17] = 0
            header[18] = 0
            header[19] = 0
            // format = 1 编码方式
            header[20] = 1
            header[21] = 0
            // 声道数目
            val channelSize = if (channelConfig == AudioFormat.CHANNEL_IN_MONO) 1 else 2
            header[22] = channelSize.toByte()
            header[23] = 0
            // 采样频率
            header[24] = (sampleRateInHz and 0xff).toByte()
            header[25] = (sampleRateInHz shr 8 and 0xff).toByte()
            header[26] = (sampleRateInHz shr 16 and 0xff).toByte()
            header[27] = (sampleRateInHz shr 24 and 0xff).toByte()
            // 每秒传输速率
            val byteRate = (audioFormat * sampleRateInHz * channelSize).toLong()
            header[28] = (byteRate and 0xff).toByte()
            header[29] = (byteRate shr 8 and 0xff).toByte()
            header[30] = (byteRate shr 16 and 0xff).toByte()
            header[31] = (byteRate shr 24 and 0xff).toByte()
            // block align 数据库对齐单位，每个采样需要的字节数
            header[32] = (2 * 16 / 8).toByte()
            header[33] = 0
            // bits per sample 每个采样需要的 bit 数
            header[34] = 16
            header[35] = 0

            //data chunk
            header[36] = 'd'.toByte()
            header[37] = 'a'.toByte()
            header[38] = 't'.toByte()
            header[39] = 'a'.toByte()
            // pcm字节数
            header[40] = (audioByteLen and 0xff).toByte()
            header[41] = (audioByteLen shr 8 and 0xff).toByte()
            header[42] = (audioByteLen shr 16 and 0xff).toByte()
            header[43] = (audioByteLen shr 24 and 0xff).toByte()
            try {
                fileOutputStream.write(header, 0, 44)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }
}
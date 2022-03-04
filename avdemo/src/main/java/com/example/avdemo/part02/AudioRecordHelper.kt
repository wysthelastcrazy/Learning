package com.example.avdemo.part02

import android.media.*
import android.util.Log
import com.example.commonlib.log.LogUtil
import com.example.commonlib.utils.FileUtils
import com.example.commonlib.utils.Rx
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object AudioRecordHelper {
    private const val TAG = "AudioRecordHelper"

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
    private var mPath: String = ""
    private var mFileName: String = ""
    private lateinit var mAudioRecord: AudioRecord

    @Volatile
    private var isStopped: Boolean = false

    /**
     * 初始化录制参数
     */
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
        this.mPath = path
        this.mFileName = fileName
        val bufferSizeInBytes = AudioRecord.getMinBufferSize(
            44100, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        //构造方法的参数说明：
        //audioSource：音频输入源，比如麦克风等，通过MediaRecorder.AudioSource获取；
        //sampleRateInHz：音频采样率，每秒取得声音样本的次数，采样率越高，声音质量越好，常见的有16k，44.1KHZ；
        //channelConfig：音频录制时的声道，分为单声道和立体声道，在AudioFormat中定义；
        //audioFormat：音频格式（采样位数、采样值），用来衡量声音波动变化的参数，有8位和16位，在AudioFormat中定义；
        //bufferSizeInBytes：音频缓冲区大小，不同手机厂商有不同的实现，可通过AudioRecord.getMinBufferSize()方法获取；
        mAudioRecord = AudioRecord(
            audioSource,
            sampleRateInHz,
            channelConfig,
            audioFormat,
            bufferSizeInBytes
        )
        if (mAudioRecord.state == AudioRecord.STATE_UNINITIALIZED) {
            LogUtil.e(TAG, "initRecorder AudioRecord is uninitialized")
            mState = State.IDLE
            return
        }
        mState = State.INITIALIZED
    }

    fun startRecord() {
        if (mState != State.INITIALIZED) {
            LogUtil.e(TAG, "startRecord state is invalid. currState is $mState")
            return
        }
        if (mAudioRecord == null || mAudioRecord.state == AudioRecord.STATE_UNINITIALIZED) {
            LogUtil.e(TAG, "startRecord AudioRecord is uninitialized")
            mState = State.IDLE
            return
        }
        LogUtil.i(TAG, "startRecord ++++++++++++")
        val bufferSizeInBytes = AudioRecord.getMinBufferSize(
            44100, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        Rx.runTag(TAG){
            try {
                val audioData = ByteArray(bufferSizeInBytes)
                mAudioRecord.startRecording()
                mState = State.RECORDING
                while (!isStopped) {
                    mAudioRecord.read(audioData, 0, bufferSizeInBytes)
                    FileUtils.saveFile("$mPath$mFileName.PCM", audioData, true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                releaseRecorder(bufferSizeInBytes)
            }
        }
    }

    fun stopRecord() {
        if (mState != State.RECORDING) {
            LogUtil.e(TAG, "stopRecord state is invalid. currState is $mState")
            return
        }
        if (mAudioRecord == null || mAudioRecord.state == AudioRecord.STATE_UNINITIALIZED) {
            LogUtil.e(TAG, "stopRecord AudioRecord is uninitialized")
            mState = State.IDLE
            return
        }
        LogUtil.i(TAG, "stopRecord ++++++++++++")
        isStopped = true
        mState = State.STOPPING
    }

    private fun releaseRecorder(bufferSizeInBytes: Int) {
        LogUtil.i(TAG, "releaseRecorder ++++++++++++")
        if (mAudioRecord != null && mAudioRecord.state == AudioRecord.STATE_INITIALIZED) {
            mAudioRecord.stop()
            mAudioRecord.release()
        }
        mState = State.STOPPED
        convertPCMToWAV(
            mPath, mFileName, 44100, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes
        )
        mState = State.IDLE
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
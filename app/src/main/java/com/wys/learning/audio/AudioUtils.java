package com.wys.learning.audio;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class AudioUtils {
    private static final String TAG = "AudioUtils";
    public static void downMusicVolume(Context context){
//        STREAM_ALARM：手机闹铃的声音
//        STREAM_MUSIC：手机音乐的声音
//        STREAM_DTMF：DTMF音调的声音

//        STREAM_RING：电话铃声的声音
//        STREAM_NOTFICATION：系统提示的声音
//        STREAM_SYSTEM：系统的声音
//        STREAM_VOICE_CALL：语音电话声音
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //打开扬声器
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG,"[downMusicVolume] maxVolume = " + maxVolume + ",volume = " + volume);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);

    }
    public static void upMusicVolume(Context context){
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //打开扬声器
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG,"[upMusicVolume] maxVolume = " + maxVolume + ",volume = " + volume);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
    }

    /**
     * 增加系统声音
     * @param context
     */
    public static void upSystemVolume(Context context){
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //打开扬声器
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.d(TAG,"[upSystemVolume] maxVolume = " + maxVolume + ",volume = " + volume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,volume+1,AudioManager.FLAG_SHOW_UI);
    }

    /**
     * 降低系统声音
     * @param context
     */
    public static void downSystemVolume(Context context){
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //打开扬声器
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.d(TAG,"[downSystemVolume] maxVolume = " + maxVolume + ",volume = " + volume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,volume-1,AudioManager.FLAG_SHOW_UI);
    }
    public static void setSpeakerphone(Context context,boolean isSpeakerOn){
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //是否打开扬声器
        mAudioManager.setSpeakerphoneOn(isSpeakerOn);
    }
}

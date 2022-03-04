package com.wys.liblame;

public class LameUtils {
    static {
        System.loadLibrary("lame_utils");
    }
    public static native String getLameVersion();
}

package com.wys.libyuv.utils;

public class YuvUtils {
    static {
        System.loadLibrary("yuv_utils");
    }

    /**
     * nv21 -> i420
     *
     * @param src_nv21_data_array
     * @param width
     * @param height
     * @param dst_i420_data_array
     */
    public static native void NV21ToI420(byte[] src_nv21_data_array, int width, int height, byte[] dst_i420_data_array);

    /**
     * i420 -> nv21
     *
     * @param src_i420_data_array
     * @param width
     * @param height
     * @param dst_nv21_data_array
     */
    public static native void I420ToNV21(byte[] src_i420_data_array, int width, int height, byte[] dst_nv21_data_array);

    /**
     * 压缩
     * @param src_i420_data_array    原始数据
     * @param width                 原始宽度
     * @param height                原始高度
     * @param dst_i420_data_array    输出数据
     * @param dst_width             输出宽度
     * @param dst_height            输出高度
     * @param mode                  压缩模式（这里有0，1，2，3速度又快到慢，质量由低到高）
     */
    public static native void compressI420(byte[] src_i420_data_array, int width, int height, byte[] dst_i420_data_array, int dst_width, int dst_height, int mode);

    /**
     * 旋转
     * @param src_i420_data_array
     * @param width
     * @param height
     * @param dst_i420_data_array
     * @param degree
     */
    public static native void rotateI420(byte[] src_i420_data_array, int width, int height, byte[] dst_i420_data_array, int degree);

    /**
     * 镜像
     * @param src_i420_data_array
     * @param width
     * @param height
     * @param dst_i420_data_array
     */
    public static native void mirrorI420(byte[] src_i420_data_array, int width, int height, byte[] dst_i420_data_array);

    /**
     * i420 -> ARGB_8888
     * @param src_i420_data_array
     * @param width
     * @param height
     * @param dst_rgba_data_array
     */
    public static native void I420ToRGBA(byte[] src_i420_data_array, int width, int height, byte[] dst_rgba_data_array);

//    /**
//     * YV12 -> ARGB_8888
//     * @param src_yv12_data_array
//     * @param width
//     * @param height
//     * @param dst_rgba_data_array
//     */
//    public static native void YV12ToRGBA(byte[] src_yv12_data_array, int width, int height, byte[] dst_rgba_data_array);
//
//    /**
//     * NV12 -> ARGB_8888
//     * @param src_nv12_data_array
//     * @param width
//     * @param height
//     * @param dst_rgba_data_array
//     */
//    public static native void NV12ToRGBA(byte[] src_nv12_data_array, int width, int height, byte[] dst_rgba_data_array);
//
//    /**
//     * NV21 -> ARGB_8888
//     * @param src_nv21_data_array
//     * @param width
//     * @param height
//     * @param dst_rgba_data_array
//     */
//    public static native void NV21ToRGBA(byte[] src_nv21_data_array, int width, int height, byte[] dst_rgba_data_array);
//
//    /**
//     * rotate RGB image
//     * @param src_rgb_data_array
//     * @param width
//     * @param height
//     * @param dst_rgb_data_array
//     * @param degree
//     */
//    public static native void rotateRGB(byte[] src_rgb_data_array, int width, int height, byte[] dst_rgb_data_array, float degree);
//
//    /**
//     * rotate ARGB image
//     * @param src_rgba_data_array
//     * @param width
//     * @param height
//     * @param dst_rgba_data_array
//     * @param degree
//     */
//    public static native void rotateRGBA(byte[] src_rgba_data_array, int width, int height, byte[] dst_rgba_data_array, float degree);
//
//    /**
//     * rotate YUV420 image
//     * @param src_yuv420_data_array
//     * @param width
//     * @param height
//     * @param dst_yuv420_data_array
//     * @param degree
//     */
//    public static native void rotateYUV420P(byte[] src_yuv420_data_array, int width, int height, byte[] dst_yuv420_data_array, float degree);
//
//    /**
//     * rotate YUV420SP image
//     * @param src_yuv420SP_data_array
//     * @param width
//     * @param height
//     * @param dst_yuv420SP_data_array
//     * @param degree
//     */
//    public static native void rotateYUV420SP(byte[] src_yuv420SP_data_array, int width, int height, byte[] dst_yuv420SP_data_array, float degree);
}

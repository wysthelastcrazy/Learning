//
// Created by wangyasheng on 2021/12/7.
//
#include <jni.h>
#include "libyuv.h"

void i420ToNV21(jbyte *data, jint width, jint height, jbyte *data1);

void rotateI420(jbyte *data, jint width, jint height, jbyte *data1, jint degree);

void nv21ToI420(jbyte *data, jint width, jint height, jbyte *data1);

void mirrorI420(jbyte *src_i420_data, jint width, jint height, jbyte *dst_i420_data);

void
scaleI420(jbyte *data, jint width, jint height, jbyte *data1, jint width1, jint height1, jint mode);

void i420ToRGBA(jbyte *data, jint width, jint height, jbyte *data1);

extern "C"
JNIEXPORT void JNICALL
Java_com_wys_libyuv_utils_YuvUtils_NV21ToI420(JNIEnv
* env,
jclass clazz, jbyteArray
src_nv21_data_array,
jint width, jint
height,
jbyteArray dst_i420_data_array
) {
    jbyte *src_nv21_data = env->GetByteArrayElements(src_nv21_data_array,JNI_FALSE);
    jbyte *dst_i420_data = env->GetByteArrayElements(dst_i420_data_array,JNI_FALSE);

    nv21ToI420(src_nv21_data, width, height, dst_i420_data);

    env->ReleaseByteArrayElements(src_nv21_data_array,src_nv21_data,0);
    env->ReleaseByteArrayElements(dst_i420_data_array,dst_i420_data,0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wys_libyuv_utils_YuvUtils_I420ToNV21(JNIEnv *env, jclass clazz,
                                              jbyteArray src_i420_data_array, jint width,
                                              jint height, jbyteArray dst_nv21_data_array) {
    jbyte *src_i420_data = env->GetByteArrayElements(src_i420_data_array,JNI_FALSE);
    jbyte *dst_nv21_data = env->GetByteArrayElements(dst_nv21_data_array,JNI_FALSE);

    i420ToNV21(src_i420_data,width,height,dst_nv21_data);

    env->ReleaseByteArrayElements(src_i420_data_array,src_i420_data,0);
    env->ReleaseByteArrayElements(dst_nv21_data_array,dst_nv21_data,0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wys_libyuv_utils_YuvUtils_compressI420(JNIEnv *env, jclass clazz,
                                               jbyteArray src_i420_data_array, jint width,
                                               jint height, jbyteArray dst_i420_data_array,
                                               jint dst_width, jint dst_height, jint mode
                                               ) {
    jbyte *src_i420_data = env->GetByteArrayElements(src_i420_data_array,JNI_FALSE);
    jbyte *dst_i420_data = env->GetByteArrayElements(dst_i420_data_array,JNI_FALSE);

    scaleI420(src_i420_data, width, height, dst_i420_data, dst_width, dst_height, mode);

    env->ReleaseByteArrayElements(src_i420_data_array,src_i420_data,0);
    env->ReleaseByteArrayElements(dst_i420_data_array,dst_i420_data,0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wys_libyuv_utils_YuvUtils_rotateI420(JNIEnv *env, jclass clazz,
                                              jbyteArray src_i420_data_array, jint width,
                                              jint height, jbyteArray dst_i420_data_array,
                                              jint degree) {
    jbyte *src_i420_data = env->GetByteArrayElements(src_i420_data_array,JNI_FALSE);
    jbyte *dst_i420_data = env->GetByteArrayElements(dst_i420_data_array,JNI_FALSE);

    rotateI420(src_i420_data,width,height,dst_i420_data,degree);

    env->ReleaseByteArrayElements(src_i420_data_array,src_i420_data,0);
    env->ReleaseByteArrayElements(dst_i420_data_array,dst_i420_data,0);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_wys_libyuv_utils_YuvUtils_mirrorI420(JNIEnv *env, jclass clazz,
                                              jbyteArray src_i420_data_array, jint width,
                                              jint height, jbyteArray dst_i420_data_array) {
    jbyte *src_i420_data = env->GetByteArrayElements(src_i420_data_array,JNI_FALSE);
    jbyte *dst_i420_data = env->GetByteArrayElements(dst_i420_data_array,JNI_FALSE);

    mirrorI420(src_i420_data,width,height,dst_i420_data);

    env->ReleaseByteArrayElements(src_i420_data_array,src_i420_data,0);
    env->ReleaseByteArrayElements(dst_i420_data_array,dst_i420_data,0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wys_libyuv_utils_YuvUtils_I420ToRGBA(JNIEnv *env, jclass clazz,
                                              jbyteArray src_i420_data_array, jint width,
                                              jint height, jbyteArray dst_rgba_data_array) {

    jbyte *src_i420_data = env->GetByteArrayElements(src_i420_data_array,JNI_FALSE);
    jbyte *dst_rgba_data = env->GetByteArrayElements(dst_rgba_data_array,JNI_FALSE);

    i420ToRGBA(src_i420_data,width,height,dst_rgba_data);

    env->ReleaseByteArrayElements(src_i420_data_array,src_i420_data,JNI_ABORT);
    env->ReleaseByteArrayElements(dst_rgba_data_array,dst_rgba_data,0);
}

void nv21ToI420(jbyte *src_nv21_data, jint width, jint height, jbyte *dst_i420_data){
    jint src_y_size = width * height;
    jint src_u_size = (width >> 1) * (height >> 1);

    jbyte *src_nv21_y_data = src_nv21_data;
    jbyte *src_nv21_vu_data = src_nv21_data + src_y_size;

    jbyte *dst_i420_y_data = dst_i420_data;
    jbyte *dst_i420_u_data = dst_i420_data + src_y_size;
    jbyte *dst_i420_v_data = dst_i420_data +src_y_size + src_u_size;

    libyuv::NV21ToI420((const uint8_t *) src_nv21_y_data, width,
                       (const uint8_t *) src_nv21_vu_data, width,
                       (uint8_t *) dst_i420_y_data, width,
                       (uint8_t *) dst_i420_u_data, width >> 1,
                       (uint8_t *) dst_i420_v_data, width >> 1,
                       width, height);
}

void i420ToNV21(jbyte *src_i420_data, jint width, jint height, jbyte *dst_nv21_data){
    jint src_y_size = width * height;
    jint src_u_size = (width >> 1) * (height >> 1);

    jbyte *dst_nv21_y_data = dst_nv21_data;
    jbyte *dst_nv21_uv_data = dst_nv21_y_data + src_y_size;

    jbyte *src_i420_y_data = src_i420_data;
    jbyte *src_i420_u_data = src_i420_data + src_y_size;
    jbyte *src_i420_v_data = src_i420_data + src_y_size + src_u_size;

    libyuv::I420ToNV21((const uint8_t *) src_i420_y_data, width,
                       (const uint8_t *) src_i420_u_data,width >> 1,
                       (const uint8_t *) src_i420_v_data,width >> 1,
                       (uint8_t *) dst_nv21_y_data, width,
                       (uint8_t *) dst_nv21_uv_data, width,
                       width,height);
}

void rotateI420(jbyte *src_i420_data, jint width, jint height, jbyte *dst_i420_data,jint degree) {
    jint src_i420_y_size = width * height;
    jint src_i420_u_size = (width >> 1) * (height >> 1);

    jbyte *src_i420_y_data = src_i420_data;
    jbyte *src_i420_u_data = src_i420_data + src_i420_y_size;
    jbyte *src_i420_v_data = src_i420_data + src_i420_y_size + src_i420_u_size;

    jbyte *dst_i420_y_data = dst_i420_data;
    jbyte *dst_i420_u_data = dst_i420_data + src_i420_y_size;
    jbyte *dst_i420_v_data = dst_i420_data + src_i420_y_size + src_i420_u_size;

    //要注意，这里的width和height在旋转之后时相反的
    if (degree == libyuv::kRotate90 || degree == libyuv::kRotate270) {
        libyuv::I420Rotate((const uint8_t *) src_i420_y_data, width,
                           (const uint8_t *) src_i420_u_data, width >> 1,
                           (const uint8_t *) src_i420_v_data, width >> 1,
                           (uint8_t *) dst_i420_y_data, height,
                           (uint8_t *) dst_i420_u_data, height >> 1,
                           (uint8_t *) dst_i420_v_data, height >> 1,
                           width, height,
                           (libyuv::RotationMode) degree);
    } else {
        libyuv::I420Rotate((const uint8_t *) src_i420_y_data, width,
                           (const uint8_t *) src_i420_u_data, width >> 1,
                           (const uint8_t *) src_i420_v_data, width >> 1,
                           (uint8_t *) dst_i420_y_data, width,
                           (uint8_t *) dst_i420_u_data, width >> 1,
                           (uint8_t *) dst_i420_v_data, width >> 1,
                           width, height,
                           (libyuv::RotationMode) degree);
    }
}

void mirrorI420(jbyte *src_i420_data, jint width, jint height, jbyte *dst_i420_data) {
    jint src_i420_y_size = width * height;
    jint src_i420_u_size = (width >> 1) * (height >> 1);

    jbyte *src_i420_y_data = src_i420_data;
    jbyte *src_i420_u_data = src_i420_data + src_i420_y_size;
    jbyte *src_i420_v_data = src_i420_data + src_i420_y_size + src_i420_u_size;

    jbyte *dst_i420_y_data = dst_i420_data;
    jbyte *dst_i420_u_data = dst_i420_data + src_i420_y_size;
    jbyte *dst_i420_v_data = dst_i420_data + src_i420_y_size + src_i420_u_size;

    libyuv::I420Mirror(reinterpret_cast<const uint8_t *>(src_i420_y_data), width,
                       reinterpret_cast<const uint8_t *>(src_i420_u_data), width >> 1,
                       reinterpret_cast<const uint8_t *>(src_i420_v_data), width >> 1,
                       reinterpret_cast<uint8_t *>(dst_i420_y_data), width,
                       reinterpret_cast<uint8_t *>(dst_i420_u_data), width >> 1,
                       reinterpret_cast<uint8_t *>(dst_i420_v_data), width >> 1,
                       width, height);
}

void scaleI420(jbyte *src_i420_data, jint width, jint height, jbyte *dst_i420_data, jint dst_width,
               jint dst_height, jint mode) {

    jint src_i420_y_size = width * height;
    jint src_i420_u_size = (width >> 1) * (height >> 1);
    jbyte *src_i420_y_data = src_i420_data;
    jbyte *src_i420_u_data = src_i420_data + src_i420_y_size;
    jbyte *src_i420_v_data = src_i420_data + src_i420_y_size + src_i420_u_size;

    jint dst_i420_y_size = dst_width * dst_height;
    jint dst_i420_u_size = (dst_width >> 1) * (dst_height >> 1);
    jbyte *dst_i420_y_data = dst_i420_data;
    jbyte *dst_i420_u_data = dst_i420_data + dst_i420_y_size;
    jbyte *dst_i420_v_data = dst_i420_data + dst_i420_y_size + dst_i420_u_size;

    libyuv::I420Scale((const uint8_t *) src_i420_y_data, width,
                      (const uint8_t *) src_i420_u_data, width >> 1,
                      (const uint8_t *) src_i420_v_data, width >> 1,
                      width, height,
                      (uint8_t *) dst_i420_y_data, dst_width,
                      (uint8_t *) dst_i420_u_data, dst_width >> 1,
                      (uint8_t *) dst_i420_v_data, dst_width >> 1,
                      dst_width, dst_height,
                      (libyuv::FilterMode) mode);
}
void i420ToRGBA(jbyte *src_i420_data, jint width, jint height, jbyte *dst_RGBA_data){
    jbyte *pY = src_i420_data;
    jbyte *pU = src_i420_data + width * height;
    jbyte *pV = src_i420_data + width * height * 5 / 4;

    libyuv::I420ToABGR(reinterpret_cast<const uint8_t *>(pY), width,
                       reinterpret_cast<const uint8_t *>(pU), width >> 1,
                       reinterpret_cast<const uint8_t *>(pV), width >> 1,
                       reinterpret_cast<uint8_t *>(dst_RGBA_data), width * 4,
                       width, height);
}



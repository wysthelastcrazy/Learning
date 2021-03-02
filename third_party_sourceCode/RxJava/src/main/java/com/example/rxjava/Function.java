package com.example.rxjava;

/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe:
 */
public interface Function<T,R> {
    R apply(T t);
}

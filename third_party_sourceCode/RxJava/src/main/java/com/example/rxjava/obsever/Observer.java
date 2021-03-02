package com.example.rxjava.obsever;

/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe:观察者
 */
public interface Observer<T> {
    void onSubscribe();
    void onNext(T item);
    void onError(Throwable e);
    void onComplete();
}

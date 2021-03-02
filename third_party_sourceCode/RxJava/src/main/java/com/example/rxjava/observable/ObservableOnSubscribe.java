package com.example.rxjava.observable;

import com.example.rxjava.obsever.Observer;

/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe:正真的被观察者
 */
public interface ObservableOnSubscribe<T> {
    /**
     * 设置观察者
     * @param observer
     */
     void subscribe(Observer<T> observer);

}

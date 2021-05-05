package com.example.rxjava.observable.operators;


import androidx.annotation.NonNull;

import com.example.rxjava.observable.Observable;
import com.example.rxjava.observable.ObservableOnSubscribe;
import com.example.rxjava.obsever.Observer;

/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe:
 */
public class ObservableCreate<T> extends Observable<T> {
    final ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    /**
     * 复写了subscribeActual()
     * 作用：订阅时，通过接口回调，调用被观察者（Observable）和观察者（Observer）的方法
     * @param observer
     */
    @Override
    protected void subscribeActual(@NonNull Observer<T> observer) {
        //调用观察者的onSubscribe()
        observer.onSubscribe();
        if (source!=null){
            //调用source对象的subscribe()
            //实际被观察者的订阅方法
            source.subscribe(observer);
        }
    }
}

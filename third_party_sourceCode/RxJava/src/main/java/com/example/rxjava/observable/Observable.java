package com.example.rxjava.observable;

import android.support.annotation.NonNull;

import com.example.rxjava.Function;
import com.example.rxjava.observable.operators.ObservableCreate;
import com.example.rxjava.observable.operators.ObservableMap;
import com.example.rxjava.obsever.Observer;

/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe: 虚假的被观察者
 */
public abstract class Observable<T> implements ObservableSource<T>{

    /**
     * 接收一个真正的被观察者，返回一个虚假的被观察者，
     * 返回的原因是RxJava后续的方法都不再是静态的了，所以需要
     * 得到一个对象。更重要的原因是RxJava使用了装饰者模式，能够
     * 完成更好的功能拓展。
     * @param source
     * @param <T>
     * @return
     */
    public static <T> Observable<T> create(ObservableOnSubscribe<T> source){
        return new ObservableCreate<>(source);
    }

    public <R> Observable<R> map(Function<T,R> mapper){
        return new ObservableMap<>(this,mapper);
    }

    @Override
    public final void subscribe(@NonNull Observer<T> observer) {
        subscribeActual(observer);
    }

    /**
     * 作用：真正的订阅处理逻辑
     * @param observer
     */
    protected abstract void subscribeActual(@NonNull Observer<T> observer);
}

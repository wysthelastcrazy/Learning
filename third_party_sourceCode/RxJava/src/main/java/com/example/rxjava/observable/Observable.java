package com.example.rxjava.observable;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.rxjava.Function;
import com.example.rxjava.obsever.Observer;

/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe: 虚假的被观察者
 */
public class Observable<T> {
    private ObservableOnSubscribe<T> source;
    private Observable(ObservableOnSubscribe<T> source){
        this.source = source;
    }
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
        return new <T>Observable(source);
    }
    public void subscribe(Observer<T> observer){
        observer.onSubscribe();
        if (source!=null){
            source.subscribe(observer);
        }
    }

    public <R> Observable<R> map(Function<T,R> mapper){
        return new Observable<>(new MapObservable<T,R>(source,mapper));
    }
}

package com.example.rxjava.observable.operators;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.example.rxjava.Function;
import com.example.rxjava.observable.Observable;
import com.example.rxjava.observable.ObservableSource;
import com.example.rxjava.obsever.Observer;


/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe:
 */
public class ObservableMap<T,R> extends Observable<R> {
    protected final ObservableSource<T> source;
    private Function<T,R> mapper;
    public ObservableMap(ObservableSource<T> source, Function<T,R> mapper){
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    protected void subscribeActual(@NonNull Observer<R> observer) {
        MapObserver mapObserver = new MapObserver(observer,mapper);
        source.subscribe(mapObserver);
    }

    class MapObserver<T,R> implements Observer<T>{
        private Observer<R> observer;
        private Function<T,R> mapper;
        public MapObserver(Observer<R> observer,Function<T,R> mapper){
            this.observer = observer;
            this.mapper = mapper;
        }
        @Override
        public void onSubscribe() {
            observer.onSubscribe();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onNext(T item) {
            R result = mapper.apply(item);
            observer.onNext(result);
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}

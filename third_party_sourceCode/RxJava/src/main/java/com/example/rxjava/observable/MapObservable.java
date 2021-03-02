package com.example.rxjava.observable;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.rxjava.Function;
import com.example.rxjava.obsever.Observer;


/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe:
 */
public class MapObservable<T,R> implements ObservableOnSubscribe<R> {
    private ObservableOnSubscribe<T> source;
    private Function<T,R> mapper;
    public MapObservable(ObservableOnSubscribe<T> source,Function<T,R> mapper){
        this.source = source;
        this.mapper = mapper;
    }
    @Override
    public void subscribe(Observer<R> observer) {
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

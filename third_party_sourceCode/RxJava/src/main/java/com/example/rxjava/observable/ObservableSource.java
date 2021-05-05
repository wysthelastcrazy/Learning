package com.example.rxjava.observable;


import androidx.annotation.NonNull;

import com.example.rxjava.obsever.Observer;

/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe:
 */
public interface ObservableSource<T> {
    void subscribe(@NonNull Observer<T> observer);
}

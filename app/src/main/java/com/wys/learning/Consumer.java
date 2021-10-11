package com.wys.learning;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}

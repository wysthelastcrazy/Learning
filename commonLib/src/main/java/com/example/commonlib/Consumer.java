package com.example.commonlib;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}

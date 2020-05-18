package com.xupu.locationmap.common.po;

public interface MyCallback<T> {
    void call(int state, T t, String msg);
}
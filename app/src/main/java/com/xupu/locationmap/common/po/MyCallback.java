package com.xupu.locationmap.common.po;

public interface MyCallback<T> {
    void call(ResultData<T> resultData);
}
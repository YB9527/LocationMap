package com.xupu.locationmap.common.dialog;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;

public interface DialogCallback<T> {
      void call(int code, T t);
}

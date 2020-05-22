package com.xupu.locationmap.projectmanager.po;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.po.ResultData;

public abstract class BtuFiledCustom<T> extends FiledCustom {


    public BtuFiledCustom() {

    }

    public BtuFiledCustom(String attribute) {
        super(attribute);
    }

    /**
     * 要检查数据吗
     */
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public BtuFiledCustom setCheck(boolean check) {
        isCheck = check;
        return this;
    }

    public abstract void OnClick(ResultData<T> resultData);
}

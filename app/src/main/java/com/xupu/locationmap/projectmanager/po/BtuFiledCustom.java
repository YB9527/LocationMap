package com.xupu.locationmap.projectmanager.po;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.po.ResultData;

public abstract class BtuFiledCustom<T> extends FiledCustom {


    public BtuFiledCustom() {

    }

    public BtuFiledCustom(String idText, String attribute) {
        super(idText,attribute);
    }
    public BtuFiledCustom(Integer id, String attribute) {
        super(id,attribute);
    }
    /**
     * 比如需要返回 对象吗， 比如取消键的话，不要返回对象
     */
    private boolean isReturn;
    /**
     * 要检查数据吗
     */
    private boolean isCheck;
    /**
     * 是否弹出确认窗口
     */
    private boolean isConfirm;

    private String confirmmessage;

    public boolean isConfirm() {
        return isConfirm;
    }

    public BtuFiledCustom setConfirm(boolean confirm, String confirmmessage) {
        isConfirm = confirm;
        this.confirmmessage = confirmmessage;
        return this;
    }

    public String getConfirmmessage() {
        return confirmmessage;
    }

    public boolean isReturn() {
        return isReturn;

    }


    public BtuFiledCustom setReturn(boolean aReturn) {
        isReturn = aReturn;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public BtuFiledCustom setCheck(boolean check) {
        isCheck = check;
        return this;
    }

    public abstract void OnClick(MyJSONObject MyJSONObject);
}

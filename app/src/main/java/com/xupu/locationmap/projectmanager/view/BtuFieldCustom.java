package com.xupu.locationmap.projectmanager.view;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;

public abstract class BtuFieldCustom<T> extends FieldCustom {


    public BtuFieldCustom() {

    }

    public BtuFieldCustom(String idText, String attribute) {
        super(idText,attribute);
    }
    public BtuFieldCustom(Integer id, String attribute) {
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

    /**
     * 是否比对信息
     */
    private boolean isCompare;

    private String compareMessage="没有修改";

    public boolean isConfirm() {
        return isConfirm;
    }

    public BtuFieldCustom setConfirm(boolean confirm, String confirmmessage) {
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


    public BtuFieldCustom setReturn(boolean aReturn) {
        isReturn = aReturn;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public BtuFieldCustom setCheck(boolean check) {
        isCheck = check;
        return this;
    }

    public abstract void OnClick(MyJSONObject myJSONObject);

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    public void setConfirmmessage(String confirmmessage) {
        this.confirmmessage = confirmmessage;
    }

    public boolean isCompare() {
        return isCompare;
    }

    public BtuFieldCustom setCompare(boolean compare) {
        isCompare = compare;
        return this;
    }

    public String getCompareMessage() {
        return compareMessage;
    }

    public BtuFieldCustom setCompareMessage(String compareMessage) {
        this.compareMessage = compareMessage;
        return this;
    }
}

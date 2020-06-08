package com.xupu.locationmap.projectmanager.po;

public abstract class ViewFildCustom extends FiledCustom {


    public ViewFildCustom(Integer rid) {
        super(rid,"");

    }
    public boolean isConfirm() {
        return isConfirm;
    }

    /**
     * 是否弹出确认窗口
     */
    private boolean isConfirm;

    private String confirmmessage;
    public ViewFildCustom setConfirm(boolean confirm, String confirmmessage) {
        isConfirm = confirm;
        this.confirmmessage = confirmmessage;
        return this;
    }

    public String getConfirmmessage() {
        return confirmmessage;
    }


    public abstract void OnClick(MyJSONObject myJSONObject);
}

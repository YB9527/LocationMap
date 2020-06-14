package com.xupu.locationmap.projectmanager.view;

import com.xupu.locationmap.common.po.Callback;

public class CheckBoxFieldCustom extends FieldCustom {

    private Callback callback;
    private Integer itemRid;
    /**
     *
     * @param selectRid 选择框
     * @param itemRid item 的点击事件触发
     */
    public CheckBoxFieldCustom(Integer selectRid,String attribute, Integer itemRid) {
        super(selectRid,attribute);
        this.itemRid  = itemRid;

    }

    public Callback getCallback() {
        return callback;
    }

    public CheckBoxFieldCustom setCallback(Callback callback) {
        this.callback = callback;
        return  this;
    }

    public Integer getItemRid() {
        return itemRid;
    }

    public void setItemRid(Integer itemRid) {
        this.itemRid = itemRid;
    }
}

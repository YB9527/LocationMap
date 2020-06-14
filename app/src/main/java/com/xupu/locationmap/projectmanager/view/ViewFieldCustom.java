package com.xupu.locationmap.projectmanager.view;

import android.view.View;

import com.xupu.locationmap.projectmanager.page.MyItemRecyclerViewAdapter;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

public abstract class ViewFieldCustom extends FieldCustom {


    public ViewFieldCustom(Integer rid) {
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
    public ViewFieldCustom setConfirm(boolean confirm, String confirmmessage) {
        isConfirm = confirm;
        this.confirmmessage = confirmmessage;
        return this;
    }

    public String getConfirmmessage() {
        return confirmmessage;
    }


    public abstract void OnClick(View view, MyJSONObject myJSONObject);
}

package com.xupu.locationmap.projectmanager.view;

import android.content.Intent;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;

public class ImgFieldCusom extends FieldCustom {
    public ImgFieldCusom(String idText, String attribute) {
        super(idText, attribute);
    }

    public ImgFieldCusom(Integer id, String attribute) {
        super(id, attribute);
    }

    public ImgFieldCusom(Integer id) {
        super(id);
    }

    private Intent intent;

    public ImgFieldCusom setShowActivty(Intent intent) {
        this.intent = intent;
        return this;
    }

    public void onClick(MyJSONObject myJSONObject) {

    }

}

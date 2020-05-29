package com.xupu.locationmap.projectmanager.po;

import android.content.Intent;
import android.os.Bundle;

public class ImgFiledCusom extends FiledCustom {
    public ImgFiledCusom(String idText, String attribute) {
        super(idText,attribute);
    }
    public ImgFiledCusom(Integer id, String attribute) {
        super(id,attribute);
    }


    private Intent intent;
    public ImgFiledCusom setShowActivty(Intent intent) {
        this.intent = intent;
        return this;
    }

   public void onClick(MyJSONObject myJSONObject){

   }

}

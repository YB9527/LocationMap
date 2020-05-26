package com.xupu.locationmap.projectmanager.po;

import android.content.Intent;
import android.os.Bundle;

public class ImgFiledCusom extends FiledCustom {
    public ImgFiledCusom(String attribute) {
        super(attribute);
    }


    private Intent intent;
    public ImgFiledCusom setShowActivty(Intent intent) {
        this.intent = intent;
        return this;
    }

   public void onClick(MyJSONObject myJSONObject){

   }

}

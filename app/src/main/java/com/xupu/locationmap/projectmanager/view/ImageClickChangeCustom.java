package com.xupu.locationmap.projectmanager.view;

import android.view.View;
import android.widget.ImageView;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;

public class ImageClickChangeCustom extends ViewFieldCustom {

    private Integer oneImage;
    private Integer twoImage;
    private Integer imgRid;
    private ImageView imageView;

    public ImageClickChangeCustom(Integer rid,Integer imgRid, Integer oneImage,Integer twoImage) {
        super(rid);

    }


    @Override
    public void OnClick(View view, MyJSONObject myJSONObject) {
        imageView = view.findViewById(imgRid);

    }
}

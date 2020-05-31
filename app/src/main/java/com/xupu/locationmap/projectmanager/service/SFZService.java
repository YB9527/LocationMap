package com.xupu.locationmap.projectmanager.service;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.po.SFZBack;
import com.xupu.locationmap.common.po.SFZFront;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;


import java.util.UUID;

public class SFZService {


    public static MyJSONObject frontToMyJSONObject(MyJSONObject media, SFZFront sfz) {

        MyJSONObject sfzJson = new MyJSONObject(UUID.randomUUID().toString(), media.getJsonobject().getString("task"), media.getId(), JSONObject.toJSONString(sfz));
        return sfzJson;
    }

    public static MyJSONObject backToMyJSONObject(MyJSONObject media, SFZBack sfz) {
        return new MyJSONObject(UUID.randomUUID().toString(), Customizing.SFZ_back, media.getId(), JSONObject.toJSONString(sfz));
    }
}

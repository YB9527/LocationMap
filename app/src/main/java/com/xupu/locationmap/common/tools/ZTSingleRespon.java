package com.xupu.locationmap.common.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 当个中台对象
 */
public abstract class ZTSingleRespon extends ZTRespon {

    @Override
    public abstract void onSuccess(JSONObject httpRespon);

    @Override
    public void onSuccess(JSONArray httpRespon) {
        if (httpRespon.size() > 0) {
            JSONObject jsonObject = httpRespon.getJSONObject(0);
            jsonObject.remove("value");
            onSuccess(jsonObject);
        }
        onSuccess(new JSONObject());
    }
}

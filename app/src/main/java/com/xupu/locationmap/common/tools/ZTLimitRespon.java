package com.xupu.locationmap.common.tools;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class ZTLimitRespon extends JSONObjectRespon {
    @Override
    public abstract void onSuccess(JSONArray httpRespon);

    @Override
    public void onSuccess(JSONObject httpRespon) {

    }

    @Override
    public void parse(String json) {

        if (TextUtils.isEmpty(json)) {
            //请求失败
            onError("网络失败");
            return;
        }

        //根据我需要的类型,进行转换json
        //如果我只需要获取json对象,老老实实把json
        // ADS.class
        //尝试转化json->需要的类型
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject.getIntValue("code") == 1000) {
            onSuccess(jsonObject.getJSONObject("data").getJSONArray("rows"));
        } else {
            onError("服务器响应问题："+ jsonObject.getString("msg"));
            return;
        }
    }
}

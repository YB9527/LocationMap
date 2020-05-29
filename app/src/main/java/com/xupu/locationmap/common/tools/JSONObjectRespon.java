package com.xupu.locationmap.common.tools;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;


public abstract class JSONObjectRespon {
    //http返回的类型的泛型

    public JSONObjectRespon() {

    }

    //失败->调用者->失败的原因
    public void onError(String msg) {
        AndroidTool.showAnsyTost(msg, 1);
    }

    //成功->返回我需要的类型
    public abstract void onSuccess(JSONObject httpRespon);

    public   void onSuccess(JSONArray httpRespon){

    }
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
        onSuccess(jsonObject);
    }
}

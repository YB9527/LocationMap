package com.xupu.locationmap.common.tools;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONObject;

public class JSONTool {
    /**
     *json 对象值复制 浅克隆
     * @param srcJson 源数据
     * @param descJson 目标对象
     */

    @TargetApi(Build.VERSION_CODES.N)
    public static void copytAttribute(JSONObject srcJson, JSONObject descJson) {
        for (String key : srcJson.keySet()){
            if(descJson.containsKey(key)){
                descJson.replace(key,srcJson.get(key));
            }
        }
    }
}

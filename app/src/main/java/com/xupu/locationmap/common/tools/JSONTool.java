package com.xupu.locationmap.common.tools;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xupu.locationmap.projectmanager.page.LowMapManager;
import com.xupu.locationmap.projectmanager.po.LowImage;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONTool {
    /**
     * json 对象值复制 浅克隆
     *
     * @param srcJson  源数据
     * @param descJson 目标对象
     */

    @TargetApi(Build.VERSION_CODES.N)
    public static void copytAttribute(JSONObject srcJson, JSONObject descJson) {
        for (String key : srcJson.keySet()) {
            if (descJson.containsKey(key)) {
                descJson.replace(key, srcJson.get(key));
            }
        }
    }

    public static Map<String, List<MyJSONObject>> getIDMap(String jsonKey, List<MyJSONObject> source) {
        Map<String, List<MyJSONObject>> map = new HashMap<>();
        List<MyJSONObject> list;
        for (MyJSONObject myJSONObject : source) {
            list = map.get(myJSONObject.getTablename());
            if (list == null) {
                list = new ArrayList<>();
                map.put(myJSONObject.getTablename(), list);
            }
            list.add(myJSONObject);
        }
        return map;
    }

    /**
     * 转 JSONObject
     * @param obj
     * @return
     */
    public static JSONObject toJSONObject(Object obj){
       String json = JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
       return  JSONObject.parseObject(json);
    }

    /**
     * 用的是 myJSONObject.getJsonobject() 转换
     * @param myJSONObjects
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> toObject(List<MyJSONObject> myJSONObjects,Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if(myJSONObjects != null){
            for (MyJSONObject myJSONObject : myJSONObjects){
                T t = JSONObject.toJavaObject(myJSONObject.getJsonobject(),clazz);
                list.add(t);
            }
        }
        return  list;
    }

    /**
     * new MyJSONObject(null, null, null, JSONTool.toJSONObject(obj))
     * @param extiLayers
     * @return
     */
    public static <T> List<MyJSONObject> toMyJSONObject(List<T> extiLayers) {
        List<MyJSONObject> myShowLayers = new ArrayList<>();
        if(extiLayers != null){
            for(Object obj : extiLayers){
                myShowLayers.add(new MyJSONObject(null, null, null, JSONTool.toJSONObject(obj)));
            }
        }
        return  myShowLayers;
    }
}

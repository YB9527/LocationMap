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
import java.util.UUID;

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
            String key = myJSONObject.getJsonobject().getString(jsonKey);
            list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }
            list.add(myJSONObject);
        }
        return map;
    }

    /**
     * 转 JSONObject
     *
     * @param obj
     * @return
     */
    public static JSONObject toJSONObject(Object obj) {
        String json = JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        return JSONObject.parseObject(json);
    }

    /**
     * 用的是 myJSONObject.getJsonobject() 转换
     *
     * @param myJSONObjects
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> toObject(List<MyJSONObject> myJSONObjects, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (myJSONObjects != null) {
            for (MyJSONObject myJSONObject : myJSONObjects) {
                T t = JSONObject.toJavaObject(myJSONObject.getJsonobject(), clazz);
                list.add(t);
            }
        }
        return list;
    }

    /**
     * new MyJSONObject(null, null, null, JSONTool.toJSONObject(obj))
     *
     * @param list
     * @return
     */
    public static <T> List<MyJSONObject> toMyJSONObject(List<T> list) {
        List<MyJSONObject> myShowLayers = new ArrayList<>();
        if (list != null) {
            for (Object obj : list) {
                myShowLayers.add(toMyJSONObject(obj));
            }
        }
        return myShowLayers;
    }

    public static String getString(MyJSONObject myJSONObject, String key) {
        return myJSONObject.getJsonobject().getString(key);
    }

    public static MyJSONObject toMyJSONObject(Object obj) {
        return new MyJSONObject(UUID.randomUUID().toString(), null, null, JSONTool.toJSONObject(obj));
    }


}

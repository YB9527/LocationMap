package com.xupu.locationmap.projectmanager.service;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.ReflectTool;
import com.xupu.locationmap.projectmanager.po.NF;
import com.xupu.locationmap.projectmanager.po.XZDM;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class ItemListService {
    /**
     * like 查询  只有后缀 “%”
     *
     * @param mark
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> findAll(String mark, Class<T> tClass) {
        List<T> list = RedisTool.findListRedis(mark + "%", tClass);
        return list;
    }



    @TargetApi(Build.VERSION_CODES.N)
    public static void addItem(String mark, JSONObject json) {
        String own = UUID.randomUUID().toString();
        json.replace("id",own);
        mark =  mark+"_"+own;
        RedisTool.saveRedis(mark, json);
    }


    public static void deleteItem(String mark) {
        RedisTool.deleteRedisByMark(mark);
    }

    public static void updateByMark(String mark, Object obj) {
        RedisTool.updateRedis(mark, obj);
    }
}

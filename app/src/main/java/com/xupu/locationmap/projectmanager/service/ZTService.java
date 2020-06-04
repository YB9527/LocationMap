package com.xupu.locationmap.projectmanager.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.HttpRespon;
import com.xupu.locationmap.common.tools.JSONObjectRespon;
import com.xupu.locationmap.common.tools.OkHttpClientUtils;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.common.tools.ZTRespon;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZTService {

    public static String XZQ = "行政区";

    public static String ID = "id";
    public static String APP_ID = "appid";
    public static String PROJECT_TABLE_NAME = "项目列表";
    public static String PROJECT_TABLE_LIST = "表列表_根据项目类型";
    public static String TABLE_FILED_LIST = "字段列表_根据表ID";
    public static String TABLE_Structure = "表结构";
    public static String QUAN_SHU_ZHU_TI = "权属主体";
    public static String DK_List = "地块列表";
    public static String TASK_LIST = "任务列表_根据项目类型";
    public static String XZQ_LIST = "行政区列表";
    private static Map<String, String> apidataList;
    //通过表格item 中的id 得到表格的id
    private static Map<String, String> tableIdMap;

    /**
     * 通过名字 得到 表格
     *
     * @param name
     * @param myCallback
     */
    public static void getTableId(String name, final Callback<String> myCallback) {
        synchronized (ZTService.class) {
            if (apidataList == null) {
                apidataList = new HashMap<>();
                new OkHttpClientUtils.GetBuild(Tool.getHostAddress()).addUrl("list").ztBuildToGet(new ZTRespon() {
                    @Override
                    public void onSuccess(JSONArray tablelist) {
                        for (int i = 0; i < tablelist.size(); i++) {
                            JSONObject table = tablelist.getJSONObject(i);
                            apidataList.put(table.getString("text"), table.getString("value"));

                        }
                        myCallback.call(apidataList.get(name));
                    }
                });
            } else {
                myCallback.call(apidataList.get(name));
            }
        }
    }

    /**
     * 通过idd得到表格  筛选出 值appid的想
     *
     * @param tableid
     * @param appid
     * @param callback
     */
    public static void getTableItemList(String tableid, String appid, final Callback<JSONArray> callback) {
        new OkHttpClientUtils.GetBuild().addUrl(tableid).addParamter("appid", appid).ztBuildToGet(new ZTRespon() {
            @Override
            public void onSuccess(JSONArray tablelist) {
                callback.call(tablelist);
            }
        });
    }

    /**
     * @param tableid  项目表格id
     * @param callback
     */
    public static void getTableItemList(String tableid, final Callback<JSONArray> callback) {
        //拿到所有项目
        new OkHttpClientUtils.GetBuild().addUrl(tableid + "/p").ztBuildToGet(new ZTRespon() {
            @Override
            public void onSuccess(JSONArray projects) {
                callback.call(projects);
            }
        });
    }

    public static Map<String, String> getTableIdMap() {
        synchronized (ZTService.class) {
            if (tableIdMap == null) {
                tableIdMap = new HashMap<>();
                tableIdMap.put("03a5042f-fdba-483b-b0b7-c7706c22bb83", "313af5b5-44b0-4e41-88eb-d62dfc1dc7a4");//地块
                tableIdMap.put("cef06e0e-3fd2-4cce-8451-f0ea1c36a5ca", "bccbd22d-b5b8-4eb3-8225-acce9cd676bf");//行政区
                tableIdMap.put("aacd513d-e833-45cd-80c1-95dbec9ebaaa", "a43378a6-bf48-4023-95f3-b3378f27b951");//权属主体
                tableIdMap.put("6646c538-8a85-43fe-b102-5619baad0947", "bccbd22d-b5b8-4eb3-8225-acce9cd676bf");//潜力图斑
            }
        }
        return tableIdMap;
    }

    public static String getTableIdByItemId(String itemid) {

        String tableid = getTableIdMap().get(itemid);
        if (tableid == null) {
            AndroidTool.showAnsyTost("没有找到对应的表格,需要改后台api", 1);
            try {
                Thread.sleep(2000);
                throw new RuntimeException("没有找到对应的表格,需要改后台api");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return tableid;
    }

    public static String getItemIdByTableId(String itemid) {
       /* for (String tableid : getTableIdMap().keySet()) {
            if (tableIdMap.get(tableid).equals(itemid)) {
                return tableid;
            }
        }*/
        return getTableIdMap().get(itemid);
    }

    /**
     * 通过任务表格id 得到 正确表格id
     *
     * @param tasktableid
     * @return
     *//*
    public static String getTasktableId(String tasktableid) {
        for (String key : getTableIdMap().keySet()) {
            if (tableIdMap.get(key).equals(tasktableid)) {
                return key;
            }
        }
        return null;
    }*/
}

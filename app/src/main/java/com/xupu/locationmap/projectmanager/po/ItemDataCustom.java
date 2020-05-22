package com.xupu.locationmap.projectmanager.po;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemDataCustom {

    public ItemDataCustom(Integer rid, JSONObject jsonObject, Map<Integer, FiledCustom> map) {
        this.jsonObject = jsonObject;
        this.map = map;
        this.rid = rid;
    }

    private Integer rid;
    private JSONObject jsonObject;
    private Map<Integer, FiledCustom> map;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Map<Integer, FiledCustom> getMap() {
        return map;
    }

    public void setMap(Map<Integer, FiledCustom> map) {
        this.map = map;
    }



}

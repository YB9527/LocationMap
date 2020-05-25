package com.xupu.locationmap.projectmanager.po;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemDataCustom {

    public ItemDataCustom(Integer rid, MyJSONObject jsonObject, Map<Integer, FiledCustom> map) {
        this.myJSONObject = jsonObject;
        this.map = map;
        this.rid = rid;
    }

    private Integer rid;
    private MyJSONObject myJSONObject;
    private Map<Integer, FiledCustom> map;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Map<Integer, FiledCustom> getMap() {
        return map;
    }

    public void setMap(Map<Integer, FiledCustom> map) {
        this.map = map;
    }

    public MyJSONObject getMyJSONObject() {
        return myJSONObject;
    }

    public void setMyJSONObject(MyJSONObject myJSONObject) {
        this.myJSONObject = myJSONObject;
    }
}

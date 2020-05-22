package com.xupu.locationmap.projectmanager.po;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class RecyclerViewFiledCustom extends FiledCustom {
    private Integer rid;
    private List<JSONObject> jsonObjects;
    private Map<Integer, FiledCustom> map;


    public RecyclerViewFiledCustom() {

    }

    public RecyclerViewFiledCustom(Integer rid, List<JSONObject> jsonObjects, Map<Integer, FiledCustom> map) {
        this.rid = rid;
        this.jsonObjects = jsonObjects;
        this.map = map;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public List<JSONObject> getJsonObjects() {
        return jsonObjects;
    }

    public void setJsonObjects(List<JSONObject> jsonObjects) {
        this.jsonObjects = jsonObjects;
    }

    public Map<Integer, FiledCustom> getMap() {
        return map;
    }

    public void setMap(Map<Integer, FiledCustom> map) {
        this.map = map;
    }
}

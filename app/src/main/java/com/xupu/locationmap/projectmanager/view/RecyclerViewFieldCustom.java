package com.xupu.locationmap.projectmanager.view;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewFieldCustom extends FieldCustom {
    private Integer rid;
    private List<JSONObject> jsonObjects;
    private Map<Integer, FieldCustom> map;


    public RecyclerViewFieldCustom() {

    }

    public RecyclerViewFieldCustom(Integer recyclerRid, Map<Integer, FieldCustom> map) {
        this.rid = rid;
        this.jsonObjects = jsonObjects;
        this.map = map;
    }

    public static class Builder {
        private Integer recyclerRid;
        private Map<Integer, FieldCustom> map ;

        public Builder(Integer recyclerRid){
            map = new HashMap<>();
            this.recyclerRid =recyclerRid;
        }
        public  Builder addFieldCustom(Integer rid, FieldCustom filedCustom) {
            map.put(rid, filedCustom);
            return this;
        }

        public RecyclerViewFieldCustom build() {
            return new RecyclerViewFieldCustom(recyclerRid, map);
        }
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

    public Map<Integer, FieldCustom> getMap() {
        return map;
    }

    public void setMap(Map<Integer, FieldCustom> map) {
        this.map = map;
    }
}

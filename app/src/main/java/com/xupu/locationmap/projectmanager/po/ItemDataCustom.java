package com.xupu.locationmap.projectmanager.po;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDataCustom {

    private List<FiledCustom> filedCustoms;


    public ItemDataCustom(Integer rid, MyJSONObject jsonObject, List<FiledCustom> filedCustoms) {
        this.myJSONObject = jsonObject;
        this.filedCustoms =filedCustoms;
        this.rid = rid;
    }

    private Integer rid;
    private MyJSONObject myJSONObject;

    public List<FiledCustom> getFiledCustoms() {
        return filedCustoms;
    }

    public void setFiledCustoms(List<FiledCustom> filedCustoms) {
        this.filedCustoms = filedCustoms;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }


    public MyJSONObject getMyJSONObject() {
        return myJSONObject;
    }

    public void setMyJSONObject(MyJSONObject myJSONObject) {
        this.myJSONObject = myJSONObject;
    }
}

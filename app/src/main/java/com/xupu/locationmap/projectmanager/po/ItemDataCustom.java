package com.xupu.locationmap.projectmanager.po;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.xupu.locationmap.usermanager.po.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDataCustom {

    private List<FiledCustom> filedCustoms;


    public ItemDataCustom(Integer rid, MyJSONObject jsonObject, List<FiledCustom> filedCustoms) {
        this.myJSONObject = jsonObject;
        this.filedCustoms = filedCustoms;
        this.rid = rid;
    }

    private Integer rid;
    private MyJSONObject myJSONObject;

    public ItemDataCustom(Integer rid, Object obj, List<FiledCustom> filedCustoms) {
        MyJSONObject myJSONObject = new MyJSONObject();
        String json = JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        JSONObject jsonObject = JSONObject.parseObject(json);
        myJSONObject.setJsonobject(jsonObject);

        this.myJSONObject = myJSONObject;
        this.filedCustoms = filedCustoms;
        this.rid = rid;
    }

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

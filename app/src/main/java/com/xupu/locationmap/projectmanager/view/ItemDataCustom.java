package com.xupu.locationmap.projectmanager.view;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.List;

public class ItemDataCustom {

    private List<FieldCustom> filedCustoms;


    public ItemDataCustom(Integer rid, MyJSONObject jsonObject, List<FieldCustom> filedCustoms) {
        this.myJSONObject = jsonObject;
        this.filedCustoms = filedCustoms;
        this.rid = rid;
    }

    private Integer rid;
    private MyJSONObject myJSONObject;

    public ItemDataCustom(Integer rid, Object obj, List<FieldCustom> filedCustoms) {
        MyJSONObject myJSONObject = new MyJSONObject();
        String json = JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        JSONObject jsonObject = JSONObject.parseObject(json);
        myJSONObject.setJsonobject(jsonObject);

        this.myJSONObject = myJSONObject;
        this.filedCustoms = filedCustoms;
        this.rid = rid;
    }

    public List<FieldCustom> getFieldCustoms() {
        return filedCustoms;
    }

    public void setFieldCustoms(List<FieldCustom> filedCustoms) {
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

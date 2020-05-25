package com.xupu.locationmap.projectmanager.po;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.tools.Tool;

import java.io.Serializable;

public class MyJSONObject implements Serializable {
    /**
     * 对象id
     */
    private String id;
    /**
     * 表格名称
     */
    private String tablename;
    /**
     * 父对象id
     */
    private String parentid;
    /**
     * json 数据
     */
    private String json;
    /**
     * JSONOBJECT
     */
    private JSONObject jsonobject;

    public MyJSONObject() {

    }

    public MyJSONObject(String id, String tablename, String parentid, String json) {
        this.id = id;
        this.tablename = tablename;
        this.parentid = parentid;
        this.setJson(json);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {

        this.json = json;
        if (Tool.isEmpty(json)) {
            this.jsonobject = new JSONObject();
        } else {
            this.jsonobject = JSONObject.parseObject(json);
        }
    }

    public JSONObject getJsonobject() {
        return jsonobject;
    }

    public void setJsonobject(JSONObject jsonobject) {
        this.jsonobject = JSONObject.parseObject(jsonobject.toJSONString());
        toJson();
    }

    /**
     * 转到json对象
     *
     * @return
     */
    public MyJSONObject toJsonObject() {
        jsonobject = JSONObject.parseObject(json);
        return this;
    }

    /**
     * 转到 json 字符串
     *
     * @return
     */
    public MyJSONObject toJson() {
        json = jsonobject.toJSONString();
        return this;
    }

    @Override
    public String toString() {
        return "MyJSONObject{" +
                "json='" + json + '\'' +
                ", jsonobject=" + jsonobject +
                '}';
    }
}

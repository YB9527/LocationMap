package com.xupu.locationmap.projectmanager.po;

import com.alibaba.fastjson.JSONObject;

public class MyJSONObject {
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
    }

    public JSONObject getJsonobject() {
        return jsonobject;
    }

    public void setJsonobject(JSONObject jsonobject) {
        this.jsonobject = jsonobject;
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
}

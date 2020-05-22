package com.xupu.locationmap.projectmanager.po;


import java.util.Date;

public class SugProject {

    private String id;

    private String name;

    private String appid;
    private String rootdomain;

    private String workdomain;

    private  String srs;

    private String creator;

    private String createtime;

    private String edittime;

    private String remark;

    private Double version;

    private String dbconn;

    private String gdbconn;

    private  Integer type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppid() {

        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRootdomain() {
        return rootdomain;
    }

    public void setRootdomain(String rootdomain) {
        this.rootdomain = rootdomain;
    }

    public String getWorkdomain() {
        return workdomain;
    }

    public void setWorkdomain(String workdomain) {
        this.workdomain = workdomain;
    }

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String getDbconn() {
        return dbconn;
    }

    public void setDbconn(String dbconn) {
        this.dbconn = dbconn;
    }

    public String getGdbconn() {
        return gdbconn;
    }

    public void setGdbconn(String gdbconn) {
        this.gdbconn = gdbconn;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

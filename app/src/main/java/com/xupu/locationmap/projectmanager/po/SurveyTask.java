package com.xupu.locationmap.projectmanager.po;

/**
 * 任务对象
 */
public class SurveyTask {

    private String id;

    private String appid;

    private String tableid;

    private String taskname;

    private int taskseq;

    private int attcount;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public int getTaskseq() {
        return taskseq;
    }

    public void setTaskseq(int taskseq) {
        this.taskseq = taskseq;
    }

    public int getAttcount() {
        return attcount;
    }

    public void setAttcount(int attcount) {
        this.attcount = attcount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

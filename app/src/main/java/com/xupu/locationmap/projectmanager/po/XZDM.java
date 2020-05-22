package com.xupu.locationmap.projectmanager.po;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public  class XZDM {
    private String id;
    private String parentid;
    private Integer sid;
    private String appid;
    private String code;
    private String caption;
    private Boolean isdeafault;
    private String remark;
    private String referid;

    public XZDM(){

    }
    public XZDM(String code,String caption){
        this.code =code;
        this.caption =caption;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getIsdeafault() {
        return isdeafault;
    }

    public void setIsdeafault(Boolean isdeafault) {
        this.isdeafault = isdeafault;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReferid() {
        return referid;
    }

    public void setReferid(String referid) {
        this.referid = referid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XZDM xzdm = (XZDM) o;
        return id.equals(xzdm.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "XZDM{" +
                "id='" + id + '\'' +
                ", parentid='" + parentid + '\'' +
                ", sid=" + sid +
                ", appid='" + appid + '\'' +
                ", code='" + code + '\'' +
                ", caption='" + caption + '\'' +
                ", isdeafault=" + isdeafault +
                ", remark='" + remark + '\'' +
                ", referid='" + referid + '\'' +
                '}';
    }
}

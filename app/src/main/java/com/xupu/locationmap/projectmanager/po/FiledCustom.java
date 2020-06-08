package com.xupu.locationmap.projectmanager.po;

import com.xupu.locationmap.common.tools.AndroidTool;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 表格字段的封装映射
 */
public class FiledCustom implements Serializable {
    private Integer id;
    private String idText;
    private String attribute;

    public FiledCustom() {

    }
    public FiledCustom(Integer id){
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public FiledCustom(String idText, String attribute) {
        this.idText = idText;
        this.id = AndroidTool.getCompentID("id", idText);
        this.attribute = attribute;
    }

    public FiledCustom(Integer id, String attribute) {
        this.id = id;
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getIdText() {

        return idText;
    }

    public void setIdText(String idText) {
        this.id = AndroidTool.getCompentID("id", idText);
        this.idText = idText;
    }
}

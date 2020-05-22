package com.xupu.locationmap.projectmanager.po;

import java.lang.reflect.Method;

/**
 * 表格字段的封装映射
 */
public  class FiledCustom {
    private String attribute;


    public FiledCustom() {

    }

    public FiledCustom(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }



}

package com.xupu.locationmap.projectmanager.po;

import java.util.List;

public class Project {
    private  String name;
    private List<XZDM> xzdms;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<XZDM> getXzdms() {
        return xzdms;
    }

    public void setXzdms(List<XZDM> xzdms) {
        this.xzdms = xzdms;
    }
}

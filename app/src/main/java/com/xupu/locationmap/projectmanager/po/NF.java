package com.xupu.locationmap.projectmanager.po;

import java.util.ArrayList;
import java.util.List;

public class NF {
    private String id;
    private String name;
    private String bz;

    public NF(){

    }
    public NF(String name, String bz) {
        this.name = name;
        this.bz = bz;
    }





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

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }


}

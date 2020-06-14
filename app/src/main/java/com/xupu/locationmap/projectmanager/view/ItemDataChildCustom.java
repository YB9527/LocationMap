package com.xupu.locationmap.projectmanager.view;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.List;

public class ItemDataChildCustom {


    private List<MyJSONObject>  childs;
    private List<FieldCustom> filedCustoms;


    public ItemDataChildCustom(List<MyJSONObject> childs, List<FieldCustom> filedCustoms) {
        this.childs = childs;
        this.filedCustoms = filedCustoms;
    }

    public List<FieldCustom> getFieldCustoms() {
        return filedCustoms;
    }

    public void setFieldCustoms(List<FieldCustom> filedCustoms) {
        this.filedCustoms = filedCustoms;
    }

    public List<MyJSONObject> getChilds() {
        return childs;
    }

    public void setChilds(List<MyJSONObject> childs) {
        this.childs = childs;
    }
}

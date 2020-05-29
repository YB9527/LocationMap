package com.xupu.locationmap.projectmanager.po;

import java.util.List;
import java.util.Map;

public class ItemDataChildCustom {


    private List<MyJSONObject>  childs;
    private List<FiledCustom> filedCustoms;


    public ItemDataChildCustom(List<MyJSONObject> childs, List<FiledCustom> filedCustoms) {
        this.childs = childs;
        this.filedCustoms = filedCustoms;
    }

    public List<FiledCustom> getFiledCustoms() {
        return filedCustoms;
    }

    public void setFiledCustoms(List<FiledCustom> filedCustoms) {
        this.filedCustoms = filedCustoms;
    }

    public List<MyJSONObject> getChilds() {
        return childs;
    }

    public void setChilds(List<MyJSONObject> childs) {
        this.childs = childs;
    }
}

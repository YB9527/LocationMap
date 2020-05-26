package com.xupu.locationmap.projectmanager.po;

import java.util.List;
import java.util.Map;

public class ItemDataChildCustom {


    private List<MyJSONObject>  childs;
    private Map<Integer, FiledCustom> map;


    public ItemDataChildCustom(List<MyJSONObject> childs, Map<Integer, FiledCustom> map) {
        this.childs = childs;
        this.map = map;
    }

    public Map<Integer, FiledCustom> getMap() {
        return map;
    }

    public void setMap(Map<Integer, FiledCustom> map) {
        this.map = map;
    }

    public List<MyJSONObject> getChilds() {
        return childs;
    }

    public void setChilds(List<MyJSONObject> childs) {
        this.childs = childs;
    }
}

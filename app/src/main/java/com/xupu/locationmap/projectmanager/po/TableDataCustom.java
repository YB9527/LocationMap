package com.xupu.locationmap.projectmanager.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableDataCustom {

    private boolean isEdit;
    /**
     * 承载 控件的 item
     */
    private int fragmentItem;
    /**
     * 当个item 显示的内容
     */
    private Map<Integer, FiledCustom> map;


    private List<MyJSONObject> list;



    public TableDataCustom(int fragmentItem, Map<Integer, FiledCustom> map, List<MyJSONObject> list) {
        this.fragmentItem = fragmentItem;
        this.map = map;
        this.list = new ArrayList<>();
        this.list.addAll(list);

    }

    public boolean isEdit() {
        return isEdit;
    }

    public TableDataCustom setEdit(boolean edit) {
        isEdit = edit;
        return  this;
    }

    public Map<Integer, FiledCustom> getMap() {
        return map;
    }

    public void setMap(Map<Integer, FiledCustom> map) {
        this.map = map;
    }

    public List<MyJSONObject> getList() {
        return list;
    }

    public void setList(List<MyJSONObject> list) {
        this.list = list;
    }

    public int getFragmentItem() {
        return fragmentItem;
    }

    public void setFragmentItem(int fragmentItem) {
        this.fragmentItem = fragmentItem;
    }
}

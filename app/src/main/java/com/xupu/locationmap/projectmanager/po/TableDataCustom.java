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
    List<FiledCustom> filedCustoms;
    private List<List<FiledCustom>> childRidList;

    private List<MyJSONObject> list;

    public TableDataCustom(){

    }
    public TableDataCustom(int fragmentItem, List<FiledCustom> filedCustoms, List<MyJSONObject> list) {
        this.fragmentItem = fragmentItem;
        this.filedCustoms = filedCustoms;
        //this.list = new ArrayList<>();
        this.list = (list);

    }

    public boolean isEdit() {
        return isEdit;
    }


    public TableDataCustom setEdit(boolean edit) {
        isEdit = edit;
        return this;
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


    public List<FiledCustom> getFiledCustoms() {
        return filedCustoms;
    }

    public List<List<FiledCustom>> getChildRidList() {
        return childRidList;
    }

    public TableDataCustom setChildRidList(List<List<FiledCustom>> childRidList) {
        this.childRidList = childRidList;
        return this;
    }



    public void setFiledCustoms(List<FiledCustom> fs) {
        this.filedCustoms = fs;
    }
}

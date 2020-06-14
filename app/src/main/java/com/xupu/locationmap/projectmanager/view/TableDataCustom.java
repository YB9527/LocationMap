package com.xupu.locationmap.projectmanager.view;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.List;

public class TableDataCustom {

    private boolean isEdit;
    /**
     * 承载 控件的 item
     */
    private int fragmentItem;
    /**
     * 当个item 显示的内容
     */
    List<FieldCustom> FieldCustoms;
    private List<List<FieldCustom>> childRidList;

    private List<MyJSONObject> list;

    public TableDataCustom(){

    }
    public TableDataCustom(int fragmentItem, List<FieldCustom> FieldCustoms, List<MyJSONObject> list) {
        this.fragmentItem = fragmentItem;
        this.FieldCustoms = FieldCustoms;
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


    public List<FieldCustom> getFieldCustoms() {
        return FieldCustoms;
    }

    public List<List<FieldCustom>> getChildRidList() {
        return childRidList;
    }

    public TableDataCustom setChildRidList(List<List<FieldCustom>> childRidList) {
        this.childRidList = childRidList;
        return this;
    }



    public void setFieldCustoms(List<FieldCustom> fs) {
        this.FieldCustoms = fs;
    }
}

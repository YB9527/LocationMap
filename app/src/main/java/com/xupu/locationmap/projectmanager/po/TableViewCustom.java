package com.xupu.locationmap.projectmanager.po;

import android.view.View;

import com.xupu.locationmap.projectmanager.page.ItemFragment;

import java.util.Map;

/**
 * smarttable要装载的单个表格
 */
public class TableViewCustom {

    /**
     * 数据对象
     */
    private Class tClass;
    /**
     * 表格名称
     */
    private String tableName;
    private ItemFragment itemFragMent;
    /**
     * 当个item 显示的内容
     */
    private Map<View, FiledCustom> map;


    public Class gettClass() {
        return tClass;
    }

    public void settClass(Class tClass) {
        this.tClass = tClass;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ItemFragment getItemFragMent() {
        return itemFragMent;
    }

    public void setItemFragMent(ItemFragment itemFragMent) {
        this.itemFragMent = itemFragMent;
    }

    public Map<View, FiledCustom> getMap() {
        return map;
    }

    public void setMap(Map<View, FiledCustom> map) {
        this.map = map;
    }
}

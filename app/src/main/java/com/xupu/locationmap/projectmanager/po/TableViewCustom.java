package com.xupu.locationmap.projectmanager.po;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;


import java.util.Map;

/**
 * smarttable要装载的单个表格
 */
public class TableViewCustom {

    /**
     * 数据对象
     */
    private Class<? extends Fragment> itemFragMentClass;
    /**
     * 表格名称
     */
    private String tableName;

    /**
     * 当个item 显示的内容
     */
    private Map<View, FiledCustom> map;

    /**
     * 给table 注入的 数据（po）
     */
    private Bundle bundle;


    public TableViewCustom(String tableName, Class<? extends Fragment> itemFragMentClass, Map<View, FiledCustom> map, Bundle bundle) {
        this.itemFragMentClass = itemFragMentClass;
        this.tableName = tableName;
        this.map = map;
        this.bundle = bundle;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class<? extends Fragment> getItemFragMentClass() {
        return itemFragMentClass;
    }

    public void setItemFragMentClass(Class<? extends Fragment> itemFragMentClass) {
        this.itemFragMentClass = itemFragMentClass;
    }

    public Map<View, FiledCustom> getMap() {
        return map;
    }

    public void setMap(Map<View, FiledCustom> map) {
        this.map = map;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}

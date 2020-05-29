package com.xupu.locationmap.projectmanager.po;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * smarttable要装载的单个表格
 */
public class TableViewCustom   {

    /**
     * 数据对象
     */
    private   Class<? extends Fragment> itemFragMentClass;
    /**
     * 表格名称
     */
    private String tableName;

    private TableDataCustom tableDataCustom;


    private Type itemClass;

    public TableViewCustom(String tableName, Class<? extends Fragment> itemFragMentClass,Type itemClass) {
        this.itemFragMentClass = itemFragMentClass;
        this.tableName = tableName;
        this.itemClass = itemClass;

    }

    public TableViewCustom(String tableName, Class<? extends Fragment> itemFragMentClass,Type itemClass, TableDataCustom tableDataCustom) {
        this.itemFragMentClass = itemFragMentClass;
        this.tableName = tableName;
        this.tableDataCustom = tableDataCustom;
        this.itemClass = itemClass;

    }

    public Type getItemClass() {
        return itemClass;
    }

    public void setItemClass(Type itemClass) {
        this.itemClass = itemClass;
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

    public TableDataCustom getTableDataCustom() {
        return tableDataCustom;
    }

    public void setTableDataCustom(TableDataCustom tableDataCustom) {
        this.tableDataCustom = tableDataCustom;
    }
}

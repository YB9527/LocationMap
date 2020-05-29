package com.xupu.locationmap.projectmanager.po;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据表格要求 来显示那张表格的数据
 */
public class TableDataCustom_TableName extends TableDataCustom {

    /**
     * 要查询的数据库表格名称 地块
     */
    private String tableName;


    public String getTableName() {

        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 配置表格的 条目内容
     */
    private MyJSONObject tableItem;

    public TableDataCustom_TableName(int fragmentItem,List<FiledCustom> fs, MyJSONObject tableItem) {
        super(fragmentItem, fs, new ArrayList<MyJSONObject>());
        this.tableItem = tableItem;
    }
    public TableDataCustom_TableName(){

    }
    public MyJSONObject getTableItem() {
        return tableItem;
    }

    public void setTableItem(MyJSONObject tableItem) {
        this.tableItem = tableItem;
    }
}

package com.xupu.locationmap.common.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理
 */
public class TableTool {

    private static SQLiteDatabase db;
    public final static String Table_Name = "xupu";

    static {
        if (db == null) {
            PetDbHelper mDbHelper = new PetDbHelper(AndroidTool.getMainActivity());
            db = mDbHelper.getReadableDatabase();
        }
    }

    /**
     * 根据id 查找对象
     *
     * @param id
     * @return
     */
    public static MyJSONObject findById(String id) {
        String sql = "select id,tablename,parentid,json from  " + Table_Name + " where  id =  '" + id + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            return jsonObject;
        }
        return null;
    }


    /**
     * 根据表格名称查找
     *
     * @param tablename
     * @return
     */
    public static List<MyJSONObject> findByTableName(String tablename) {

        String sql = "select id,tablename,parentid,json from " + Table_Name + " where  tablename =  '" + tablename + "'";
        List<MyJSONObject> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            jsons.add(jsonObject);
        }
        return jsons;

    }

    public static List<MyJSONObject> findByTableNameAndParentId(String tablename, String parentid) {

        String sql = "select id,tablename,parentid,json from " + Table_Name + " where  tablename =  '" + tablename + "' AND parentid =  '" + parentid + "'";
        List<MyJSONObject> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            jsons.add(jsonObject);
        }
        return jsons;

    }


    private static MyJSONObject cursorToMyJSONObject(Cursor cursor) {
        MyJSONObject jsonObject = new MyJSONObject(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );
        return jsonObject;
    }

    /**
     * 更具父节点查找
     *
     * @param parentid
     * @return
     */
    public static List<MyJSONObject> findByParentId(String parentid) {
        String sql = "select id,tablename,parentid,json from  " + Table_Name + " where  parentid =  '" + parentid + "'";
        List<MyJSONObject> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            jsons.add(jsonObject);
        }
        return jsons;
    }

    /**
     * 根据id 修改对象
     *
     * @param myJSONObject
     * @return
     */
    public static int updateById(MyJSONObject myJSONObject) {
        myJSONObject.toJson();
        ContentValues values = new ContentValues();
        values.put("tablename", myJSONObject.getTablename());
        values.put("parentid", myJSONObject.getParentid());
        values.put("json", myJSONObject.getJson());
        return db
                .update(Table_Name, values, "id" + " = ?", new String[]{myJSONObject.getId()});
    }


    /**
     * 保存对象
     *
     * @param myJSONObject
     */
    public static boolean insert(MyJSONObject myJSONObject) {
        try {
            myJSONObject.toJson();
            ContentValues values = new ContentValues();
            values.put("id", myJSONObject.getId());
            values.put("tablename", myJSONObject.getTablename());
            values.put("parentid", myJSONObject.getParentid());
            values.put("json", myJSONObject.getJson());
            long count = db.insert(Table_Name, null, values);
            if (count == 0) {
                return false;
            }
        } catch (Exception e) {
            AndroidTool.showAnsyTost("对象有问题无法保存：" + e.getMessage() + ":::" + myJSONObject.toString(), 1);
            return false;
        }
        return true;
    }

    /**
     * 批量保存对象
     *
     * @param list
     */
    public static boolean insertMany(List<MyJSONObject> list) {
        for (MyJSONObject myJSONObject : list) {
            if (!insert(myJSONObject)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 先检查有没有子节点，如果没有才能删除
     *
     * @param myJSONObject
     */
    public static boolean delete(MyJSONObject myJSONObject) {
        List<MyJSONObject> childs = findByParentId(myJSONObject.getId());
        if (Tool.isEmpty(childs)) {
            deleteById(myJSONObject.getId());
        }else{
            AndroidTool.showAnsyTost("有子对象无法删除",1);
            return  false;
        }
        return true;
    }

    /**
     * 通过id 删除 ，没有检查是否有子节点
     *
     * @param id
     * @return
     */
    private static int deleteById(String id) {
        int count = db.delete(Table_Name, "id = ?", new String[]{id});
        return count;
    }

    /**
     * 批量修改
     * @param myJSONObjects
     */
    public static void updateMany(List<MyJSONObject> myJSONObjects) {
        for (MyJSONObject myJSONObject : myJSONObjects) {
            updateById(myJSONObject);
        }
    }
}
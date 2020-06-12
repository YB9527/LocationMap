package com.xupu.locationmap.common.tools;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.exceptionmanager.MapException;
import com.xupu.locationmap.projectmanager.page.ProjectPage;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.Redis;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.ZTService;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理
 */
public class TableTool {

    public static SQLiteDatabase db;
    public final static String Table_Name = "xupu";
    private final static String FIELD = "id,tablename,parentid,json,deletechild,tableid,state";
    public final static int STATE_NONE = 0;
    public final static int STATE_UPDATE = 1;
    public final static int STATE_INSERT = 2;
    public final static int STATE_DELETE = 3;

    static {
        //PetDbHelper mDbHelper = new PetDbHelper(AndroidTool.getMainActivity(),"shelter.db");
        //db = mDbHelper.getReadableDatabase();
        if (db == null) {
            //1、拿到当前的项目
            String projectJson = RedisTool.findRedis(ProjectService.CURRENT_PROJECT_MARK);
            if (Tool.isEmpty(projectJson)) {
                //跳到项目选择页面
                Intent intent = new Intent(AndroidTool.getMainActivity(), ProjectPage.class);
                AndroidTool.getMainActivity().startActivity(intent);
            } else {
                MyJSONObject project = JSONObject.parseObject(projectJson, MyJSONObject.class);
                ProjectService.setCurrentSugProject(project);
                createDB(ProjectService.getName(project)+"_"+ProjectService.getProjectRandom(project));
            }
        }
    }


    /**
     * 根据id 查找对象
     *
     * @param id
     * @return
     */
    public static MyJSONObject findById(String id) {

        return findByIdAndState(id, "!=", STATE_DELETE);
       /* String sql = "select " +FIELD+" from  " + Table_Name + " where  id =  '" + id + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            return jsonObject;
        }
        return null;*/
    }

    /**
     * 根据id 查找对象
     *
     * @param id
     * @param state 不等于这个
     * @return
     */
    public static MyJSONObject findByIdAndState(String id, String smybol, int state) {

        String sql = "select " + FIELD + " from  " + Table_Name + " where  id =  '" + id + "' AND state " + smybol + "  '" + state + "'";
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
    public static ArrayList<MyJSONObject> findByTableNameAndSmybol(String tablename, String smybol, int state) {

        String sql = "select " + FIELD + "  from " + Table_Name + " where  tablename =  '" + tablename + "'AND state " + smybol + " '" + state + "'";
        ArrayList<MyJSONObject> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            jsons.add(jsonObject);
        }
        return jsons;

    }

    public static ArrayList<MyJSONObject> findByTableName(String tablename) {
        return findByTableNameAndSmybol(tablename, "!=", STATE_DELETE);
       /* String sql = "select " +FIELD+"  from " + Table_Name + " where  tablename =  '" + tablename + "'";
        ArrayList<MyJSONObject> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            jsons.add(jsonObject);
        }
        return jsons;
*/
    }

    public static List<MyJSONObject> findByTableNameAndParentIdAndSmybol(String tablename, String parentid, String smybol, int state) {

        String sql = "select " + FIELD + "  from " + Table_Name + " where  tablename =  '" + tablename + "' AND parentid =  '" + parentid + "'AND state " + smybol + "'" + state + "'";
        List<MyJSONObject> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            jsons.add(jsonObject);
        }
        return jsons;
    }

    public static List<MyJSONObject> findByTableNameAndParentId(String tablename, String parentid) {


        return findByTableNameAndParentIdAndSmybol(tablename, parentid, "!=", STATE_DELETE);
    /*    String sql = "select " +FIELD+"  from " + Table_Name + " where  tablename =  '" + tablename + "' AND parentid =  '" + parentid + "'";
        List<MyJSONObject> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            jsons.add(jsonObject);
        }
        return jsons;*/
    }


    private static MyJSONObject cursorToMyJSONObject(Cursor cursor) {
        MyJSONObject jsonObject = new MyJSONObject(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );

        jsonObject.setDeletechild(cursor.getInt(4));
        jsonObject.setTableid(cursor.getString(5));
        return jsonObject;
    }

    /**
     * 更具父节点查找
     *
     * @param parentid
     * @return
     */
    public static List<MyJSONObject> findByParentIdAndSmybol(String parentid, String smybol, int state) {
        String sql = "select " + FIELD + " from  " + Table_Name + " where  parentid =  '" + parentid + "' AND state " + smybol + "  '" + state + "'";
        List<MyJSONObject> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            jsons.add(jsonObject);
        }
        return jsons;
    }

    public static List<MyJSONObject> findByParentId(String parentid) {
        return findByParentIdAndSmybol(parentid, "!=", STATE_DELETE);
      /*  String sql = "select " +FIELD+" from  " + Table_Name + " where  parentid =  '" + parentid + "'";
        List<MyJSONObject> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyJSONObject jsonObject = cursorToMyJSONObject(cursor);
            jsons.add(jsonObject);
        }
        return jsons;*/
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
        values.put("deletechild", myJSONObject.getDeletechild());
        values.put("tableid", myJSONObject.getTableid());
        values.put("state", STATE_UPDATE);
        return db
                .update(Table_Name, values, "id" + " = ?", new String[]{myJSONObject.getId()});
    }

    public static int updateById(MyJSONObject myJSONObject,int state) {
        myJSONObject.toJson();
        ContentValues values = new ContentValues();
        values.put("tablename", myJSONObject.getTablename());
        values.put("parentid", myJSONObject.getParentid());
        values.put("json", myJSONObject.getJson());
        values.put("deletechild", myJSONObject.getDeletechild());
        values.put("tableid", myJSONObject.getTableid());
        values.put("state", state);
        return db
                .update(Table_Name, values, "id" + " = ?", new String[]{myJSONObject.getId()});
    }
    /**
     * 保存对象
     *
     * @param myJSONObject
     */
    public static boolean insert(MyJSONObject myJSONObject, int state) {
        try {
            myJSONObject.toJson();
            ContentValues values = new ContentValues();
            values.put("id", myJSONObject.getId());
            values.put("tablename", myJSONObject.getTablename());
            values.put("parentid", myJSONObject.getParentid());
            values.put("json", myJSONObject.getJson());
            values.put("deletechild", myJSONObject.getDeletechild());
            values.put("tableid", myJSONObject.getTableid());
            values.put("state", state);
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
    public static boolean insertMany(List<MyJSONObject> list, int state) {
        for (MyJSONObject myJSONObject : list) {
            if (!insert(myJSONObject, state)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 先检查有没有子节点，如果没有才能删除
     * 只是改变了对象的状态，并没有删除
     * @param myJSONObject
     */
    public static boolean delete(MyJSONObject myJSONObject) {
        List<MyJSONObject> childs = findByParentId(myJSONObject.getId());
        boolean flag = true;
        if (Tool.isEmpty(childs)) {
            flag = true;
            //deleteById(myJSONObject.getId());
            myJSONObject.setState(STATE_DELETE);
            updateById(myJSONObject,STATE_DELETE);
        } else {
            for (MyJSONObject js : childs) {
                if (js.getDeletechild() == 0) {
                    //删除地块改为修改状态
                    myJSONObject.setState(STATE_DELETE);
                    js.setState(STATE_DELETE);
                    updateById(myJSONObject,STATE_DELETE);
                    updateById(js,STATE_DELETE);
                    //deleteByParentId(myJSONObject.getId());
                    //deleteById(myJSONObject.getId());
                    return true;
                }
            }
            AndroidTool.showAnsyTost("有子对象无法删除", 1);
            return false;
        }
        return true;
    }

    /**
     * 最后的删除
     *
     * @param myJSONObject
     */
    public static boolean deletefinal(MyJSONObject myJSONObject) {
        List<MyJSONObject> childs = findByParentId(myJSONObject.getId());
        boolean flag = true;
        if (Tool.isEmpty(childs)) {
            flag = true;
            deleteById(myJSONObject.getId());

        } else {
            for (MyJSONObject js : childs) {
                if (js.getDeletechild() == 0) {
                    //删除地块改为修改状态
                    deleteByParentId(myJSONObject.getId());
                    deleteById(myJSONObject.getId());
                    return true;
                }
            }
            AndroidTool.showAnsyTost("有子对象无法删除", 1);
            return false;
        }
        return true;
    }

    private static int deleteByParentId(String id) {
        int count = db.delete(Table_Name, "parentid = ?", new String[]{id});
        return count;
    }

    /**
     * 通过id 删除 ，没有检查是否有子节点
     *
     * @param id
     * @return
     */
    private static int deleteById(String id) {

        //修改地图状态

        int count = db.delete(Table_Name, "id = ?", new String[]{id});
        return count;
    }

    /**
     * 批量修改
     *
     * @param myJSONObjects
     */
    public static int updateMany(List<MyJSONObject> myJSONObjects) {
        int count = 0;
        for (MyJSONObject myJSONObject : myJSONObjects) {
            count += updateById(myJSONObject);
        }
        return count;
    }


    public static void createDB(String projectTableName) {
        PetDbHelper mDbHelper = new PetDbHelper(AndroidTool.getMainActivity(), projectTableName + ".db");
        db = mDbHelper.getReadableDatabase();
    }
}

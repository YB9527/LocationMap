package com.xupu.locationmap.common.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RedisTool {


    private static SQLiteDatabase db;
    public final static String REDIS = "redis.db";

    /**
     * 得到本地 sqlite 数据库
     *
     * @return
     */
    private static SQLiteDatabase getSQLiteDatabase() {
        if (db == null) {
            PetDbHelper mDbHelper = new PetDbHelper(AndroidTool.getMainActivity(), REDIS);
            db = mDbHelper.getReadableDatabase();
        }
        return db;
    }

    /**
     * 查找本地 sqlite 的json 数据 如果没有返回 null
     *
     * @param mark
     * @return
     */
    public static String findRedis(String mark) {
        String json = null;
        SQLiteDatabase db = getSQLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("mark", mark);
        Cursor cursor = db.rawQuery("select json from redis where mark = ?", new String[]{mark});

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            json = cursor.getString(0);
        }
        cursor.close();
        return json;
    }

    /**
     * 修改redis 如果没有就增加
     *
     * @param mark
     * @param json
     */
    public static void updateRedis(String mark, String json) {
        SQLiteDatabase db = getSQLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("mark", mark);
        values.put("json", json);
        int count = db
                .update("redis", values, "mark" + " = ?", new String[]{mark});
        if (count == 0) {
            insertData(mark, json);
        }

    }

    /**
     * 修改redis 如果没有就增加
     *
     * @param mark
     * @param obj  obj转换为json 进行保存
     */
    public static void updateRedis(String mark, Object obj) {
        if (obj == null) {
            updateRedis(mark, "");
        } else {
            updateRedis(mark, new Gson().toJson(obj));
        }
    }

    /**
     * 插入数据
     *
     * @param mark
     * @param json
     */
    private static void insertData(String mark, String json) {

        SQLiteDatabase db = getSQLiteDatabase();

        ContentValues values = new ContentValues();
        values.put("mark", mark);
        values.put("json", json);
        long count = db.insert("redis", null, values);

    }

    /**
     * 在 本地数据中找 缓存数据， 并转换成json
     *
     * @param mark
     * @param type 转换成的结果对象
     * @return
     */
    public static <T> T findRedis(String mark, Type type) {
        String json = findRedis(mark);
        if (Tool.isEmpty(json)) {
            return null;
        }
        return Tool.JsonToObject(json, type);
    }

    /**
     * 保存 redis ，
     *
     * @param mark
     * @param obj
     * @return
     */
    public static void saveRedis(String mark, Object obj) {
        String json = new Gson().toJson(obj);
        insertData(mark, json);
    }
    /**
     * 保存 redis ，
     * @param mark
     * @param obj
     * @return
     */
 /*   public static void  saveRedis(String mark,Object obj) {
        String jsonInDatabase = findRedis(mark);
        if (Tool.isEmpty(jsonInDatabase)) {
            String json =new Gson().toJson(obj);
            insertData(mark, json);

        }else{
            updateRedis(mark,obj);
        }
    }*/

    /**
     * @param beginMark 匹配开始 mark 值
     * @param type
     * @return
     */
    public static <T> ArrayList<T> findListRedis(String beginMark, Type type) {
        ArrayList<T> list = new ArrayList<>();
        List<String> jsons = findListRedis(beginMark);
        for (String json : jsons
        ) {
            T t = Tool.JsonToObject(json, type);
            list.add(t);
        }
        return list;
    }

    public static List<String> findListRedis(String mark) {
        SQLiteDatabase db = getSQLiteDatabase();
        String sql = "select json from  redis where  mark like  '" + mark + "'";

        //sql = "select json from  redis";
        List<String> jsons = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String json = cursor.getString(0);
            jsons.add(json);
        }
        return jsons;

    }

    /**
     * 根据mark 删除地块
     *
     * @param mark
     */
    public static int deleteRedisByMark(String mark) {
        SQLiteDatabase db = getSQLiteDatabase();
        int count = db.delete("redis", "mark = ?", new String[]{mark});
        return count;
    }
}

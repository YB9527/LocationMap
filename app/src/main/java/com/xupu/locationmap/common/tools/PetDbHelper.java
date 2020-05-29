package com.xupu.locationmap.common.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xupu.locationmap.projectmanager.po.Redis;

public class PetDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static String DATABASE_NAME = "shelter.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 10;

    /**
     * Constructs a new instance of {@link PetDbHelper}.
     *
     * @param context of the app
     */
    public PetDbHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        DATABASE_NAME = databaseName;
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        if (db.getPath().contains(RedisTool.REDIS)) {
            String SQL_CREATE_PETS_TABLE2 = "CREATE TABLE redis (id INTEGER PRIMARY KEY AUTOINCREMENT,mark  TEXT NOT NULL,json TEXT)";
            db.execSQL(SQL_CREATE_PETS_TABLE2);
        } else {
            String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + TableTool.Table_Name + " (id TEXT  NOT NULL ,tablename TEXT  NOT NULL,parentid TEXT,json  NOT NULL,deletechild integer,tableid TEXT)";
            db.execSQL(SQL_CREATE_PETS_TABLE);
        }
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        if (!db.getPath().contains(RedisTool.REDIS)) {
            if (oldVersion == 1) {
                //增加是否允许删除子节点
                String sql = "ALTER TABLE " + TableTool.Table_Name + " ADD COLUMN deletechild Integer";
                db.execSQL(sql);
            }
            if (oldVersion == 9) {
                //增加 下载数据库时的表格tableid
                String sql = "ALTER TABLE " + TableTool.Table_Name + " ADD COLUMN tableid TEXT";
                db.execSQL(sql);
            }
        }

    }
}

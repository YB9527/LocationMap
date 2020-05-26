package com.xupu.locationmap.common.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PetDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "shelter.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 9;

    /**
     * Constructs a new instance of {@link PetDbHelper}.
     *
     * @param context of the app
     */
    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + TableTool.Table_Name + " (id TEXT  NOT NULL ,tablename TEXT  NOT NULL,parentid TEXT,json  NOT NULL,deletechild integer)";
        String SQL_CREATE_PETS_TABLE2 = "CREATE TABLE redis (id INTEGER PRIMARY KEY AUTOINCREMENT,mark  TEXT NOT NULL,json TEXT)";
        db.execSQL(SQL_CREATE_PETS_TABLE);
        db.execSQL(SQL_CREATE_PETS_TABLE2);

    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        String sql = "ALTER TABLE " + TableTool.Table_Name + " ADD COLUMN deletechild Integer";
        db.execSQL(sql);
    }

}

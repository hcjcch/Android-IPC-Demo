package com.hcjcch.ipctest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 请用一句话描述
 * <p>
 * Created by hcjcch on 2017/5/19.
 */

public class IPCDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ipc";
    public static final String TABLE_NAME_BOOK = "book";
    public static final String TABLE_NAME_STUDENT = "student";

    private static final int DB_VERSION = 1;

    public IPCDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BOOK + "(_id INTEGER PRIMARY KEY,name TEXT)";
        db.execSQL(CREATE_BOOK_TABLE);
        String CREATE_STUDENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_STUDENT + "(_id INTEGER PRIMARY KEY,name TEXT)";
        db.execSQL(CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

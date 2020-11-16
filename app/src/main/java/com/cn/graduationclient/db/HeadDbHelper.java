package com.cn.graduationclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HeadDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database";
    private static final String HEAD_TABLE_NAME = "head";
    private static final int HEAD_DATABASE_VISION = 1;
    public HeadDbHelper(@Nullable Context context) {
        super(context, HEAD_TABLE_NAME, null, HEAD_DATABASE_VISION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table="create table "+HEAD_TABLE_NAME+"(uid verchar(11) not null,msg verchar(10000))";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + HEAD_TABLE_NAME);
        onCreate(db);
    }
}

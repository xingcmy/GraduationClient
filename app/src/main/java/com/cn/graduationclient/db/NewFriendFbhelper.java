package com.cn.graduationclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NewFriendFbhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database";
    private static final String Friend_TABLE_NAME = "newfriend";
    private static final int FRIEND_DATABASE_VISION = 1;
    public NewFriendFbhelper(@Nullable Context context) {
        super(context, Friend_TABLE_NAME,null, FRIEND_DATABASE_VISION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table="create table "+Friend_TABLE_NAME+"(uid verchar(11) not null,friend verchar(11) not null,msg verchar(10000),type verchar(45))";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Friend_TABLE_NAME);
        onCreate(db);
    }
}

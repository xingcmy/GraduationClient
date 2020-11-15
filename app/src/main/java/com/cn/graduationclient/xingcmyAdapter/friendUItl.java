package com.cn.graduationclient.xingcmyAdapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cn.graduationclient.db.FriendDbHelper;

import java.util.ArrayList;
import java.util.List;

public class friendUItl {
    public static FriendDbHelper friendDbHelper;
    public static SQLiteDatabase sqLiteDatabase;
    public static List<friends> getFriend(Context context,String UID){

        List<friends> list=new ArrayList<>();
        friendDbHelper=new FriendDbHelper(context);
        sqLiteDatabase=friendDbHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from friend where uid='"+UID+"'",null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String name=cursor.getString(1);
                String msg=cursor.getString(2);
                String time=cursor.getString(3);
                int type=cursor.getInt(4);
                friends friend=new friends(name,msg,time,type);
                list.add(friend);
            }
            return list;
        }
        return null;
    }
}

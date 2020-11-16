package com.cn.graduationclient.xingcmyAdapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cn.graduationclient.db.NewFriendFbhelper;

import java.util.ArrayList;
import java.util.List;

public class NewfriendUitl {

    public static NewFriendFbhelper newFriendFbhelper;
    public static SQLiteDatabase sqLiteDatabase;

    /**
     *
     * @param context
     * @param UID 登录账号
     * @return 好友验证消息列表
     */
    public List<Newfriend> getNewFriend(Context context,String UID){
        List<Newfriend> list=new ArrayList<>();
        newFriendFbhelper=new NewFriendFbhelper(context);
        sqLiteDatabase=newFriendFbhelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from newfriend where uid='"+UID+"'",null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String id=cursor.getString(1);
                String msg=cursor.getString(2);
                String type=cursor.getString(3);
                Newfriend newfriend=new Newfriend(id,msg,type);
                list.add(newfriend);
            }
            return list;
        }
        return null;
    }
}

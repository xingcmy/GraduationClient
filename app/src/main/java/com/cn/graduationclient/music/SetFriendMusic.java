package com.cn.graduationclient.music;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class SetFriendMusic {

    public static List<FriendMusic> getMusic(Context context,String[] FMusic,String[] name){
        List<FriendMusic> list=new ArrayList<>();
        int count=name.length;
        if (count>0){
            for (int i=0;i<count;i++){
                FriendMusic friendMusic=new FriendMusic(name[i],FMusic[i]);
                list.add(friendMusic);
            }
        }

        return list;
    }
}

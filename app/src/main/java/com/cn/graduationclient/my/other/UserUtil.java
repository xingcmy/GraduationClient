package com.cn.graduationclient.my.other;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.cn.graduationclient.http.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserUtil {

    public static List<User> getUserInformation(Context context,String[] id,String[] name,String[] signature,String[] sex,String[] birthday,String[] profession,String[] email,String[] city){

        List<User> lists=new ArrayList<>();
        int li=id.length;
        if (li>0){
            for (int i=0;i<li;i++){
                //System.out.println(id[0]);
                User user=new User(id[i],name[i],signature[i],sex[i],birthday[i],profession[i],email[i],city[i]);
                lists.add(user);
            }
            return lists;
        }
        return null;



    }

}

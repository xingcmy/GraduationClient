package com.cn.graduationclient.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.my.other.Information;
import com.cn.graduationclient.my.other.User;
import com.cn.graduationclient.my.other.UserUtil;
import com.cn.graduationclient.xingcmyAdapter.FriendAdapter;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class friend extends Activity {

    ListView friend_list;

    List<User> userList;

    HttpUtil httpUtil=new HttpUtil();

    String friendId;
    String UID;
    int num;

    String[] id;
    String[] name;
    String[] signature;
    String[] sex;
    String[] birthday;
    String[] profession;
    String[] email;
    String[] city;

    String[] FI;

    Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        HoldTitle friend_hold=findViewById(R.id.friend_hold);
        friend_list=findViewById(R.id.friend_list);

        Intent intent=getIntent();
        UID=intent.getStringExtra("UID");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Message message=new Message();
                    friendId=httpUtil.httpFriendId(UID);
                    Log.d("cs",friendId);
                    JSONObject Fobject=new JSONObject(friendId);
                    num=Fobject.getInt("num");

                    FI=new String[num];
                    for (int i=0;i<num;i++){
                        FI[i]=Fobject.getString("friend"+i);
                    }

                    message.arg1=num;
                    String [] information=new String[num];

                    for (int j=0;j<num;j++){
                        String mation=httpUtil.httpInformation(FI[j]);
                        information[j]=mation;
                        Log.d("cs"+j,mation);

                    }
                    message.obj=information;
                    handler.sendMessage(message);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                int i=msg.arg1;
                if (i!=0){
                    name=new String[i];
                    signature=new String[i];
                    sex=new String[i];
                    birthday=new String[i];
                    profession=new String[i];
                    email=new String[i];
                    city=new String[i];
                    id=new String[i];
                }
                String[]tion=(String[]) msg.obj;
                Log.d("cs",tion[0]);
                for (int j=0;j<i;j++){
                    JSONObject object= null;
                    try {
                        object = new JSONObject(tion[j]);
                        id[j]=object.getString("Id");
                        name[j]=object.getString("name");
                        signature[j]=object.getString("signature");
                        sex[j]=object.getString("sex");
                        birthday[j]=object.getString("birthday");
                        profession[j]=object.getString("profession");
                        email[j]=object.getString("Email");
                        city[j]=object.getString("city");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                userList=new UserUtil().getUserInformation(friend.this,id,name,signature,sex,birthday,profession,email,city);

                FriendAdapter adapter=new FriendAdapter(userList,friend.this);
                friend_list.setAdapter(adapter);
            }
        };



        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1=new Intent(friend.this, Information.class);
                intent1.putExtra("UID",UID);
                intent1.putExtra("id",userList.get(position).getId());
                intent1.putExtra("name",userList.get(position).getName());
                intent1.putExtra("signature",userList.get(position).getSignature());
                intent1.putExtra("sex",userList.get(position).getSex());
                intent1.putExtra("birthday",userList.get(position).getBirthday());
                intent1.putExtra("profession",userList.get(position).getProfession());
                intent1.putExtra("email",userList.get(position).getEmail());
                intent1.putExtra("city",userList.get(position).getCity());
                startActivity(intent1);

            }
        });

        friend_hold.setIvmoreOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(friend.this,AddFriend.class);
                intent.putExtra("UID",UID);
                startActivity(intent);
            }
        });
        friend_hold.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

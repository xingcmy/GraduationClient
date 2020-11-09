package com.cn.graduationclient.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.message.FriendMessage;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.xingcmyAdapter.Lable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddFriendInformation extends Activity {

    HoldTitle information_hold;

    Lable information_id,information_name,information_signature,information_sex,information_birthday,information_profession,information_email,information_city;

    String id,name,signature,sex,birthday,profession,email,city;
    String UID;
    String[] FI;
    String friendId;

    Button add_friend,alter_information,sendMsg;

    HttpUtil httpUtil=new HttpUtil();

    int num;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_infromation);

        Intent intent=getIntent();
        UID=intent.getStringExtra("UID");
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        signature=intent.getStringExtra("signature");
        sex=intent.getStringExtra("sex");
        birthday=intent.getStringExtra("birthday");
        profession=intent.getStringExtra("profession");
        email=intent.getStringExtra("email");
        city=intent.getStringExtra("city");

        information_hold=findViewById(R.id.information_add_friend_hold);
        information_id=findViewById(R.id.information_add_friend_id);
        information_name=findViewById(R.id.information_add_friend_name);
        information_signature=findViewById(R.id.information_add_friend_signature);
        information_sex=findViewById(R.id.information_add_friend_sex);
        information_birthday=findViewById(R.id.information_add_friend_birthday);
        information_email=findViewById(R.id.information_add_friend_email);
        information_profession=findViewById(R.id.information_add_friend_profession);
        information_city=findViewById(R.id.information_add_friend_city);
        add_friend=findViewById(R.id.add_friend_button_add);
        alter_information=findViewById(R.id.add_friend_button_alter);
        sendMsg=findViewById(R.id.add_friend_button_setMsg);

        information_id.setTv_labletitle(id);
        information_name.setTv_labletitle(name);
        information_signature.setTv_labletitle(signature);
        information_sex.setTv_labletitle(sex);
        information_birthday.setTv_labletitle(birthday);
        information_city.setTv_labletitle(city);
        information_profession.setTv_labletitle(profession);
        information_email.setTv_labletitle(email);

        setButtonGoneOrVisible();

        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        alter_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddFriendInformation.this,AlterInformation.class);
                intent.putExtra("UID",UID);
                intent.putExtra("name",name);
                intent.putExtra("signature",signature);
                intent.putExtra("sex",sex);
                intent.putExtra("birthday",birthday);
                intent.putExtra("profession",profession);
                intent.putExtra("email",email);
                intent.putExtra("city",city);
                startActivity(intent);
                AddFriendInformation.this.finish();
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddFriendInformation.this, FriendMessage.class);
                intent.putExtra("UID",UID);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

        information_hold.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void setButtonGoneOrVisible(){
        if (UID.equals(id)){
            add_friend.setVisibility(View.GONE);
            alter_information.setVisibility(View.VISIBLE);
            sendMsg.setVisibility(View.VISIBLE);
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        friendId = httpUtil.httpFriendId(UID);
                        Log.d("cs", friendId);
                        JSONObject Fobject = new JSONObject(friendId);
                        num = Fobject.getInt("num");

                        FI = new String[num];
                        for (int i = 0; i < num; i++) {
                            FI[i] = Fobject.getString("friend" + i);
                        }
                        for (int j=0;j<num;j++){
                            if (id.equals(FI[j])){
                                add_friend.setVisibility(View.GONE);
                                alter_information.setVisibility(View.GONE);
                                sendMsg.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

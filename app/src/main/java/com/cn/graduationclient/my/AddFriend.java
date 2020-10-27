package com.cn.graduationclient.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddFriend extends Activity {
    EditText friendId;
    ImageButton add_friendImage_button;
    LinearLayout addFriendInformation,NoInformation;
    TextView add_friend_name,add_friend_signature,no_id;

    HttpUtil httpUtil=new HttpUtil();

    String id,name,signature,sex,birthday,profession,email,city;
    String UID;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String information=(String) msg.obj;
            try {
                JSONObject jsonObject=new JSONObject(information);
                String error=jsonObject.getString(StructureSystem.ERROR);
                if (error.equals(StructureSystem.SUCCESS)){
                    addFriendInformation.setVisibility(View.VISIBLE);
                    NoInformation.setVisibility(View.GONE);

                    id=jsonObject.getString("Id");
                    email=jsonObject.getString("Email");
                    name=jsonObject.getString("name");
                    signature=jsonObject.getString("signature");
                    sex=jsonObject.getString("sex");
                    birthday=jsonObject.getString("birthday");
                    profession=jsonObject.getString("profession");
                    city=jsonObject.getString("city");

                    add_friend_name.setText(name);
                    add_friend_signature.setText(signature);
                }else if (error.equals(StructureSystem.FAILED)){
                    addFriendInformation.setVisibility(View.GONE);
                    NoInformation.setVisibility(View.VISIBLE);
                    no_id.setText("没有当前用户ID账号请重新输入");
                    friendId.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        HoldTitle holdTitle=findViewById(R.id.add_friend_hold);

        friendId=findViewById(R.id.add_friend_id);

        add_friendImage_button=findViewById(R.id.image_button_found_friend);

        addFriendInformation=findViewById(R.id.add_friend_information);
        NoInformation=findViewById(R.id.noInformation);

        add_friend_name=findViewById(R.id.text_friends_name_add);
        add_friend_signature=findViewById(R.id.text_friends_signature_add);
        no_id=findViewById(R.id.text_no_Id);

        holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_friendImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friend_id=friendId.getText().toString();
                if (friend_id.equals("")||friend_id==null){
                    Toast.makeText(AddFriend.this,"请输入对方ID账号",Toast.LENGTH_LONG).show();
                }else {
                    try {
                        String id=httpUtil.httpInformation(friend_id);
                        Message message=new Message();
                        message.obj=id;
                        handler.sendMessage(message);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        addFriendInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AddFriend.this,AddFriendInformation.class);
                intent.putExtra("UID",UID);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("signature",signature);
                intent.putExtra("sex",sex);
                intent.putExtra("birthday",birthday);
                intent.putExtra("profession",profession);
                intent.putExtra("email",email);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });
    }
}

package com.cn.graduationclient.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddFriendMsg extends Activity {

    String UID;
    String id,name,sex,city;

    TextView text_name,text_sex,text_city;

    EditText msg;
    HttpUtil httpUtil=new HttpUtil();

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String friend=(String) msg.obj;
            try {
                JSONObject jsonObject=new JSONObject(friend);
                if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                    Toast.makeText(AddFriendMsg.this,"发送成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_friend_verify);

        Intent intent=getIntent();
        UID=intent.getStringExtra("UID");
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        sex=intent.getStringExtra("sex");
        city=intent.getStringExtra("city");

        HoldTitle holdTitle=findViewById(R.id.add_friend_hold_msg);
        text_name=findViewById(R.id.text_add_friend_name);
        text_city=findViewById(R.id.text_add_friend_city);
        text_sex=findViewById(R.id.text_add_friend_sex);
        msg=findViewById(R.id.edit_text_add_friend_msg);

        text_sex.setText(sex);
        text_city.setText(city);
        text_name.setText(name);
        msg.setText("我是...");
        holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        holdTitle.setTvmoreOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String friend=httpUtil.httpSendMsg(UID,id,msg.getText().toString(), TypeSystem.ADD_FRIEND,TypeSystem.WRITE);
                            Message message=new Message();
                            message.obj=friend;
                            handler.sendMessage(message);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
    }
}

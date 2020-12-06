package com.cn.graduationclient.message.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class random_welcome extends Activity {

    HttpUtil httpUtil=new HttpUtil();

    String UID;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String MessageRandom=(String) msg.obj;
            try {
                JSONObject jsonObject=new JSONObject(MessageRandom);
                String error=jsonObject.getString(StructureSystem.ERROR);
                if (error.equals(StructureSystem.SUCCESS)){
                    String id=jsonObject.getString("Random");
                    Intent intent=new Intent(random_welcome.this,RandomFriend.class);
                    intent.putExtra("UID",UID);
                    intent.putExtra("id",id);
                    startActivity(intent);
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
        setContentView(R.layout.welcome_random);

        Intent intent=getIntent();
        UID=intent.getStringExtra("UID");

        HoldTitle holdTitle=findViewById(R.id.welcome_random_hold);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new RandomThread().start();
            }
        }).start();


        holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpUtil.httpRandom(UID,null,null,3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                finish();
            }
        });
    }

    class RandomThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                while (true){
                    Thread.sleep(1000);
                    String error=httpUtil.httpRandom(UID,null,null,0);
                    Message message=new Message();
                    message.obj=error;
                    handler.sendMessage(message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

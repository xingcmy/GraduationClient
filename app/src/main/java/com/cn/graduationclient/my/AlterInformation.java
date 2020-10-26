package com.cn.graduationclient.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.xingcmyAdapter.EditLa;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.xingcmyAdapter.Lable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AlterInformation extends Activity {

    HoldTitle information_hold;

    Lable information_id,information_sex,information_birthday,information_profession,information_email,information_city;

    EditLa information_name,information_signature;
    String id,name,signature,sex,birthday,profession,email,city;


    HttpUtil httpUtil=new HttpUtil();

    Handler handler;

    String UID;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_information);

        Intent intent=getIntent();

        UID=intent.getStringExtra("UID");
        name=intent.getStringExtra("name");
        signature=intent.getStringExtra("signature");
        sex=intent.getStringExtra("sex");
        birthday=intent.getStringExtra("birthday");
        profession=intent.getStringExtra("profession");
        email=intent.getStringExtra("email");
        city=intent.getStringExtra("city");

        getViewId();

        information_name.setTv_labletitle(name);
        information_signature.setTv_labletitle(signature);
        information_sex.setTv_labletitle(sex);
        information_birthday.setTv_labletitle(birthday);
        information_email.setTv_labletitle(email);
        information_profession.setTv_labletitle(profession);
        information_city.setTv_labletitle(city);

        information_hold.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       information_hold.setTvmoreOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {

                   }
               }).start();
           }
       });
    }

    public void getViewId(){
        information_hold=findViewById(R.id.el_hold);

        information_id=findViewById(R.id.information_el_id);
        information_id.setTv_labletitle(UID);
        information_name=findViewById(R.id.information_el_name);
        information_signature=findViewById(R.id.information_el_signature);
        information_sex=findViewById(R.id.information_el_sex);
        information_birthday=findViewById(R.id.information_el_birthday);
        information_email=findViewById(R.id.information_el_email);
        information_profession=findViewById(R.id.information_el_profession);
        information_city=findViewById(R.id.information_el_city);

    }
}

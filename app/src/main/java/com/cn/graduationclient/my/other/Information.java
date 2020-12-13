package com.cn.graduationclient.my.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.db.HeadDbHelper;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.message.FriendMessage;
import com.cn.graduationclient.my.AlterInformation;
import com.cn.graduationclient.tool.MsgTool;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.xingcmyAdapter.Lable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Information extends Activity {
    HoldTitle information_hold;

    Lable information_id,information_name,information_signature,information_sex,information_birthday,information_profession,information_email,information_city;

    String id,name,signature,sex,birthday,profession,email,city;

    Button alter_information,send_message;
    ImageView imageView;

    HttpUtil httpUtil=new HttpUtil();

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    String UID;

    HeadDbHelper headDbHelper;
    SQLiteDatabase sqLiteDatabase;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_information);

        headDbHelper=new HeadDbHelper(Information.this);
        sqLiteDatabase=headDbHelper.getReadableDatabase();

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

        getViewId();
        Listener();

        information_id.setTv_labletitle(id);
        information_name.setTv_labletitle(name);
        information_signature.setTv_labletitle(signature);
        information_sex.setTv_labletitle(sex);
        information_birthday.setTv_labletitle(birthday);
        information_profession.setTv_labletitle(profession);
        information_email.setTv_labletitle(email);
        information_city.setTv_labletitle(city);

        Cursor cursor=sqLiteDatabase.rawQuery("select * from head where uid='"+id+"'",null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                Bitmap bitmap=new MsgTool().decodeSampleBitmap(imageView,cursor.getString(1));
                imageView.setImageBitmap(bitmap);
            }
        }


    }

    public void getViewId(){
        information_hold=findViewById(R.id.information_other_hold);

        imageView=findViewById(R.id.information_other_head);
        information_id=findViewById(R.id.information_lable_other_id);
        information_name=findViewById(R.id.information_lable_other_name);
        information_signature=findViewById(R.id.information_lable_other_signature);
        information_sex=findViewById(R.id.information_lable_other_sex);
        information_birthday=findViewById(R.id.information_lable_other_birthday);
        information_email=findViewById(R.id.information_lable_other_email);
        information_email.setTv_labletitle(email);
        information_profession=findViewById(R.id.information_lable_other_profession);
        information_city=findViewById(R.id.information_lable_other_city);

        send_message=findViewById(R.id.send_other_message);
    }

    public void Listener(){

        information_hold.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Information.this, FriendMessage.class);
                intent.putExtra("UID",UID);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });


    }
}

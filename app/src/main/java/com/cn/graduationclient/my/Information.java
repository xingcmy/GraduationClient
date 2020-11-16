package com.cn.graduationclient.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.cn.graduationclient.R;
import com.cn.graduationclient.db.HeadDbHelper;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.login.LoginActivity;
import com.cn.graduationclient.tool.MsgTool;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.xingcmyAdapter.Lable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Information extends Activity implements View.OnClickListener {

    HoldTitle information_hold;

    Lable information_id,information_name,information_signature,information_sex,information_birthday,information_profession,information_email,information_city;

    String id,name,signature,sex,birthday,profession,email,city;

    Button alter_information,send_message;
    ImageView imageView;

    HttpUtil httpUtil=new HttpUtil();

    Handler handler;

    String UID;

    HeadDbHelper headDbHelper;
    SQLiteDatabase sqLiteDatabase;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);

        headDbHelper=new HeadDbHelper(Information.this);
        sqLiteDatabase=headDbHelper.getReadableDatabase();

        Intent intent=getIntent();
        UID=intent.getStringExtra("UID");
        email=intent.getStringExtra("email");
        getViewId();
        Listener();

        Cursor cursor=sqLiteDatabase.rawQuery("select * from head where uid='"+UID+"'",null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                Bitmap bitmap=new MsgTool().decodeSampleBitmap(imageView,cursor.getString(1));
                imageView.setImageBitmap(bitmap);
            }
        }

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String mation=(String) msg.obj;
                if (mation!=null){
                    try {
                        JSONObject jsonObject=new JSONObject(mation);

                        name=jsonObject.getString("name");
                        signature=jsonObject.getString("signature");
                        sex=jsonObject.getString("sex");
                        birthday=jsonObject.getString("birthday");
                        profession=jsonObject.getString("profession");
                        city=jsonObject.getString("city");

                        information_name.setTv_labletitle(name);
                        information_signature.setTv_labletitle(signature);
                        information_sex.setTv_labletitle(sex);
                        information_birthday.setTv_labletitle(birthday);
                        information_profession.setTv_labletitle(profession);
                        information_city.setTv_labletitle(city);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    String msg=httpUtil.httpInformation(UID);
                    String head=httpUtil.httpGetHead(UID);
                    JSONObject jsonObject=new JSONObject(head);
                    String path=jsonObject.getString("path");
                    String filePath="";
                    byte[] bytes=new MsgTool().StringToByte(path);

                    filePath=new MsgTool().getFileByBytes(bytes, Information.this.getExternalFilesDir(null).getPath(),UID+".jpg");

                    Cursor cursor=sqLiteDatabase.rawQuery("select * from head where uid='"+UID+"'",null);
                    if (cursor.getCount()<=0){
                        sqLiteDatabase.execSQL("insert into head values('"+UID+"','"+filePath+"')");
                    }else if (cursor.getCount()>0){
                        sqLiteDatabase.execSQL("update head set msg='"+filePath+"' where uid='"+UID+"'");
                    }
                    Message message=handler.obtainMessage();
                    //message.what=0x01;

                    message.obj=msg;
                    handler.sendMessage(message);



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getViewId(){
        information_hold=findViewById(R.id.information_hold);

        imageView=findViewById(R.id.information_my_head);
        information_id=findViewById(R.id.information_lable_id);
        information_id.setTv_labletitle(UID);
        information_name=findViewById(R.id.information_lable_name);
        information_signature=findViewById(R.id.information_lable_signature);
        information_sex=findViewById(R.id.information_lable_sex);
        information_birthday=findViewById(R.id.information_lable_birthday);
        information_email=findViewById(R.id.information_lable_email);
        information_email.setTv_labletitle(email);
        information_profession=findViewById(R.id.information_lable_profession);
        information_city=findViewById(R.id.information_lable_city);

        alter_information=findViewById(R.id.alter_information);
        send_message=findViewById(R.id.send_message);
    }

    public void Listener(){
        information_hold.setIvbackOnClickListener(Information.this);

        information_hold.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alter_information.setOnClickListener(Information.this);
        send_message.setOnClickListener(Information.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alter_information:
                Intent intent=new Intent(Information.this,AlterInformation.class);
                intent.putExtra("UID",UID);
                intent.putExtra("name",name);
                intent.putExtra("signature",signature);
                intent.putExtra("sex",sex);
                intent.putExtra("birthday",birthday);
                intent.putExtra("profession",profession);
                intent.putExtra("email",email);
                intent.putExtra("city",city);
                startActivity(intent);
                Information.this.finish();
                break;
        }
    }
}

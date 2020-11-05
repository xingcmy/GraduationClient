package com.cn.graduationclient.login.otherLogin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.homepage.HomePageActivity;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email extends Activity implements View.OnClickListener {

    EditText editText_email, editText_yan;
    Button button_yan, button_login;
    HttpUtil httpUtil = new HttpUtil();

    String email_yan;
    String UID,Phone,Email;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    Toast.makeText(Email.this,"当前邮箱尚未注册",Toast.LENGTH_SHORT).show();
                    break;
                case 0x02:
                    try {
                        JSONObject jsonObject=new JSONObject((String) msg.obj);
                        UID=jsonObject.getString(StructureSystem.ID);
                        Phone=jsonObject.getString(StructureSystem.PHONE);
                        Email=jsonObject.getString(StructureSystem.EMAIL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email);

        HoldTitle login_email_hold = findViewById(R.id.login_email_hold);
        editText_email = findViewById(R.id.login_edit_email);
        editText_yan = findViewById(R.id.login_email_edit_yan);
        button_yan = findViewById(R.id.login_email_button_yan);
        button_login = findViewById(R.id.login_email_button_login);

        button_yan.setOnClickListener(this);
        button_login.setOnClickListener(this);
        login_email_hold.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_email_button_yan:
                yan();
                break;
            case R.id.login_email_button_login:
                loginE();
                break;
        }
    }

    public void yan() {
        String emailId = editText_email.getText().toString();
        if (emailId.length() <= 0) {
            Toast.makeText(Email.this, "请输入邮箱", Toast.LENGTH_SHORT).show();

        } else {
            String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p;
            Matcher m;
            p = Pattern.compile(regEx1);
            m = p.matcher(emailId);
            if (m.matches()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String j_email = httpUtil.httpEmail(emailId, StructureSystem.LOGIN);
                            Message message=new Message();
                            JSONObject jsonObject = new JSONObject(j_email);
                            if (jsonObject.getString("error").equals("success")) {
                                email_yan=jsonObject.getString("email_yan");
                                String IdPhoneEmail=httpUtil.httpGetIdPhoneEmail(emailId);
                                message.what=0x02;
                                message.obj=IdPhoneEmail;
                            } else if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.FAILED)){
                                message.what=0x01;
                            }
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } else {
                Toast.makeText(Email.this, "邮箱账号错误", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void loginE(){
        //String emailId = editText_email.getText().toString();
        String emailYan=editText_yan.getText().toString();
        if (email_yan!=null){
            if (emailYan.length()<=0){
                Toast.makeText(Email.this,"请输入验证码",Toast.LENGTH_SHORT).show();
            }else if (emailYan.equals(email_yan)){
                Intent intent=new Intent(com.cn.graduationclient.login.otherLogin.Email.this, HomePageActivity.class);
                intent.putExtra(StructureSystem.UID,UID);
                intent.putExtra("phone",Phone);
                intent.putExtra("email",Email);

                startActivity(intent);
                com.cn.graduationclient.login.otherLogin.Email.this.finish();
            }
        }else {
            Toast.makeText(Email.this,"请先获取验证码",Toast.LENGTH_SHORT).show();
        }
    }

}

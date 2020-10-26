package com.cn.graduationclient.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.cn.graduationclient.R;
import com.cn.graduationclient.http.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email extends Activity implements View.OnClickListener {

    EditText et_email,et_eamilyan,ed_register_password,ed_register_password_too;
    TextView tv_register_phone;
    Button button_email,button_register;

    HttpUtil httpUtil=new HttpUtil();

    String email_yan="xingcmy";
    String phone,password,email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email);

        getViewId();

        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        tv_register_phone.setText("当前注册手机号"+phone);

        Listener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_email:
                setEmail();
                break;
            case R.id.b_register:
                setPassword();
                break;
        }
    }

    public void getViewId(){
        et_email=findViewById(R.id.et_email);
        et_eamilyan=findViewById(R.id.et_emailyan);
        ed_register_password=findViewById(R.id.ed_register_password);
        ed_register_password_too=findViewById(R.id.ed_register_password_too);

        tv_register_phone=findViewById(R.id.tv_register_phone);

        button_email=findViewById(R.id.b_email);
        button_register=findViewById(R.id.b_register);
    }

    public void Listener(){
        button_email.setOnClickListener(this);
        button_register.setOnClickListener(this);
    }

    public void setEmail(){
        if (et_email.getText().toString().length()<=0){
            Toast.makeText(Email.this,"请输入邮箱账号",Toast.LENGTH_LONG).show();
        }else {
            String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p;
            Matcher m;
            p = Pattern.compile(regEx1);
            email=et_email.getText().toString();
            m = p.matcher(email);
            if (m.matches()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String j_email=httpUtil.httpEmail(et_email.getText().toString());
                            JSONObject jsonObject=new JSONObject(j_email);
                            if (jsonObject.getString("error").equals("success")){
                                email_yan=jsonObject.getString("email_yan");
                                Log.d("c",email_yan);
                            }else {
                                Log.d("c",jsonObject.getString("error"));
                                //Toast.makeText(Email.this,"该邮箱已被注册",Toast.LENGTH_LONG).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }else {
                Toast.makeText(Email.this,"邮箱账号错误",Toast.LENGTH_LONG).show();
            }

        }

    }

    public void setPassword(){
        if (et_email.getText().toString().length()<=0){
            Toast.makeText(Email.this,"请输入邮箱账号",Toast.LENGTH_LONG).show();
        }else if (et_eamilyan.getText().toString().length()<=0){
            Toast.makeText(Email.this,"请输入验证码",Toast.LENGTH_LONG).show();
        }else if (et_eamilyan.getText().toString().equals(email_yan)){
            if (ed_register_password.getText().toString().length()<=0||ed_register_password_too.getText().toString().length()<=0){
                Toast.makeText(Email.this,"请输入密码",Toast.LENGTH_LONG).show();
            }else if (ed_register_password_too.getText().toString().equals(ed_register_password.getText().toString())){
                password=ed_register_password.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String j_id=httpUtil.httpRegister(phone,password,email);

                            if (j_id!=null){
                                JSONObject jsonObject=new JSONObject(j_id);
                                String id=jsonObject.getString("Id");
                                View inflate = getLayoutInflater().inflate(R.layout.register_success, null);
                                final AlertDialog.Builder ab = new AlertDialog.Builder(Email.this);
                                AlertDialog out = ab.create();
                                out.setView(inflate);

                                TextView my_id=inflate.findViewById(R.id.text_my_id);
                                Button button=inflate.findViewById(R.id.my_button);

                                my_id.setText(id);

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else Toast.makeText(Email.this,"密码不一致",Toast.LENGTH_LONG).show();
        }else Toast.makeText(Email.this,"验证码错误",Toast.LENGTH_LONG).show();
    }

}

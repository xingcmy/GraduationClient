package com.cn.graduationclient.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;

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

    CheckBox register_password_checkbox,register_password_checkbox_too;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    Toast.makeText(Email.this,"注册成功",Toast.LENGTH_SHORT).show();
                    View inflate = getLayoutInflater().inflate(R.layout.register_success, null);
                    final AlertDialog.Builder ab = new AlertDialog.Builder(Email.this);
                    AlertDialog out = ab.create();
                    out.setView(inflate);
                    out.show();

                    TextView my_id=inflate.findViewById(R.id.text_my_id);
                    Button button=inflate.findViewById(R.id.my_button);

                    my_id.setText((String)msg.obj);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Email.this.finish();
                            out.dismiss();
                        }
                    });
                    break;
                case 0x02:
                    Toast.makeText(Email.this,"当前邮箱已被注册",Toast.LENGTH_LONG);
                    break;
                case 0x03:
                    Toast.makeText(Email.this,"验证码发送成功",Toast.LENGTH_LONG);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email);

        getViewId();
        HoldTitle holdTitle=findViewById(R.id.holdt_register);
        holdTitle.setIvmoreOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email.this.finish();
            }
        });

        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        tv_register_phone.setText("当前注册手机号"+phone);

        Listener();

        register_password_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckedChanged(register_password_checkbox.isChecked(),R.id.register_password_checkbox);
            }
        });

        register_password_checkbox_too.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckedChanged(register_password_checkbox_too.isChecked(),R.id.register_password_checkbox_too);
            }
        });
    }

    public void onCheckedChanged(boolean isChecked, int v) {
        switch (v){
            case R.id.register_password_checkbox:
                ed_register_password.setTransformationMethod(isChecked? PasswordTransformationMethod.getInstance(): HideReturnsTransformationMethod.getInstance());
                ed_register_password.setSelection(ed_register_password.length());
                if (isChecked)
                    register_password_checkbox.setButtonDrawable(R.drawable.visibility_off);
                else register_password_checkbox.setButtonDrawable(R.drawable.visibility_on);
                break;
            case R.id.register_password_checkbox_too:
                ed_register_password_too.setTransformationMethod(isChecked?PasswordTransformationMethod.getInstance():HideReturnsTransformationMethod.getInstance());
                ed_register_password_too.setSelection(ed_register_password_too.length());
                if (isChecked){
                    register_password_checkbox_too.setButtonDrawable(R.drawable.visibility_off);
                }else {
                    register_password_checkbox_too.setButtonDrawable(R.drawable.visibility_on);
                }
        }

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
        register_password_checkbox=findViewById(R.id.register_password_checkbox);
        register_password_checkbox_too=findViewById(R.id.register_password_checkbox_too);
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
                            Message message=new Message();
                            String j_email=httpUtil.httpEmail(et_email.getText().toString(), StructureSystem.REGISTER);
                            JSONObject jsonObject=new JSONObject(j_email);
                            if (jsonObject.getString("error").equals("success")){
                                email_yan=jsonObject.getString("email_yan");
                                Log.d("c",email_yan);
                                message.what=0x03;
                            }else {
                                Log.d("c",jsonObject.getString("error"));
                                message.what=0x02;
                                //Toast.makeText(Email.this,"该邮箱已被注册",Toast.LENGTH_LONG).show();
                            }
                            handler.sendMessage(message);

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
                                Message message=new Message();
                                message.obj=id;
                                message.what=0x01;
                                handler.sendMessage(message);
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

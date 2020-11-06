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
import com.cn.graduationclient.login.Register;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Phone extends Activity implements View.OnClickListener {

    EditText editText_phone, editText_yan;
    Button button_yan, button_login;
    HttpUtil httpUtil = new HttpUtil();

    String phone_yan,phoneId;
    String UID,Phone,Email;
    boolean flog=false;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x02:
                    Toast.makeText(com.cn.graduationclient.login.otherLogin.Phone.this,"当前手机号未注册",Toast.LENGTH_SHORT).show();
                    break;
                case 0x01:
                    SMSSDK.getVerificationCode("86",phoneId);
                    editText_yan.requestFocus();
                    flog=true;
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
        setContentView(R.layout.login_phone);

        HoldTitle holdTitle=findViewById(R.id.login_phone_hold);
        editText_phone = findViewById(R.id.login_edit_phone);
        editText_yan = findViewById(R.id.login_phone_edit_yan);
        button_yan = findViewById(R.id.login_phone_button_yan);
        button_login = findViewById(R.id.login_phone_button_login);

        button_yan.setOnClickListener(this);
        button_login.setOnClickListener(this);
        holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EventHandler eventHandler=new EventHandler(){
            public void afterEvent(int event, final int result, Object data){
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
//                handler.sendMessage(msg);
                //handler.sendMessage(msg);
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //提交验证
                            if (flog)
                                if (result == SMSSDK.RESULT_COMPLETE) { //判断 手机号与验证码
                                    Intent intent=new Intent(com.cn.graduationclient.login.otherLogin.Phone.this, HomePageActivity.class);
                                    intent.putExtra(StructureSystem.UID,UID);
                                    intent.putExtra("phone",Phone);
                                    intent.putExtra("email",Email);

                                    startActivity(intent);
                                    com.cn.graduationclient.login.otherLogin.Phone.this.finish();

                                }
                                else Toast.makeText(Phone.this,"验证码错误",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_phone_button_yan:
                if (yan()){
                    //submitPrivacyGrantResult(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String j_phone=httpUtil.httpPhone(phoneId,StructureSystem.LOGIN);
                                Message message=new Message();
                                JSONObject jsonObject=new JSONObject(j_phone);
                                if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                                    String IdPhoneEmail=httpUtil.httpGetIdPhoneEmail(phoneId);
                                    message.obj=IdPhoneEmail;
                                    message.what=0x01;
                                }else if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.FAILED)){
                                    message.what=0x02;
                                }
                              handler.sendMessage(message);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
                break;
            case R.id.login_phone_button_login:
                if (loginP())
                    //submitPrivacyGrantResult(true);
                    SMSSDK.submitVerificationCode("86",phoneId,phone_yan);
                break;
        }
    }

    public boolean yan(){
        phoneId=editText_phone.getText().toString();
        if (phoneId.length()<=0){
            Toast.makeText(com.cn.graduationclient.login.otherLogin.Phone.this,"请输入手机号",Toast.LENGTH_SHORT).show();
            editText_phone.requestFocus();
            return false;
        }else if (phoneId.length()!=11){
            Toast.makeText(com.cn.graduationclient.login.otherLogin.Phone.this,"手机号位数错误",Toast.LENGTH_SHORT).show();
            editText_phone.requestFocus();
            return false;
        }else {
            String num="[1][358]\\d{9}";
            if (phoneId.matches(num)){
                return true;
            }else {
                Toast.makeText(com.cn.graduationclient.login.otherLogin.Phone.this,"手机号错误",Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
    public boolean loginP(){
        yan();
        phone_yan=editText_yan.getText().toString();
        if (phone_yan.length()<=0){
            Toast.makeText(com.cn.graduationclient.login.otherLogin.Phone.this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return false;
        }else if (phone_yan.length()!=6){
            Toast.makeText(com.cn.graduationclient.login.otherLogin.Phone.this,"验证码位数错误",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }
}

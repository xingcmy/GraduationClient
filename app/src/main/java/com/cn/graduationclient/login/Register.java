package com.cn.graduationclient.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.mob.MobSDK;
import com.mob.OperationCallback;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class Register extends Activity implements View.OnClickListener {


    private static final String TAG = "Register";

    Handler handler,SQLhandler;
    Button button_local;
    EditText et_phone,et_yan;
    boolean flag=true,flog=false;
    Button b_yan,b_register;

    int success=0;
    HttpUtil httpUtil=new HttpUtil();
    String phone_number,yan_number;

    CheckBox checkBox;

    Socket socket;

    boolean yes_no=true;





    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        setB_logout();


        EventHandler eventHandler=new EventHandler(){
            public void afterEvent(int event, final int result, Object data){
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                msg.what=0x01;
//                handler.sendMessage(msg);
                handler.sendMessage(msg);
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //提交验证
                            if (flog)
                                if (result == SMSSDK.RESULT_COMPLETE) { //判断 手机号与验证码

                                    Log.d("s",result+"");
                                    Intent intent=new Intent(Register.this,Email.class);
                                    intent.putExtra("phone",et_phone.getText().toString());
                                    startActivity(intent);
                                    Register.this.finish();
                                }
                                else Toast.makeText(Register.this,"验证码错误",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);



        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){

                super.handleMessage(msg);
                switch (msg.what){
                    case 0x01:
                        int event=msg.arg1;
                        int result=msg.arg2;
                        Object data=msg.obj;
                        if (event== SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            if (result== SMSSDK.RESULT_COMPLETE){
                                boolean smart= (boolean) data;
                                if (smart){
                                    et_phone.requestFocus();
                                    return;
                                }
                            }
                        }
                        if (result== SMSSDK.RESULT_COMPLETE){
                            if (event== SMSSDK.EVENT_GET_VERIFICATION_CODE){

                            }
                        }
                        else {
                            if (flag){
                                b_yan.setVisibility(View.VISIBLE);
                                et_phone.requestFocus();
                            }
                            else {

                            }
                        }
                        break;
                    case 0x02:
                        SMSSDK.getVerificationCode("86",phone_number);
                        et_yan.requestFocus();
                        flog=true;
                        break;
                    case 0x03:
                        Toast.makeText(Register.this,"当前手机号已被注册",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

    }




    @SuppressLint("SetTextI18n")
    public void setB_logout() {



        et_phone=findViewById(R.id.et_phone);
        b_yan=findViewById(R.id.b_registeryan);
        et_yan=findViewById(R.id.et_registeryan);
        b_register=findViewById(R.id.b_submit);
        checkBox=findViewById(R.id.checkBox);

        TextView yinsi=findViewById(R.id.tv_yinsi);
        //if (checkBox.isChecked()==true)

        et_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (success==0){
                    et_phone.setText("");
                    success=1;
                }

            }
        });


        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox.isChecked()==true){
                submitPrivacyGrantResult(yes_no);
                //MobSDK.submitPolicyGrantResult(yes_no,null);
                if (judyan())
                    //submitPrivacyGrantResult(true);
                    SMSSDK.submitVerificationCode("86",phone_number,yan_number);
                flag=false;
              }else {
                   Toast.makeText(Register.this,"您尚未同意服务条款",Toast.LENGTH_LONG).show();
               }
            }
        });
        b_yan.setOnClickListener(this);

        yinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View inflate = LayoutInflater.from(Register.this).inflate(R.layout.dialog_xieyi_yinsi_style, null);
                TextView tv_title =inflate.findViewById(R.id.tv_title);
                tv_title.setText("隐私政策");
                TextView tv_content = (TextView) inflate.findViewById(R.id.tv_content);
                tv_content.setText(R.string.Privacypolicy);
                Button agree=inflate.findViewById(R.id.agree_button);
                Button decline=inflate.findViewById(R.id.decline_button);

                final Dialog dialog = new AlertDialog
                        .Builder(Register.this)
                        .setView(inflate)
                        .show();
                final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = 800;
                params.height = 1200;
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        yes_no=true;
                        dialog.dismiss();
                    }
                });
                decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        yes_no=false;
                        dialog.dismiss();
                    }
                });
            }
        });








        HoldTitle holdTitle1=findViewById(R.id.holdt_register);

        holdTitle1.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_registeryan:
                if (judPhone()){
                    //submitPrivacyGrantResult(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String j_phone=httpUtil.httpPhone(phone_number, StructureSystem.REGISTER);
                                Message message=new Message();
                                JSONObject jsonObject=new JSONObject(j_phone);
                                if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                                    message.what=0x02;
                                }else if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.FAILED)){
                                    message.what=0x03;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
        }
    }
    private boolean judyan() {
        judPhone();
        if (TextUtils.isEmpty(et_yan.getText().toString().trim())){
            Toast.makeText(Register.this,"请输入验证码",Toast.LENGTH_LONG).show();
            et_yan.requestFocus();
            return false;
        }
        else if (et_yan.getText().toString().trim().length()!=6){
            Toast.makeText(Register.this,"验证码位数错误",Toast.LENGTH_LONG).show();
            et_yan.requestFocus();
            return false;
        }
        else {
            yan_number=et_yan.getText().toString().trim();
            return true;
        }
    }

    private boolean judPhone() {
        if (TextUtils.isEmpty(et_phone.getText().toString().trim())){
            Toast.makeText(Register.this,"请输入您的手机号",Toast.LENGTH_LONG).show();
            et_phone.requestFocus();
            return false;
        }
        else if (et_phone.getText().toString().trim().length()!=11){
            Toast.makeText(Register.this,"您的手机号位数不正确",Toast.LENGTH_LONG).show();
            et_phone.requestFocus();
            return false;
        }
        else {
            phone_number=et_phone.getText().toString().trim();
            String nu="[1][358]\\d{9}";
            if (phone_number.matches(nu))
                return true;
            else {
                Toast.makeText(Register.this,"手机号不正确",Toast.LENGTH_LONG).show();
                return false;
            }

        }
    }

    private void submitPrivacyGrantResult(boolean granted) {
        MobSDK.submitPolicyGrantResult(granted, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                Log.d(TAG, "隐私协议授权结果提交：成功");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "隐私协议授权结果提交：失败");
            }
        });
    }
}

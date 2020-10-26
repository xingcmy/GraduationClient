package com.cn.graduationclient.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Forget extends Activity implements View.OnClickListener {
    HoldTitle hol_forget;
    EditText edforget_phone,edforget_yan,edforget_password;
    Button forget_yan,forget_finish;
    Handler handler,SQLhandler;
    boolean flag=true,flog=false;
    String phone_number,yan_number;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget);

        forget_getId();
        hol_forget.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EventHandler eventHandler=new EventHandler(){
            public void afterEvent(int event, final int result, Object data){
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //提交验证
                            if (flog)
                                if (result == SMSSDK.RESULT_COMPLETE) { //判断 手机号与验证码

                                    //DBUtils.inuserupdata(edforget_phone.getText().toString(),edforget_password.getText().toString());


                                } else
                                    Toast.makeText(Forget.this, "验证码错误", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);

        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int event=msg.arg1;
                int result=msg.arg2;
                Object data=msg.obj;
                if (event== SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    if (result== SMSSDK.RESULT_COMPLETE){
                        boolean smart= (boolean) data;
                        if (smart){
                            edforget_phone.requestFocus();
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
                        forget_yan.setVisibility(View.VISIBLE);
                        edforget_phone.requestFocus();
                    }
                    else {

                    }
                }
            }
        };
        SQLhandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0x01:
                        Toast.makeText(Forget.this, "修改成功", Toast.LENGTH_LONG).show();
                        //String s = (String) msg.obj;
                        //et_user.setText(s);
                        break;
                    case 0x02:
                        Toast.makeText(Forget.this, "修改失败", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forget_yan:
                setForget_yan();
                break;
            case R.id.forget_finish:
                setForget_finish();
                break;

        }

    }


    public void forget_getId(){
        hol_forget=findViewById(R.id.forget_hold);
        edforget_password=findViewById(R.id.edforget_password);
        edforget_phone=findViewById(R.id.edforget_phone);
        edforget_yan=findViewById(R.id.edforget_yan);
        forget_finish=findViewById(R.id.forget_finish);
        forget_yan=findViewById(R.id.forget_yan);

        forget_yan.setOnClickListener(Forget.this);
        forget_finish.setOnClickListener(Forget.this);
    }

    public void setForget_finish() {
        if (judyan())
            SMSSDK.submitVerificationCode("86",phone_number,yan_number);
        flag=false;


    }

    public void setForget_yan() {
        if (judPhone()){
            SMSSDK.getVerificationCode("86",phone_number);
            edforget_yan.requestFocus();
            flog=true;
        }

    }
    private boolean judPhone() {
        if (TextUtils.isEmpty(edforget_phone.getText().toString().trim())){
            Toast.makeText(Forget.this,"请输入您的手机号",Toast.LENGTH_LONG).show();
            edforget_phone.requestFocus();
            return false;
        }
        else if (edforget_phone.getText().toString().trim().length()!=11){
            Toast.makeText(Forget.this,"您的手机号位数不正确",Toast.LENGTH_LONG).show();
            edforget_phone.requestFocus();
            return false;
        }
        else {
            phone_number=edforget_phone.getText().toString().trim();
            String nu="[1][358]\\d{9}";
            if (phone_number.matches(nu))
                return true;
            else {
                Toast.makeText(Forget.this,"手机号不正确",Toast.LENGTH_LONG).show();
                return false;
            }

        }
    }
    private boolean judyan() {
        judPhone();
        if (TextUtils.isEmpty(edforget_yan.getText().toString().trim())){
            Toast.makeText(Forget.this,"请输入验证码",Toast.LENGTH_LONG).show();
            edforget_yan.requestFocus();
            return false;
        }
        else if (edforget_yan.getText().toString().trim().length()!=6){
            Toast.makeText(Forget.this,"验证码位数错误",Toast.LENGTH_LONG).show();
            edforget_yan.requestFocus();
            return false;
        }
        else {
            yan_number=edforget_yan.getText().toString().trim();
            return true;
        }
    }
    public void intouser(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean into=true;
                //boolean into=DBUtils.inuserupdata(edforget_phone.getText().toString(),edforget_password.getText().toString());

                Message message = SQLhandler.obtainMessage();
                if (into){
                    message.what=0x01;
                }else message.what=0x02;

                SQLhandler.sendMessage(message);
            }
        }).start();
    }
}

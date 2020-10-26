package com.cn.graduationclient.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cn.graduationclient.MainActivity;
import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.homepage.HomePageActivity;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.login.otherLogin.Email;
import com.cn.graduationclient.login.otherLogin.Phone;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG ="XINGCMY";

    EditText login_edit_password,login_edit_user;
    CheckBox login_password_checkbox;
    Button login_button;

    TextView login_text_forget,login_text_register;

    String my_id;

    HttpUtil httpUtil=new HttpUtil();

    @SuppressLint("HandlerLeak")
    Handler handler;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        login_edit_password=findViewById(R.id.login_edit_password);
        login_edit_user=findViewById(R.id.login_edit_user);
        login_password_checkbox=findViewById(R.id.login_password_checkbox);
        login_button=findViewById(R.id.login_layout_button);
        login_text_forget=findViewById(R.id.login_text_forget);
        login_text_register=findViewById(R.id.login_text_register);

        login_text_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, Forget.class);
                startActivity(intent);
            }
        });
        login_text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,Register.class);

                startActivity(intent);
            }
        });
        //onCheckedChanged(true);
        login_password_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckedChanged(login_password_checkbox.isChecked());
            }
        });




        login_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if (login_edit_user.getText().toString().length()<=0||login_edit_password.getText().toString().length()<=0){
                    if (login_edit_user.getText().toString().length()<=0&&login_edit_password.getText().toString().length()<=0){
                        Toast.makeText(LoginActivity.this,"请输入用户名和密码",Toast.LENGTH_LONG).show();
                    } else if (login_edit_user.getText().toString().length()<=0){
                        Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
                    }else if (login_edit_password.getText().toString().length()<=0){
                        Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                    }
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                               String s= httpUtil.httpLogin(login_edit_user.getText().toString(),login_edit_password.getText().toString());
                               JSONObject jsonObject=new JSONObject(s);

                               Log.d("cs", String.valueOf(jsonObject));
                               String result=jsonObject.getString(StructureSystem.ERROR);
                               Log.d(TAG+"登录",result);
                               if (result.equals(StructureSystem.SUCCESS)){
                                   String id=login_edit_user.getText().toString();
                                   String IPE=httpUtil.httpGetIdPhoneEmail(id);

                                   //Log.d("cs",String.format("content://%s.cim.provider", LoginActivity.this.getPackageName()));

                                   JSONObject object=new JSONObject(IPE);
                                   String Id=object.getString(StructureSystem.ID);
                                   String Phone=object.getString(StructureSystem.PHONE);
                                   String Email=object.getString(StructureSystem.EMAIL);

                                   Intent intent=new Intent(LoginActivity.this, HomePageActivity.class);
                                   intent.putExtra(StructureSystem.UID,Id);
                                   intent.putExtra("phone",Phone);
                                   intent.putExtra("email",Email);

                                   startActivity(intent);
                                   LoginActivity.this.finish();
                               }else {
                                   Toast.makeText(LoginActivity.this,"用户名/密码错误",Toast.LENGTH_LONG).show();
                               }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
        });





    }



    public void onCheckedChanged(boolean isChecked) {
        login_edit_password.setTransformationMethod(isChecked? PasswordTransformationMethod.getInstance(): HideReturnsTransformationMethod.getInstance());
        login_edit_password.setSelection(login_edit_password.length());
        if (isChecked)
            login_password_checkbox.setButtonDrawable(R.drawable.visibility_off);
        else login_password_checkbox.setButtonDrawable(R.drawable.visibility_on);
    }

    public void In(){
        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("my_id",my_id);
        Log.d("id",my_id);
        startActivity(intent);
    }

    public void LoginEmail(View view) {
        Intent intent=new Intent(this, Email.class);
        startActivity(intent);
    }

    public void LoginPhone(View view) {
        Intent intent=new Intent(this, Phone.class);
        startActivity(intent);
    }
}
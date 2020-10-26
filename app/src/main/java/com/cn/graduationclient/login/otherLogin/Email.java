package com.cn.graduationclient.login.otherLogin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;

public class Email extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email);
        HoldTitle login_email_hold=findViewById(R.id.login_email_hold);
        login_email_hold.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

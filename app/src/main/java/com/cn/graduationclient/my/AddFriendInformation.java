package com.cn.graduationclient.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.xingcmyAdapter.Lable;

public class AddFriendInformation extends Activity {

    HoldTitle information_hold;

    Lable information_id,information_name,information_signature,information_sex,information_birthday,information_profession,information_email,information_city;

    String id,name,signature,sex,birthday,profession,email,city;
    String UID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_infromation);

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

        information_hold=findViewById(R.id.information_add_friend_hold);
        information_id=findViewById(R.id.information_add_friend_id);
        information_name=findViewById(R.id.information_add_friend_name);
        information_signature=findViewById(R.id.information_add_friend_signature);
        information_sex=findViewById(R.id.information_add_friend_sex);
        information_birthday=findViewById(R.id.information_add_friend_birthday);
        information_email=findViewById(R.id.information_add_friend_email);
        information_profession=findViewById(R.id.information_add_friend_profession);
        information_city=findViewById(R.id.information_add_friend_city);
        Button add_friend=findViewById(R.id.add_friend_button);

        information_id.setTv_labletitle(id);
        information_name.setTv_labletitle(name);
        information_signature.setTv_labletitle(signature);
        information_sex.setTv_labletitle(sex);
        information_birthday.setTv_labletitle(birthday);
        information_city.setTv_labletitle(city);
        information_profession.setTv_labletitle(profession);
        information_email.setTv_labletitle(email);

        information_hold.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

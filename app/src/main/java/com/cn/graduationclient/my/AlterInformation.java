package com.cn.graduationclient.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.cn.graduationclient.R;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.xingcmyAdapter.EditLa;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.xingcmyAdapter.Lable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlterInformation extends Activity {

    HoldTitle information_hold;

    Lable information_id,information_sex,information_birthday,information_profession,information_email,information_city;

    EditLa information_name,information_signature;
    String id,name,signature,sex,birthday,profession,email,city;


    HttpUtil httpUtil=new HttpUtil();

    Handler handler;

    String UID;
    String alter_data;

    AlertDialog birthday_out,sex_out;


    int year,month,day;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_information);

        Intent intent=getIntent();

        UID=intent.getStringExtra("UID");
        name=intent.getStringExtra("name");
        signature=intent.getStringExtra("signature");
        sex=intent.getStringExtra("sex");
        birthday=intent.getStringExtra("birthday");
        profession=intent.getStringExtra("profession");
        email=intent.getStringExtra("email");
        city=intent.getStringExtra("city");

        getViewId();

        information_name.setTv_labletitle(name);
        information_signature.setTv_labletitle(signature);
        information_sex.setTv_labletitle(sex);
        information_birthday.setTv_labletitle(birthday);
        information_email.setTv_labletitle(email);
        information_profession.setTv_labletitle(profession);
        information_city.setTv_labletitle(city);

        information_hold.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       information_hold.setTvmoreOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {

                   }
               }).start();
           }
       });
    }

    public void getViewId(){
        information_hold=findViewById(R.id.el_hold);

        information_id=findViewById(R.id.information_el_id);
        information_id.setTv_labletitle(UID);
        information_name=findViewById(R.id.information_el_name);
        information_signature=findViewById(R.id.information_el_signature);
        information_sex=findViewById(R.id.information_el_sex);
        information_birthday=findViewById(R.id.information_el_birthday);
        information_email=findViewById(R.id.information_el_email);
        information_profession=findViewById(R.id.information_el_profession);
        information_city=findViewById(R.id.information_el_city);

    }

    public void alter_onclick(){
        information_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    alter_birthday();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        information_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alter_sex();
            }
        });
    }

    public void alter_birthday() throws ParseException {

        View inflate = getLayoutInflater().inflate(R.layout.birthday_information, null);
        final AlertDialog.Builder ab = new AlertDialog.Builder(this);
        birthday_out = ab.create();
        birthday_out.setView(inflate);

        alter_data=birthday;

        HoldTitle holdTitle_birthday=inflate.findViewById(R.id.alter_birthday);
        DatePicker datePicker=inflate.findViewById(R.id.data_birthday);

        @SuppressLint("SimpleDateFormat")
        Date formatter=new SimpleDateFormat("yyyy.MM.dd").parse(birthday);
        //assert formatter != null;
        @SuppressLint("SimpleDateFormat")
        String data=new SimpleDateFormat("yyyyMMdd").format(formatter);

        int birthday_data=Integer.parseInt(data);

        int year=birthday_data/10000;
        int month=birthday_data/100%100;
        int day=birthday_data%100;

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                AlterInformation.this.year=year;
                AlterInformation.this.month=monthOfYear+1;
                AlterInformation.this.day=dayOfMonth;
                alter_data=AlterInformation.this.year+"."+AlterInformation.this.month+"."+AlterInformation.this.day;
                information_birthday.setTv_labletitle(alter_data);
                birthday_out.dismiss();
            }
        });
        holdTitle_birthday.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthday_out.dismiss();
            }
        });


        birthday_out.show();

    }

    public void alter_sex(){
        View view=getLayoutInflater().inflate(R.layout.sex_information,null);
        final AlertDialog.Builder ab=new AlertDialog.Builder(this);
        sex_out=ab.create();
        sex_out.setView(view);

        HoldTitle holdTitle_sex=view.findViewById(R.id.alter_sex);
        RadioGroup radioGroup=view.findViewById(R.id.rg_man_women);
        RadioButton radioButton_man=view.findViewById(R.id.rb_man);
        RadioButton radioButton_women=view.findViewById(R.id.rb_women);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {

                if (radioButton_man.getId()==i){
                    information_sex.setTv_labletitle(radioButton_man.getText().toString());
                    sex_out.dismiss();
                }else if (radioButton_women.getId()==i){
                    information_sex.setTv_labletitle(radioButton_women.getText().toString());
                    sex_out.dismiss();
                }
            }
        });

        holdTitle_sex.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex_out.dismiss();
            }
        });
        sex_out.show();
    }
}

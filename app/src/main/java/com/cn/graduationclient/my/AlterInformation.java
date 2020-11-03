package com.cn.graduationclient.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.cn.graduationclient.R;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.my.city.ProvinceCity;
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

    AlertDialog birthday_out,sex_out,profession_out,city_out;


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
                       String alter_signature=information_signature.getTv_labletitle();
                       String alter_name=information_name.getTv_labletitle();
                       String alter_sex=information_sex.getTv_labletitle();
                       String alter_birthday=information_birthday.getTv_labletitle();
                       String alter_profession=information_profession.getTv_labletitle();
                       String alter_city=information_city.getTv_labletitle();
                   }
               }).start();
           }
       });
       alter_onclick();
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
        information_profession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alter_profession();
            }
        });

        information_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alter_city();
            }
        });
    }

    public void alter_birthday() throws ParseException {

        View inflate = getLayoutInflater().inflate(R.layout.birthday_information, null);
        final AlertDialog.Builder ab = new AlertDialog.Builder(this);
        birthday_out = ab.create();
        final Window window=birthday_out.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        birthday_out.setView(inflate);

        alter_data=birthday;

        //birthday="2020.09.05";

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

        datePicker.init(year, month-1, day, new DatePicker.OnDateChangedListener() {
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
        final Window window=sex_out.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        sex_out.setView(view);

        HoldTitle holdTitle_sex=view.findViewById(R.id.alter_sex);
        RadioGroup radioGroup=view.findViewById(R.id.rg_man_women);
        RadioButton radioButton_man=view.findViewById(R.id.rb_man);
        RadioButton radioButton_women=view.findViewById(R.id.rb_women);

        if (information_sex.getTv_labletitle().equals("男")){
            radioButton_man.setChecked(true);
        }else if (information_sex.getTv_labletitle().equals("女")){
            radioButton_women.setChecked(true);
        }

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

    public void alter_profession(){

        View view=getLayoutInflater().inflate(R.layout.profession_alter,null);
        final AlertDialog.Builder ab=new AlertDialog.Builder(this);
        profession_out=ab.create();
        final Window window=profession_out.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        profession_out.setView(view);
        profession_out.show();

        HoldTitle holdTitle_profession=view.findViewById(R.id.profession_alter);
        RadioGroup radioGroup=view.findViewById(R.id.rg_profession);
        RadioButton radioButton_it=view.findViewById(R.id.rb_it);
        RadioButton radioButton_jr=view.findViewById(R.id.rb_jr);
        RadioButton radioButton_ys=view.findViewById(R.id.rb_ys);
        RadioButton radioButton_xz=view.findViewById(R.id.rb_xz);
        RadioButton radioButton_jy=view.findViewById(R.id.rb_jy);
        RadioButton radioButton_xs=view.findViewById(R.id.rb_xs);
        RadioButton radioButton_qt=view.findViewById(R.id.rb_qt);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {

                if (radioButton_it.getId()==i){
                    information_profession.setTv_labletitle(radioButton_it.getText().toString());
                    profession_out.dismiss();
                }else if (radioButton_jr.getId()==i){
                    information_profession.setTv_labletitle(radioButton_jr.getText().toString());
                    profession_out.dismiss();
                }else if (radioButton_xz.getId()==i){
                    information_profession.setTv_labletitle(radioButton_xz.getText().toString());
                    profession_out.dismiss();
                }else if (radioButton_ys.getId()==i){
                    information_profession.setTv_labletitle(radioButton_ys.getText().toString());
                    profession_out.dismiss();
                }else if (radioButton_jy.getId()==i){
                    information_profession.setTv_labletitle(radioButton_jy.getText().toString());
                    profession_out.dismiss();
                }else if (radioButton_xs.getId()==i){
                    information_profession.setTv_labletitle(radioButton_xs.getText().toString());
                    profession_out.dismiss();
                }else if (radioButton_qt.getId()==i){
                    information_profession.setTv_labletitle(radioButton_qt.getText().toString());
                    profession_out.dismiss();
                }
            }
        });

        holdTitle_profession.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profession_out.dismiss();
            }
        });
    }

    public void  alter_city(){

        View view=getLayoutInflater().inflate(R.layout.city_alter,null);
        final AlertDialog.Builder ab=new AlertDialog.Builder(this);
        city_out=ab.create();
        final Window window=city_out.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        city_out.setView(view);
        city_out.show();

        HoldTitle holdTitle=view.findViewById(R.id.hold_city);
        Spinner spinner=view.findViewById(R.id.spinner_province);

        //city=information_city.getTv_labletitle();

        spinner.setDropDownWidth(400); //下拉宽度
        spinner.setDropDownHorizontalOffset(100); //下拉的横向偏移
        spinner.setDropDownVerticalOffset(100); //下拉的纵向偏移

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ProvinceCity.province);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        for (int i=0;i<34;i++){
            if (ProvinceCity.province[i].equals(city)){
                spinner.setSelection(i);
                break;
            }
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                information_city.setTv_labletitle(adapter.getItem(position));
                //city_out.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city_out.dismiss();
            }
        });

    }
}

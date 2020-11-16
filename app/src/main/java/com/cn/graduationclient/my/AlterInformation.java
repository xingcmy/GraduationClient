package com.cn.graduationclient.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.db.HeadDbHelper;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.message.FriendMessage;
import com.cn.graduationclient.my.city.ProvinceCity;
import com.cn.graduationclient.tool.MsgTool;
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

    private static final int REQUEST_CODE = 1;
    HoldTitle information_hold;

    Lable information_id,information_sex,information_birthday,information_profession,information_email,information_city;

    EditLa information_name,information_signature;
    ImageView imageView;
    String id,name,signature,sex,birthday,profession,email,city;


    HttpUtil httpUtil=new HttpUtil();


    String UID;
    String alter_data;

    AlertDialog birthday_out,sex_out,profession_out,city_out;


    int year,month,day;
    String path;

    HeadDbHelper headDbHelper;
    SQLiteDatabase sqLiteDatabase;

    android.app.AlertDialog out;

    @SuppressLint("HandlerLeak")
    Handler pathHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.obj!=null||msg.obj!=""){
                View view=getLayoutInflater().inflate(R.layout.file_path,null);
                final android.app.AlertDialog.Builder ab=new android.app.AlertDialog.Builder(AlterInformation.this);
                out=ab.create();
                final Window window=out.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(0));
                out.setView(view);
                out.show();
                HoldTitle holdTitle=view.findViewById(R.id.file_path_hold);
                TextView textView=view.findViewById(R.id.text_file_path);
                ImageView img=view.findViewById(R.id.file_path_img);
                String filePath=(String)msg.obj;
                Bitmap bitmap=new MsgTool().decodeSampleBitmap(img,filePath);
                img.setImageBitmap(bitmap);
                textView.setText(filePath);
                holdTitle.setTvmoreOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        if (filePath!=null||filePath!=""){
                            byte[] BFile =new MsgTool().getBytesByFile(filePath);
                            String bytes=new MsgTool().ByteToString(BFile);
                            FileMsg(bytes,filePath);
                        }else {
                            Toast.makeText(AlterInformation.this,"没有选择文件",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        out.dismiss();
                    }
                });
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    Toast.makeText(AlterInformation.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    AlterInformation.this.finish();
                    break;
                case 0x02:
                    Toast.makeText(AlterInformation.this,"修改失败",Toast.LENGTH_SHORT).show();
                    break;
                case 0x03:
                    String alter_path=(String) msg.obj;
                    Bitmap bitmap=new MsgTool().decodeSampleBitmap(imageView,alter_path);
                    imageView.setImageBitmap(bitmap);
                    Toast.makeText(AlterInformation.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_information);

        headDbHelper=new HeadDbHelper(AlterInformation.this);
        sqLiteDatabase=headDbHelper.getReadableDatabase();

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
        Cursor cursor=sqLiteDatabase.rawQuery("select * from head where uid='"+UID+"'",null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                Bitmap bitmap=new MsgTool().decodeSampleBitmap(imageView,cursor.getString(1));
                imageView.setImageBitmap(bitmap);
            }
        }

        information_name.setTv_labletitle(name);
        information_signature.setTv_labletitle(signature);
        information_sex.setTv_labletitle(sex);
        information_birthday.setTv_labletitle(birthday);
        information_email.setTv_labletitle(email);
        information_profession.setTv_labletitle(profession);
        information_city.setTv_labletitle(city);
        alter_data=birthday;

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

                       try {
                           String error=httpUtil.httpAlterInformation(UID,alter_name,alter_signature,alter_sex,alter_birthday,alter_profession,alter_city);

                           JSONObject jsonObject=new JSONObject(error);
                           Message message=new Message();
                           if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){

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
       });
       alter_onclick();
    }

    public void getViewId(){
        information_hold=findViewById(R.id.el_hold);

        imageView=findViewById(R.id.alter_information_head);
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

    /**
     * 修改信息
     */
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

    /**
     * 生日
     * @throws ParseException
     */
    public void alter_birthday() throws ParseException {

        View inflate = getLayoutInflater().inflate(R.layout.birthday_information, null);
        final AlertDialog.Builder ab = new AlertDialog.Builder(this);
        birthday_out = ab.create();
        final Window window=birthday_out.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        birthday_out.setView(inflate);

        //alter_data=birthday;

        //birthday="2020.09.05";

        HoldTitle holdTitle_birthday=inflate.findViewById(R.id.alter_birthday);
        DatePicker datePicker=inflate.findViewById(R.id.data_birthday);

        @SuppressLint("SimpleDateFormat")
        Date formatter=new SimpleDateFormat("yyyy.MM.dd").parse(alter_data);
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
                if (AlterInformation.this.month<10||AlterInformation.this.day<10){
                    if (AlterInformation.this.month<10&& AlterInformation.this.day<10){
                        alter_data=AlterInformation.this.year+".0"+AlterInformation.this.month+".0"+AlterInformation.this.day;
                    }else if (AlterInformation.this.day<10){
                        alter_data=AlterInformation.this.year+"."+AlterInformation.this.month+".0"+AlterInformation.this.day;
                    }else {
                        alter_data=AlterInformation.this.year+".0"+AlterInformation.this.month+"."+AlterInformation.this.day;
                    }

                }else {
                    alter_data=AlterInformation.this.year+"."+AlterInformation.this.month+"."+AlterInformation.this.day;
                }

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

    /**
     * 性别
     */
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
            if (ProvinceCity.province[i].equals(information_city.getTv_labletitle())){
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

    public void onclickHead(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpg");//选择图片
        // intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        // intent.setType("*/*");//无限制
        this.startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Message message=new Message();
        if (resultCode== Activity.RESULT_OK){
            Uri uri=data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){
                path=uri.getPath();
                message.obj=path;
                pathHandler.sendMessage(message);
                return;
            }
            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
                path=getPath(this,uri);
            }else {
                path=getRealPathFromURI(uri);
            }
            message.obj=path;
            pathHandler.sendMessage(message);
            return;
        }
        if (data == null) {
            // 用户未选择任何文件，直接返回
            path="";
            message.obj=path;
            pathHandler.sendMessage(message);
            return;
        }
        Uri uri = data.getData(); // 获取用户选择文件的URI
        // 通过ContentProvider查询文件路径
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // 未查询到，说明为普通文件，可直接通过URI获取文件路径
            path = uri.getPath();
            message.obj=path;
            pathHandler.sendMessage(message);
            return;
        }
        if (cursor.moveToFirst()) {
            // 多媒体文件，从数据库中获取文件的真实路径
            path = cursor.getString(cursor.getColumnIndex("_data"));
            message.obj=path;
            pathHandler.sendMessage(message);
        }
        cursor.close();
    }


    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            return getDataColumn(context, contentUri, null, null);
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{split[1]};

            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }





    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.external.storage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public void FileMsg(String fileString,String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String fileReturn=httpUtil.httpSetHead(UID,fileString);
                    JSONObject jsonObject=new JSONObject(fileReturn);
                    Message message=new Message();
                    if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                        message.what=0x03;
                        message.obj=path;

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
}

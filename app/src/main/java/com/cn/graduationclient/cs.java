package com.cn.graduationclient;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class cs extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread()
        {
            //重写线程run方法
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run()
            {
                //定义okhttp对象
                OkHttpClient okHttpClient = new OkHttpClient();
                try
                {
                    //如果传输文件
                    FileInputStream inputStream = new FileInputStream(new File(getDataDir() + "/x1.png"));
                    //文件读取为bytes
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    //将bytes使用base64编码
                    String a1 = Base64.encodeToString(bytes, Base64.NO_PADDING);
                    //post的方法体，用来添加数据（key-value方式），但只能添加字符串作为数据
                    FormBody formBody = new FormBody.Builder().add("w1", a1).add("w2", "wad").build();
                    //请求创建
                    Request request = new Request.Builder().url("http://192.168.1.104:8080/titan/login").post(formBody).build();
                    //执行请求
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();//获得服务端返回内容
                    //将返回内容作为字符串输出，也可转换为bytes等
                    System.out.println(response.body().string());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();//线程直接启动

    }
}

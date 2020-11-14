package com.cn.graduationclient.http;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.xingcmyAdapter.json.JSONUtil;
import com.cn.graduationclient.xingcmyAdapter.json.MessageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil implements ClientHttp {

    private static String str_url="http://192.168.1.101:8080/xingcmy/";

    @Override
    public HttpURLConnection http(String servlet) {
        try {
            URL url = new URL(str_url+servlet);
            URLConnection rulConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;

            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setDoInput(true);

            httpUrlConnection.setUseCaches(false);

            httpUrlConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");

            httpUrlConnection.setRequestProperty("accept", "*/*");
            httpUrlConnection.setRequestProperty("connection", "Keep-Alive");

            httpUrlConnection.setRequestMethod("POST");

            return httpUrlConnection;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public String httpLogin(String username, String password) throws IOException, JSONException {
       HttpURLConnection httpUrlConnection= http("Login");

        OutputStream outputStream = httpUrlConnection.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put(StructureSystem.TYPE, TypeSystem.LOGIN);
        jsonObject.put(StructureSystem.USERNAME,username);
        jsonObject.put(StructureSystem.PASSWORD,password);
//        out.writeBytes(jsonObject.toString());
//        out.flush();
        objectOutputStream.writeObject(jsonObject.toString());
        objectOutputStream.flush();
        objectOutputStream.close();


        InputStreamReader read = new InputStreamReader(
                httpUrlConnection.getInputStream());
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        while ((line = reader.readLine()) != null) {

            return  line;

        }
        return null;
    }

    @Override
    public String httpRegister(String phone,String password,String email) throws IOException, JSONException {
        HttpURLConnection httpURLConnection=http("Register");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put(StructureSystem.TYPE,TypeSystem.REGISTER);
        jsonObject.put(StructureSystem.PHONE,phone);
        jsonObject.put(StructureSystem.PASSWORD,password);
        jsonObject.put(StructureSystem.EMAIL,email);

        objectOutputStream.writeObject(jsonObject.toString());
        objectOutputStream.flush();
        objectOutputStream.close();

        InputStreamReader read = new InputStreamReader(
                httpURLConnection.getInputStream());
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        while ((line = reader.readLine()) != null) {

            return  line;

        }
        return null;
    }

    @Override
    public String httpPhone(String phone,String type)throws IOException,JSONException{
        HttpURLConnection httpURLConnection=http("Phone");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();

        jsonObject.put(StructureSystem.PHONE,phone);
        jsonObject.put(StructureSystem.TYPE,type);

        objectOutputStream.writeObject(jsonObject.toString());
        objectOutputStream.flush();
        objectOutputStream.close();

        InputStreamReader read = new InputStreamReader(
                httpURLConnection.getInputStream());
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        while ((line = reader.readLine()) != null) {

            return  line;

        }
        return null;
    }

    @Override
    public String httpEmail(String email,String type) throws IOException, JSONException {
        HttpURLConnection httpURLConnection=http("Email");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();

        jsonObject.put(StructureSystem.EMAIL,email);
        jsonObject.put(StructureSystem.TYPE,type);

        objectOutputStream.writeObject(jsonObject.toString());
        objectOutputStream.flush();
        objectOutputStream.close();

        InputStreamReader read = new InputStreamReader(
                httpURLConnection.getInputStream());
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        while ((line = reader.readLine()) != null) {

            return  line;

        }
        return null;
    }

    @Override
    public String httpInformation(String id) throws IOException,JSONException{

        HttpURLConnection httpURLConnection=http("Information");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();

        jsonObject.put(StructureSystem.ID,id);

        objectOutputStream.writeObject(jsonObject.toString());
        objectOutputStream.flush();
        objectOutputStream.close();

        InputStreamReader read = new InputStreamReader(
                httpURLConnection.getInputStream());
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        while ((line = reader.readLine()) != null) {

            return  line;

        }
        return null;

    }

    @Override
    public String httpGetIdPhoneEmail(String idEmailPhone) throws IOException,JSONException{

        HttpURLConnection httpURLConnection=http("ReturnIdPhoneEmail");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();

        jsonObject.put(StructureSystem.ID,idEmailPhone);

        objectOutputStream.writeObject(jsonObject.toString());
        objectOutputStream.flush();
        objectOutputStream.close();

        InputStreamReader read = new InputStreamReader(
                httpURLConnection.getInputStream());
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        while ((line = reader.readLine()) != null) {

            return  line;

        }
        return null;

    }

    @Override
    public String httpFriendId(String id) throws IOException, JSONException {
        HttpURLConnection httpURLConnection=http("ReturnFriendId");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();

        jsonObject.put(StructureSystem.UID,id);

        objectOutputStream.writeObject(jsonObject.toString());
        objectOutputStream.flush();
        objectOutputStream.close();

        InputStreamReader read = new InputStreamReader(
                httpURLConnection.getInputStream());
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        while ((line = reader.readLine()) != null) {
            return line;

        }
        return null;
    }

    @Override
    public String httpSendMsg(String UID,String id,String msg,int type,int write)throws IOException,JSONException{
        HttpURLConnection httpURLConnection=http("Msg");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);

        MessageUtil messageUtil=new MessageUtil();
        Map<String, Object> resultMap = new HashMap<>();
        messageUtil.setStatus(write);
        messageUtil.setType(type);
        messageUtil.setSender(UID);
        messageUtil.setFromTo(id);
        messageUtil.setMsg(msg);



        objectOutputStream.writeObject(JSONUtil.ObjectToJson(messageUtil));
        objectOutputStream.flush();
        objectOutputStream.close();

        InputStreamReader reader=new InputStreamReader(httpURLConnection.getInputStream());
        BufferedReader readL=new BufferedReader(reader);
        String line="";
        while ((line=readL.readLine())!=null){
            return line;
        }
        return null;
    }

    @Override
    public String httpInMsg(String UID,int read) throws IOException, JSONException {
        HttpURLConnection httpURLConnection=http("Msg");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("sender",UID);
        jsonObject.put("status",read);

        objectOutputStream.writeObject(jsonObject.toString());
        objectOutputStream.flush();
        objectOutputStream.close();

        InputStreamReader reader=new InputStreamReader(httpURLConnection.getInputStream());
        BufferedReader readL=new BufferedReader(reader);
        String line="";
        while ((line=readL.readLine())!=null){
            return line;
        }
        return null;
    }
    @Override
    public String httpAlterInformation(String uid,String name,String signature,String sex,String birthday,String profession,String city) throws IOException, JSONException {
        HttpURLConnection httpURLConnection=http("AlterInformation");

        OutputStream outputStream=httpURLConnection.getOutputStream();

        ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();
//        JSONArray jsonArray=new JSONArray();
//
//        jsonArray.put(TypeSystem.MSG_MUSIC,name);

        jsonObject.put(StructureSystem.UID,uid);
        jsonObject.put(StructureSystem.NAME,name);
        jsonObject.put(StructureSystem.SIGNATURE,signature);
        jsonObject.put(StructureSystem.SEX,sex);
        jsonObject.put(StructureSystem.BIRTHDAY,birthday);
        jsonObject.put(StructureSystem.PROFESSION,profession);
        jsonObject.put(StructureSystem.CITY,city);

        objectOutputStream.writeObject(jsonObject.toString());
        //objectOutputStream.writeObject(jsonArray.toString());
        objectOutputStream.flush();
        objectOutputStream.close();

        InputStreamReader reader=new InputStreamReader(httpURLConnection.getInputStream());

        BufferedReader bufferedReader=new BufferedReader(reader);
        String line="";
        if ((line=bufferedReader.readLine())!=null){
            return line;
        }
        return null;
    }
}

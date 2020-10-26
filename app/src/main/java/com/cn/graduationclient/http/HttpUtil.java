package com.cn.graduationclient.http;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.cmd.TypeSystem;

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

public class HttpUtil {

    private static String str_url="http://192.168.1.104:8080/xingcmy/";

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

    public String httpEmail(String email) throws IOException, JSONException {
        HttpURLConnection httpURLConnection=http("Email");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();

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

    public String httpGetIdPhoneEmail(String id) throws IOException,JSONException{

        HttpURLConnection httpURLConnection=http("ReturnIdPhoneEmail");

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

    public String httpSendMsg(String UID,String id,String msg,String type,int write)throws IOException,JSONException{
        HttpURLConnection httpURLConnection=http("Msg");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put(StructureSystem.UID,UID);
        jsonObject.put(StructureSystem.WRITE,write);
        jsonObject.put(StructureSystem.READ,write);
        switch (type){
            case StructureSystem.MSG_TEXT:
                jsonObject.put(StructureSystem.MSG_TYPE,TypeSystem.MSG_TEXT);
                jsonObject.put(StructureSystem.ID,id);
                jsonObject.put(StructureSystem.MSG,msg);
                break;
            case StructureSystem.MSG_IMAGE:
                jsonObject.put(StructureSystem.MSG_TYPE,TypeSystem.MSG_IMAGE);
                jsonObject.put(StructureSystem.ID,id);
                break;
            case StructureSystem.MSG_MUSIC:
                jsonObject.put(StructureSystem.MSG_TYPE,TypeSystem.MSG_MUSIC);
                jsonObject.put(StructureSystem.ID,id);
                break;
            case StructureSystem.MSG_VIDEO:
                jsonObject.put(StructureSystem.MSG_TYPE,TypeSystem.MSG_VIDEO);
                jsonObject.put(StructureSystem.ID,id);
                break;
            case StructureSystem.MSG_NEW:
                jsonObject.put(StructureSystem.MSG_NEW,TypeSystem.MSG_NEW);
                jsonObject.put(StructureSystem.ID,id);
                break;
        }


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

    public Object httpInMsg(String UID,int read) throws IOException, JSONException {
        HttpURLConnection httpURLConnection=http("Msg");

        OutputStream outputStream=httpURLConnection.getOutputStream();
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put(StructureSystem.UID,UID);
        jsonObject.put(StructureSystem.WRITE,read);
        jsonObject.put(StructureSystem.READ,read);

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
}

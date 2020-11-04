package com.cn.graduationclient.http;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface ClientHttp {
    HttpURLConnection http(String servlet);
    String httpLogin(String username, String password)throws IOException, JSONException;
    String httpRegister(String phone, String password, String email) throws IOException, JSONException;
    String httpEmail(String email) throws IOException, JSONException;
    String httpInformation(String id) throws IOException,JSONException;
    String httpGetIdPhoneEmail(String id) throws IOException,JSONException;
    String httpFriendId(String id) throws IOException, JSONException;
    String httpSendMsg(String UID, String id, String msg, int type, int write)throws IOException,JSONException;
    Object httpInMsg(String UID, int read) throws IOException, JSONException;
    String httpAlterInformation(String uid, String name, String signature, String sex,
                                String birthday, String profession, String city) throws IOException, JSONException;
}

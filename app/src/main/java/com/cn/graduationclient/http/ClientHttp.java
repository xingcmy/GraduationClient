package com.cn.graduationclient.http;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface ClientHttp {

    interface post{
        /**
         * 启用post
         * @param servlet
         * @return
         */
        HttpURLConnection http(String servlet);

        /**
         * 向服务端发送username和password
         * @param username
         * @param password
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpLogin(String username, String password)throws IOException, JSONException;

        /**
         * 向服务端发送手机号、密码 和邮箱并返回ID账号
         * @param phone
         * @param password
         * @param email
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpRegister(String phone, String password, String email) throws IOException, JSONException;

        /**
         * 检测手机号是否已注册
         * @param phone
         * @param type 用于判断是登录还是注册
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpPhone(String phone,String type)throws IOException,JSONException;

        /**
         * 检测邮箱是否已注册
         * @param email
         * @param type 用于判断是登陆还是注册
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpEmail(String email,String type) throws IOException, JSONException;

        /**
         * 获取当前账号的信息
         * @param id
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpInformation(String id) throws IOException,JSONException;

        /**
         * 获取当前账号的手机号、邮箱和ID
         * @param id
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpGetIdPhoneEmail(String id) throws IOException,JSONException;

        /**
         * 返回好友列表
         * @param id
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpFriendId(String id) throws IOException, JSONException;

        /**
         * 向好友发送消息
         * @param UID 发送账号
         * @param id 接收账号
         * @param msg 消息
         * @param type 消息类别
         * @param write 发送
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpSendMsg(String UID, String id, String msg, int type, int write)throws IOException,JSONException;

        /**
         * 获取消息
         * @param UID
         * @param read
         * @return
         * @throws IOException
         * @throws JSONException
         */
        Object httpInMsg(String UID, int read) throws IOException, JSONException;

        /**
         * 修改账号信息
         * @param uid
         * @param name
         * @param signature
         * @param sex
         * @param birthday
         * @param profession
         * @param city
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpAlterInformation(String uid, String name, String signature, String sex,
                                    String birthday, String profession, String city) throws IOException, JSONException;

        /**
         * 设置账号头像
         * @param id
         * @param head
         * @return
         * @throws IOException
         */
        String httpSetHead(String id,String head) throws IOException;

        /**
         * 获取账号头像
         * @param id
         * @return
         * @throws IOException
         * @throws JSONException
         */
        String httpGetHead(String id) throws IOException, JSONException;

        /**
         * 随机聊天
         * @param setId
         * @param getId
         * @param msg
         * @param type
         * @return
         * @throws IOException
         * @throws JSONException
         */

        String httpRandom(String setId,String getId,String msg,int type)throws IOException,JSONException;
    }


    interface get{
        /**
         * 启用get
         * @param servlet
         * @return
         */
        HttpURLConnection http(String servlet);
    }

}

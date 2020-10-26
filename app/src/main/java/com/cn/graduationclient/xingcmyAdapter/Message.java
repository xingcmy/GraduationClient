package com.cn.graduationclient.xingcmyAdapter;

import android.widget.ImageView;

import com.cn.graduationclient.R;

public class Message {
    private String id;
    private String name;//昵称
    private String nowmessage;//当前消息
    private String time;//时间
    private ImageView image_message_more;

    public Message(){

    }

    public Message(String id, String name, String nowmessage, String time){
        this.id=id;
        this.name=name;
        this.nowmessage=nowmessage;
        this.time=time;
    }
    public String getId(){
        return id;
    }
    private void setId(String id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getNowmessage(){
        return nowmessage;
    }
    public void setNowmessage(String nowmessage){
        this.nowmessage=nowmessage;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time=time;
    }
    public void setImage_message_more(ImageView image_message_more){
        this.image_message_more=image_message_more;
    }


}

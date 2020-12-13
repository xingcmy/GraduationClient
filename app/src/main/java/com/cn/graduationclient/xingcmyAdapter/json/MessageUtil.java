package com.cn.graduationclient.xingcmyAdapter.json;

import org.codehaus.jackson.map.ObjectMapper;

public class MessageUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    //响应业务状态
    private int status;
    //消息类型
    private int type;
    //发送人
    private String sender;
    //收件人
    private String fromTo;
    //相应消息
    private Object msg;
    //相应中的数据
    private Object data;
//音乐名
    private String name;
//后缀名
    private String ends;

    public static ObjectMapper getMAPPER() {
        return MAPPER;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public String getFromTo() {
        return fromTo;
    }

    public String getSender() {
        return sender;
    }

    public void setFromTo(String fromTo) {
        this.fromTo = fromTo;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }
}

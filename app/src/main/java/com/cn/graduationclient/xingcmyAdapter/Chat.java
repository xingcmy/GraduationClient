package com.cn.graduationclient.xingcmyAdapter;

public class Chat {
    private String message;
    private int type;
    private String time;
    private String name;
    private int Msg_type;

    public Chat(String message, int type, String name,int msg_type,String time) {
        this.message = message;
        this.type = type;
        this.name = name;
        this.Msg_type=msg_type;
        this.time=time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMsg_type() {
        return Msg_type;
    }

    public void setMsg_type(int msg_type) {
        Msg_type = msg_type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

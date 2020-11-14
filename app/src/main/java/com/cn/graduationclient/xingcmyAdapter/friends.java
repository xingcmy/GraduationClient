package com.cn.graduationclient.xingcmyAdapter;

public class friends {
    private String id;
    private String msg;
    private int type;
    private String time;
    public friends(String id,String msg,String time,int type){
        this.id=id;
        this.msg=msg;
        this.time=time;
        this.type=type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

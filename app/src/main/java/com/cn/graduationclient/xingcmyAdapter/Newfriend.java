package com.cn.graduationclient.xingcmyAdapter;

public class Newfriend {
    private String id;
    private String msg;
    public String type;

    /**
     *
     * @param id 账号
     * @param msg 消息
     * @param type 状态
     */
    public Newfriend(String id,String msg,String type){
        this.id=id;
        this.msg=msg;
        this.type=type;
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

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

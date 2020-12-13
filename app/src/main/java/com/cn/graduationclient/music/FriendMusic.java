package com.cn.graduationclient.music;

public class FriendMusic {
    private String name;
    private Object music;

    public FriendMusic(){}

    public FriendMusic(String name,Object music){
        this.name=name;
        this.music=music;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Object getMusic() {
        return music;
    }

    public void setMusic(Object music) {
        this.music = music;
    }

    @Override
    public String toString() {
        return "FriendMusic{" +
                "name='" + name + '\'' +
                ", music=" + music +
                '}';
    }
}

package com.cn.graduationclient.my.other;

public class User {
    private String id;
    private String name;
    private String signature;
    private String sex;
    private String birthday;
    private String profession;
    private String email;
    private String city;

    public User(){}
    public User(String id,String name,String signature,String sex,String birthday,String profession,String email,String city){
        this.id=id;
        this.name=name;
        this.signature=signature;
        this.sex=sex;
        this.birthday=birthday;
        this.profession=profession;
        this.email=email;
        this.city=city;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getProfession() {
        return profession;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

package com.homechat.homechat.Objects;

/**
 * Created by manuel on 19/10/16.
 */

public class User {

    String name;
    String image;
    String email;
    String desc;

    public User(){}

    public User(String name,String image, String email, String desc) {
        this.name = name;
        this.image  = image;
        this.email = email;
        this.desc  = desc;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getDesc(){
        return desc;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
}

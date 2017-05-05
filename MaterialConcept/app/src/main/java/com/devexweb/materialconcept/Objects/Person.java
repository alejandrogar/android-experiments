package com.devexweb.materialconcept.Objects;

/**
 * Created by manuel on 21/11/16.
 */

public class Person {

    String name;
    String role;
    String email;
    String image;
    String image_thumbnail;

    public Person(){}

    public Person(String name, String role, String email, String image, String image_thumbnail){
        this.name = name;
        this.role = role;
        this.email = email;
        this.image = image;
        this.image_thumbnail = image_thumbnail;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getRole(){
        return role;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return image;
    }
    public void setImage_thumbnail(String image_thumbnail){
        this.image_thumbnail = image_thumbnail;
    }
    public String getImage_thumbnail(){
        return image_thumbnail;
    }

}

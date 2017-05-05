package com.devexweb.materialconcept.Objects;

public class NewsObj {

    String title;
    int image;
    long date;

    public NewsObj(){}


    public NewsObj(String title, int image, long date){
        this.title = title;
        this.image = image;
        this.date = date;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setImage(int image){
        this.image= image;
    }

    public int getImage(){
        return image;
    }

    public void setDate(long date){
        this.date = date;
    }

    public long getDate(){
        return date;
    }
}

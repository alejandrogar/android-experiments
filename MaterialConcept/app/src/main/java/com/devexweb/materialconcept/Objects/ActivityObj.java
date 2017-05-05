package com.devexweb.materialconcept.Objects;

import java.util.ArrayList;
import java.util.List;

public class ActivityObj {

    int type_icon;
    String type;
    String content;
    int icon;
    int image;
    long date;
    String time;
    String place;

    public static List<ActivityObj> ActivityObj = new ArrayList<>();

    public ActivityObj(){}

    public ActivityObj(int type_icon,String type, int icon, String content, int image, long date, String time, String place){
        this.type_icon =  type_icon;
        this.type =  type;
        this.content =  content;
        this.icon =  icon;
        this.image = image;
        this.date =  date;
        this.time =  time;
        this.place = place;
    }

    public void setType_icon(int type_icon){
        this.type_icon = type_icon;
    }
    public int getType_icon(){
        return type_icon;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return content;
    }
    public void setIcon(int icon){
        this.icon = icon;
    }
    public int getIcon(){
        return icon;
    }
    public void setImage(int image){
        this.image = image;
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
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }
    public void setPlace(String place){
        this.place = place;
    }
    public String getPlace(){
        return place;
    }

}
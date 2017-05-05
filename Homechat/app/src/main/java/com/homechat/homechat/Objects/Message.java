package com.homechat.homechat.Objects;

/**
 * Created by manuel on 20/10/16.
 */
public class Message {

    private String key;
    private Long date;
    private String from;
    private String message;
    private String image;

    private Message() {
    }

    public Message(String key, Long date, String from, String message, String image) {
        this.key = key;
        this.date = date;
        this.from  = from;
        this.message  = message;
        this.image = image;
    }

    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }

    public Long getDate(){
        return date;
    }
    public void setDate(Long date){
        this.date = date;
    }

    public String getFrom(){
        return from;
    }
    public void setFrom(String from){
        this.from = from;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }

    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }
}

package com.homechat.homechat.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 19/10/16.
 */


public class Chat {

    private String userKey;
    private String key;
    private String image;

    public static List<Chat> Chat = new ArrayList<>();

    public Chat() {}

    public Chat(String userKey, String key, String image) {
        this.userKey = userKey;
        this.key = key;
        this.image  = image;
    }

    public String getUserKey(){
        return userKey;
    }
    public void setUserKey(String userKey){
        this.userKey = userKey;
    }

    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }

    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }
}

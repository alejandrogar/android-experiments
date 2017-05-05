package com.jenjfood.jfood.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 5/08/16.
 */
public class Tip {

    private String body;

    public static List<Ingredient> Tip = new ArrayList<>();

    public Tip(){
    }

    public Tip(String body){
        this.body = body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getBody(){
        return body;
    }
}
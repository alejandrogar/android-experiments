package com.jenjfood.jfood.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16/04/16.
 */
public class Ingredient {

    private String name;

    public static List<Ingredient> Ingredient = new ArrayList<>();

    public Ingredient(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}

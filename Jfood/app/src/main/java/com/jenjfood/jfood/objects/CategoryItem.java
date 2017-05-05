package com.jenjfood.jfood.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 29/04/16.
 */
public class CategoryItem {

    private String id;
    private String name;
    private int quantity;
    public static List<CategoryItem> CategoryItem = new ArrayList<>();

    public CategoryItem(){

    }

    public CategoryItem(String id, String name, int quantity){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return quantity;
    }
}

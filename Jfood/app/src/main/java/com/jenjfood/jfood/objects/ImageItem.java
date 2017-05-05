package com.jenjfood.jfood.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 27/05/16.
 */
 

public class ImageItem {

    private String url;

    public static List<ImageItem> ImageItem = new ArrayList<>();

    public ImageItem(String url, String categoryId){
        this.url = url;
    }

    public void seturl(String url){
        this.url = url;
    }

    public String geturl(){
        return url;
    }
}

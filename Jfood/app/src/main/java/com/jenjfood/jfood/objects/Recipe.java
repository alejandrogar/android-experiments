package com.jenjfood.jfood.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 5/04/16.
 */
public class Recipe {

    private String type;
    private String name;
    private String date;
    private String mainPicture;
    private String portions;
    private String pTime;
    private String steps;
    private String category;
    private String ingredients;
    private String gPictures;
    private String author;

    public static List<Recipe> Recipe = new ArrayList<>();

    public Recipe(){
    }

    public Recipe(String type, String name, String date, String mainPicture, String portions, String pTime, String steps, String category, String ingredients, String gPictures, String author) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.mainPicture = mainPicture;
        this.portions = portions;
        this.pTime = pTime;
        this.steps = steps;
        this.category = category;
        this.ingredients = ingredients;
        this.gPictures = gPictures;
        this.author = author;
    }

    public int getId_() {
        return name.hashCode();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public String getPortions() {
        return portions;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    public String getPTime() {
        return pTime;
    }

    public void setPTime(String pTime) {
        this.pTime = pTime;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getgPictures() {
        return gPictures;
    }

    public void setgPictures(String gPictures) {
        this.gPictures = gPictures;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}

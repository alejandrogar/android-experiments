package com.devexweb.materialconcept.Objects;

import java.util.ArrayList;
import java.util.List;

public class ActivityDiary {

    String person,title,hour;

    public static List<ActivityDiary> ActivityDiary = new ArrayList<>();

    public ActivityDiary(){}

    public ActivityDiary(String person, String title,String hour){
        this.person = person;
        this.title = title;
        this.hour = hour;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}

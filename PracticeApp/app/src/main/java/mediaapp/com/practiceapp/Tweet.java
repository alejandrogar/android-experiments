package mediaapp.com.practiceapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 18/03/16.
 */
public class Tweet {

    private String imagen;
    private String text;
    private String date;
    private Integer id;

    public static List<Tweet> Tweets = new ArrayList<>();


    public Tweet(String imagen, String text, String date,Integer id) {
        this.imagen = imagen;
        this.text = text;
        this.date = date;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getImagen() {
        return imagen;
    }

    public Integer getId(){
        return id;
    }
}
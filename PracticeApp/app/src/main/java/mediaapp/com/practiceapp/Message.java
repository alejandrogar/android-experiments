package mediaapp.com.practiceapp;

import java.util.Date;

/**
 * Created by root on 24/03/16.
 */
public class Message {
    private String id;
    private String name;
    private String msg;
    private String date;

    public String getId(){
        return id;
    }

    public Message(){
    }

    public String getMsg(){
        return msg;
    }

    public String getName(){
        return  name;
    }

    public String getDate(){
        return  date;
    }
}

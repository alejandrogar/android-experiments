package mediaapp.com.practiceapp;

/**
 * Created by root on 25/03/16.
 */
public class MessageChat {

    private String id;
    private String name;
    private String msg;
    private String date;

    public MessageChat(String id,String name, String msg,String date){
        this.id = id;
        this.name = name;
        this.msg = msg;
        this.date = date;
    }

    public void  setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return  name;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }

    public void setDate(String date){
        this.date = date;
    }

    public  String getDate(){
        return date;
    }
}

package mediaapp.com.practiceapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 25/03/16.
 */
public class DataSource {

    static List msgs = new ArrayList<MessageChat>();

    static{

        msgs.add(new MessageChat("id","Manuel","sgdfgsdfgsdfg","Date"));
        msgs.add(new MessageChat("id","Juan","dsfgsdfgsdfgsdfgsdg","Date"));
        msgs.add(new MessageChat("id","Eduardo","sdfgsdfgsdfg","Date"));
        msgs.add(new MessageChat("id","Fernanda","sdfgsdfgsdfg","Date"));
        msgs.add(new MessageChat("id","Sofia","sdfgsdfgdsfg","Date"));

    }

}

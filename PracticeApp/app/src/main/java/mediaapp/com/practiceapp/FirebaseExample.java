package mediaapp.com.practiceapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.node.IntNode;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FirebaseExample extends AppCompatActivity implements View.OnClickListener{

    Firebase Chat;

    RecyclerView recycler;
    MessageAdapter adapter;
    MessageChat M;
    private RecyclerView.LayoutManager lManager;
    List<MessageChat> items = new ArrayList<>();

    AuthData authData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        Chat = new Firebase("https://chatepel.firebaseio.com/");
        String e = getIntent().getStringExtra("EMAIL");
        String p = getIntent().getStringExtra("PASSWORD");

        setContentView(R.layout.activity_firebase_example);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton add = (ImageButton) findViewById(R.id.addMessage);
        add.setOnClickListener(this);


        Chat.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Log.i("CANTIDAD DE MENSAJES", "There are " + snapshot.getChildrenCount() + " messages");
                items.clear();
                int Rand = 0;
                for (DataSnapshot msgSnapshot : snapshot.getChildren()) {
                    Log.i("KEY SNAPSHOT", "" + msgSnapshot.getKey());
                    Message m = msgSnapshot.getValue(Message.class);
                    Log.i("RANDOM NUMBER", Rand+"");
                    items.add(new MessageChat("id", m.getName() + ": ", m.getMsg(), "Date"));
                }
                recycler = (RecyclerView) findViewById(R.id.chat);

                adapter = new MessageAdapter(items, getApplicationContext());

                recycler.setAdapter(adapter);
                lManager = new LinearLayoutManager(getApplicationContext());
                recycler.setLayoutManager(lManager);
                recycler.setAdapter(adapter);
                Integer i = items.size();
                recycler.scrollToPosition(i - 1);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        Chat.child("messages").addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                //Message m = snapshot.getValue(Message.class);
                //Log.i("NOMBRE ", m.getName());
                //Log.i("MENSAJE ", m.getMsg());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_chat) {
            Chat.child("messages").setValue("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addMessage){
            EditText editTextMsg = (EditText) findViewById(R.id.MessageText);
            String Name;
            String Message = editTextMsg.getText().toString();

            Name = "Manuel";
            Map<String, String> m = new HashMap<String, String>();

            if(Message.equals("")){
                /*Snackbar snackbar;
                snackbar = Snackbar.make(v, "Data incomplete", Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.RED);
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();*/
            }else{
                m.put("id", "id");
                m.put("name", Name);
                m.put("msg", Message);
                m.put("date", "Date");
                Chat.child("messages").push().setValue(m);

                editTextMsg.setText("");

                /*Snackbar.make(v, "Message added successful", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        }
    }
}

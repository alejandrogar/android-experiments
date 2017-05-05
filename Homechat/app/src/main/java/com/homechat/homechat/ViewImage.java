package com.homechat.homechat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.homechat.homechat.Adapters.MessageAdapter;
import com.homechat.homechat.Objects.Message;

import java.util.ArrayList;

public class ViewImage extends Activity {

    ImageView imageView;

    DatabaseReference messages;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Bundle bundle = this.getIntent().getExtras();

        imageView = (ImageView) findViewById(R.id.ImageView);

        String KEY = bundle.getString("IMAGE");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messages = database.getReference("chats");


        String Bytes = KEY;
        byte[] imageByteArray = Base64.decode(Bytes, Base64.DEFAULT);

        Glide.with(ViewImage.this)
                .load(imageByteArray)
                .asBitmap()
                //.placeholder(R.drawable.ic_broken)
                .into(imageView);

        /*assert KEY != null;
        database.getReference("chats").child("chat1").child("messages").child(KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Message msg = snapshot.getValue(Message.class);



            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("CHAT DATA", "Failed to read value.", error.toException());
            }
        });*/
    }
}

package com.homechat.homechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.homechat.homechat.Objects.User;
import com.homechat.homechat.Utils.Utils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddChat extends AppCompatActivity {
    private static final String TAG = "AddChat";


    @Bind(R.id.search) EditText searchByEmail;
    @Bind(R.id.user_data) CardView userData;
    @Bind(R.id.user_image) CircularImageView userImage;
    @Bind(R.id.user_name) TextView userName;
    @Bind(R.id.user_email) TextView userEmail;
    @Bind(R.id.user_description) TextView userDesc;
    @Bind(R.id.start_chat) RelativeLayout startChat;

    @Bind(R.id.not_found) RelativeLayout notFound;



    Query users;
    FirebaseDatabase database;

    String userKeyPublic;
    String aux = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        ButterKnife.bind(this);

        userKeyPublic = Utils.readSharedSetting(this, Login.USER_UID, "true");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        database = FirebaseDatabase.getInstance();
        database.getReference().keepSynced(true);
        searchByEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == 66){
                    users = database.getReference("users").orderByChild("email").equalTo(searchByEmail.getText().toString());
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User user = null;
                            String Bytes;
                            byte[] imageByteArray = new byte[0];
                            String userKey = null;
                            for (DataSnapshot usersnapshot : snapshot.getChildren()) {
                                user = usersnapshot.getValue(User.class);
                                userKey = usersnapshot.getKey();
                                Bytes = user.getImage();
                                imageByteArray = Base64.decode(Bytes, Base64.DEFAULT);
                            }

                            if(user != null) {
                                userData.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext())
                                        .load(imageByteArray)
                                        .asBitmap()
                                        .placeholder(R.drawable.ic_action_action_account_circle_white)
                                        .into(userImage);
                                userName.setText(user.getName());
                                userEmail.setText(user.getEmail());
                                userDesc.setText(user.getDesc());

                                final String finalUserKey = userKey;
                                final User finalUser = user;

                                database.getReference().child("users/"+userKeyPublic+"/chats").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot chatsnapshot : dataSnapshot.getChildren()){
                                            Log.i("GET CHATS", chatsnapshot.child("userKey").getValue() + " ");
                                            if(Objects.equals(finalUserKey, chatsnapshot.child("userKey").getValue() + "") && !Objects.equals(aux, "false")){
                                                final Intent intent = new Intent(AddChat.this, ChatActivity.class);
                                                final Bundle bundle = new Bundle();

                                                bundle.putString("CHAT_KEY", chatsnapshot.getKey());
                                                bundle.putString("USER_KEY", finalUserKey);

                                                AlertDialog.Builder alert = new AlertDialog.Builder(AddChat.this);
                                                alert.setTitle("Acceder al chat existente asociado a este email?");
                                                alert.setCancelable(false);
                                                // alert.setMessage("Message");

                                                alert.setPositiveButton("Acceder", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        //Your action here
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                        finish();
                                                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                                    }
                                                });

                                                alert.setNegativeButton("Buscar otro",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                                searchByEmail.setText("");

                                                            }
                                                        });

                                                alert.show();
                                                aux = "false";
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                startChat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DatabaseReference chats = database.getReference().child("chats");
                                        final String chatKeyGenerated  = chats.push().getKey();

                                        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                        //Añadir la referencia del chat al usuario solicitado
                                        database.getReference().child("users").child(finalUserKey).child("chats").child(chatKeyGenerated).child("userKey").setValue(userKeyPublic);

                                        //Añadir la referencia del chat al emisor
                                        database.getReference().child("users").child(userKeyPublic).child("chats").child(chatKeyGenerated).child("userKey").setValue(finalUserKey);

                                        Intent intent = new Intent(AddChat.this, ChatActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("CHAT_KEY", chatKeyGenerated);
                                        bundle.putString("USER_KEY", finalUserKey);

                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                                    }
                                });
                            }else{
                                notFound.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w("CHAT DATA", "Failed to read value.", error.toException());
                        }
                    });
                    return false;
                }
                return false;
            }
        });
    }
}

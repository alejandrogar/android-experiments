package com.homechat.homechat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homechat.homechat.Adapters.ChatAdapter;
import com.homechat.homechat.Objects.Chat;
import com.homechat.homechat.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class Home extends AppCompatActivity {

    public static final String CHAT_FIRST_TIME = "chat_first_time";
    private static final String TAG = "Home";

    List<Chat> items;
    ChatAdapter adapter;
    RecyclerView.LayoutManager lManager;

    @Bind(R.id.add_chat) FloatingActionButton addChat;
    @Bind(R.id.recycler) RecyclerView recycler;

    @Bind(R.id.no_chats) RelativeLayout noChats;

    boolean chatFirstTime;

    FirebaseDatabase database;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth auth;
    String userKeyPublic;

    static boolean calledAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        items = null;
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (!calledAlready){
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                    calledAlready = true;
                }

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    database = FirebaseDatabase.getInstance();
                    database.getReference().keepSynced(true);
                    userKeyPublic = Utils.readSharedSetting(Home.this, Login.USER_UID, "false");

                    //Log.i(TAG,"USER UID "+user.getUid());
                    Utils.saveSharedSetting(Home.this, Login.USER_UID, user.getUid());
                    setContentView(R.layout.activity_home);
                    ButterKnife.bind(Home.this);

                    recycler.setVisibility(View.GONE);
                    noChats.setVisibility(View.VISIBLE);

                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);

                    int PERMISSION_ALL = 1;
                    String[] PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.SYSTEM_ALERT_WINDOW};

                    if (!hasPermissions(Home.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(Home.this, PERMISSIONS, PERMISSION_ALL);
                    }

                    recycler.setHasFixedSize(true);
                    recycler.setNestedScrollingEnabled(false);
                    //String key, String name, String date, String previewMessage, String image

                    addChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utils.saveSharedSetting(Home.this, CHAT_FIRST_TIME, "false");
                            Intent intent = new Intent(Home.this, AddChat.class);
                            startActivity(intent);
                        }
                    });

                    chatFirstTime = Boolean.valueOf(Utils.readSharedSetting(Home.this, CHAT_FIRST_TIME, "true"));

                    DatabaseReference chatsRef = database.getReference().child("users/"+ Utils.readSharedSetting(Home.this, Login.USER_UID, "false")+ "/chats");
                    chatsRef.keepSynced(true);
                    //Intent introIntent = new Intent(Home.this, Intro.class);
                    //introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

                    Log.i(TAG, Utils.readSharedSetting(Home.this, Login.USER_UID, "false"));

                    //Obtención de los chats disponibles para el usuario autentificado
                    chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            items = new ArrayList<>();
                            for (DataSnapshot chatsnapshot : snapshot.getChildren()) {
                                //database.getReference().child("users").child(chatsnapshot.getKey()).child(chatsnapshot.getKey()).removeValue();
                                //database.getReference().child("users").child(userKeyPublic).child("chats").child(chatsnapshot.getKey()).removeValue();

                                items.add(new Chat(chatsnapshot.getValue(Chat.class).getUserKey(), chatsnapshot.getKey() , "NO IMAGE"));
                                adapter = new ChatAdapter(items, getApplicationContext());
                                lManager = new LinearLayoutManager(getApplicationContext());
                                recycler.setLayoutManager(lManager);
                                recycler.setAdapter(adapter);

                                recycler.setVisibility(View.VISIBLE);
                                noChats.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i(TAG + " ERROR", "NO DATA BASE");
                            recycler.setVisibility(View.GONE);
                            noChats.setVisibility(View.VISIBLE);
                        }
                    });

                    if (chatFirstTime) {
                        new MaterialTapTargetPrompt.Builder(Home.this)
                            .setTarget(addChat)
                            .setPrimaryText("Añade un nuevo chat")
                            .setSecondaryText("Toca aquí para comenzar a chatear")
                            .setAutoDismiss(false)
                            .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                            {
                                @Override
                                public void onHidePrompt(MotionEvent event, boolean tappedTarget)
                                {
                                    //Do something such as storing a value so that this prompt is never shown again
                                }

                                @Override
                                public void onHidePromptComplete()
                                {

                                }
                            })
                            .show();
                    }
                } else {
                    Utils.saveSharedSetting(Home.this, Login.USER_UID, "NO_USER_LOGGED");
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            //Intent intent = new Intent(Home.this, Login.class);
//            Utils.saveSharedSetting(Home.this, Login.USER_UID, "NO_USER_LOGGED");
            //startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.homechat.homechat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homechat.homechat.ChatActivity;
import com.homechat.homechat.Login;
import com.homechat.homechat.Objects.Chat;
import com.homechat.homechat.Objects.User;
import com.homechat.homechat.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 19/10/16.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    List<Chat> items = new ArrayList<>();
    FirebaseDatabase database;

    private final Context context;

    public ChatAdapter(List<Chat> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView name;
        public CircularImageView image;
        public RelativeLayout launcher;


        private ChatAdapter padre = null;

        public ViewHolder(View v, ChatAdapter padre) {
            super(v);
            this.padre = padre;
            name = (TextView) v.findViewById(R.id.name);
            launcher = (RelativeLayout) v.findViewById(R.id.launcher);
            image = (CircularImageView) v.findViewById(R.id.image);

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;

        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_item, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        database = FirebaseDatabase.getInstance();

        database.getReference("users").orderByKey().equalTo(items.get(i).getUserKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot usersnapshot : snapshot.getChildren()) {

                    viewHolder.name.setText(usersnapshot.getValue(User.class).getName());

                    String Bytes = usersnapshot.getValue(User.class).getImage();
                    byte[] imageByteArray = Base64.decode(Bytes, Base64.DEFAULT);

                    Glide.with(context)
                            .load(imageByteArray)
                            .asBitmap()
                            .placeholder(R.drawable.ic_action_action_account_circle_white)
                            .into(viewHolder.image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewHolder.launcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                Bundle bundle =new Bundle();

                bundle.putString("CHAT_KEY", items.get(i).getKey());
                bundle.putString("USER_KEY", items.get(i).getUserKey());


                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                //((Activity)  context).overridePendingTransition(R.anim.fadein, R.anim.fadeout);


            }
        });
    }

}
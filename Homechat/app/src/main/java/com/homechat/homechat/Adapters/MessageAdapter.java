package com.homechat.homechat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.homechat.homechat.Objects.Message;
import com.homechat.homechat.R;
import com.homechat.homechat.ViewImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by manuel on 21/10/16.
*/

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<Message> items = new ArrayList<>();

    private final Context context;

    public MessageAdapter(List<Message> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView date;
        public TextView from;
        public TextView message,messagesecond;
        public RelativeLayout message_second_layout,message_first_layout;

        public ImageView image_in_message,image_in_message_second;

        public ViewHolder(View v, MessageAdapter padre) {
            super(v);

            //date = (TextView) v.findViewById(R.id.date);
            //from = (TextView) v.findViewById(R.id.from);

            // Message received
            message = (TextView) v.findViewById(R.id.message);
            message_first_layout = (RelativeLayout) v.findViewById(R.id.message_first_layout);
            // If message contains an image
            image_in_message = (ImageView) v.findViewById(R.id.image_in_message);

            // Message send
            messagesecond = (TextView) v.findViewById(R.id.message_second);
            message_second_layout = (RelativeLayout) v.findViewById(R.id.message_second_layout);
            // If you send an image
            image_in_message_second = (ImageView) v.findViewById(R.id.image_in_message_second);
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
                    .inflate(R.layout.message_item, viewGroup, false);

        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final int item = i;
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if(Objects.equals(items.get(i).getMessage(), "")){
            viewHolder.message.setVisibility(View.GONE);
        }else{
            viewHolder.message.setVisibility(View.VISIBLE);
        }
        if(Objects.equals(items.get(i).getMessage(), "") && Objects.equals(items.get(i).getFrom(), currentUserEmail)){
            viewHolder.messagesecond.setVisibility(View.GONE);
        }else {
            viewHolder.messagesecond.setVisibility(View.VISIBLE);
        }

        if(!Objects.equals(items.get(i).getImage(), "NO IMAGE" ) && !Objects.equals(items.get(i).getFrom(), currentUserEmail)){
            String Bytes = items.get(i).getImage();
            byte[] imageByteArray = Base64.decode(Bytes, Base64.DEFAULT);
            viewHolder.image_in_message.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(imageByteArray)
                    .asBitmap()
                    //.placeholder(R.drawable.ic_broken)
                    .into(viewHolder.image_in_message);
        }else if(!Objects.equals(items.get(i).getImage(),"NO IMAGE") && Objects.equals(items.get(i).getFrom(), currentUserEmail)){
            String Bytes = items.get(i).getImage();
            byte[] imageByteArray = Base64.decode(Bytes, Base64.DEFAULT);
            viewHolder.image_in_message_second.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(imageByteArray)
                    .asBitmap()
                    //.placeholder(R.drawable.ic_broken)
                    .into(viewHolder.image_in_message_second);
        }else{
            viewHolder.image_in_message_second.setVisibility(View.GONE);
            viewHolder.image_in_message.setVisibility(View.GONE);
        }

        if(!Objects.equals(items.get(i).getFrom(), currentUserEmail)){
            viewHolder.message_first_layout.setVisibility(View.VISIBLE);
            viewHolder.message_second_layout.setVisibility(View.GONE);
            viewHolder.message.setText(items.get(i).getMessage());
        }else{
            viewHolder.messagesecond.setText(items.get(i).getMessage());
            viewHolder.message_first_layout.setVisibility(View.GONE);
            viewHolder.message_second_layout.setVisibility(View.VISIBLE);
        }

        final Intent intent = new Intent(context , ViewImage.class);
        final Bundle bundle = new Bundle();

        /*viewHolder.image_in_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putString("IMAGE", items.get(item).getImage());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        viewHolder.image_in_message_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putString("IMAGE", items.get(item).getImage());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });*/
    }
}
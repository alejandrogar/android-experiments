package mediaapp.com.practiceapp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 25/03/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<MessageChat> items = new ArrayList<>();

    private final Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView msg;

        private MessageAdapter padre = null;

        public ViewHolder(View v, MessageAdapter padre) {
            super(v);

            this.padre = padre;
            msg = (TextView) v.findViewById(R.id.message);

        }

    }

    public MessageAdapter(List<MessageChat> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = null;
        if (i == 0) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_chat, viewGroup, false);
        }else{
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_chat_right, viewGroup, false);
        }
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final Integer I = i;

        if (i == 0) {
            String name = items.get(i).getName();
            String msg = items.get(i).getMsg();

            //viewHolder.name.setText(name);
            viewHolder.msg.setText(msg);
        }else{
            String name = items.get(i).getName();
            String msg = items.get(i).getMsg();

            //viewHolder.name.setText(name);
            viewHolder.msg.setText(msg);
        }


    }
}
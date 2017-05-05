package com.devexweb.materialconcept.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devexweb.materialconcept.Objects.ActivityObj;
import com.devexweb.materialconcept.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder>{

    List<ActivityObj> items = new ArrayList<>();

    private final Context context;

    public ActivityAdapter(List<ActivityObj> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView event,news;
        LinearLayout date_header;
        ImageView type_icon;

        TextView type;
        TextView date;

        TextView content;
        ImageView icon;


        TextView content_news;
        ImageView image_news;

        private ActivityAdapter root = null;

        public ViewHolder(View v, ActivityAdapter root) {
            super(v);

            event = (CardView) v.findViewById(R.id.event_card);
            news = (CardView) v.findViewById(R.id.news_card);
            date_header = (LinearLayout) v.findViewById(R.id.date_header);
            type_icon = (ImageView) v.findViewById(R.id.type_icon);

            type = (TextView) v.findViewById(R.id.type);
            date = (TextView) v.findViewById(R.id.date);
            content = (TextView) v.findViewById(R.id.content);
            icon = (ImageView) v.findViewById(R.id.icon);

            content_news = (TextView) v.findViewById(R.id.content_news);
            image_news = (ImageView) v.findViewById(R.id.image_news);

            this.root = root;
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
                .inflate(R.layout.item_activity, viewGroup, false);
        return new ViewHolder(v,this);

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.type.setText(items.get(i).getType());
        viewHolder.date.setText(convertTime(items.get(i).getDate()));

        Glide
            .with(context)
            .load(items.get(i).getType_icon())
            .centerCrop()
            //.placeholder(R.drawable.loading_spinner)
            .crossFade()
            .into(viewHolder.type_icon);

        int previous_position=0;

        if(i != 0) previous_position = i-1;

        if(Objects.equals(convertTime(items.get(i).getDate()), convertTime(items.get(previous_position).getDate())) && i != 0){
            viewHolder.date_header.setVisibility(View.GONE);
        }else{
            viewHolder.date_header.setVisibility(View.VISIBLE);
        }

        if(Objects.equals(items.get(i).getType(), "EVENTO")){
            viewHolder.news.setVisibility(View.GONE);
            viewHolder.event.setVisibility(View.VISIBLE);

            viewHolder.content.setText(items.get(i).getContent());
            Glide
                .with(context)
                .load(items.get(i).getIcon())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .crossFade()
                .into(viewHolder.icon);
            
            viewHolder.event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Evento", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            viewHolder.news.setVisibility(View.VISIBLE);
            viewHolder.event.setVisibility(View.GONE);

            viewHolder.content_news.setText(items.get(i).getContent());

            Glide
                .with(context)
                .load(items.get(i).getImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .crossFade()
                .into(viewHolder.image_news);

            viewHolder.news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Noticias", Toast.LENGTH_SHORT).show();
                }
            });
        }

        setFadeAnimation(viewHolder.itemView);

    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1);
        view.startAnimation(anim);
    }

    public String convertTime(long time) {
        Date date = new Date(time * 1000);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");

        String[] days = {"","Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre","Nomviembre", "Diciembre"};

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String formatedDate = days[dayOfWeek] + ", " + dayOfMonth+ " de " + months[month] + " " + year;
        return  formatedDate;
    }
}
package com.devexweb.materialconcept.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devexweb.materialconcept.DetailNews;
import com.devexweb.materialconcept.Objects.NewsObj;
import com.devexweb.materialconcept.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    List<NewsObj> items = new ArrayList<>();

    private final Context context;

    public NewsAdapter(List<NewsObj> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;
        ImageView image;
        CardView card;
        private NewsAdapter root = null;

        public ViewHolder(View v, NewsAdapter root) {
            super(v);

            card = (CardView) v.findViewById(R.id.news_card);

            title = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.date);
            image = (ImageView) v.findViewById(R.id.image);

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
                .inflate(R.layout.item_news, viewGroup, false);
        return new ViewHolder(v,this);

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.title.setText(items.get(i).getTitle());
        viewHolder.date.setText(convertTime(items.get(i).getDate()));

        Glide
            .with(context)
            .load(items.get(i).getImage())
            .centerCrop()
            //.placeholder(R.drawable.loading_spinner)
            .crossFade()
            .into(viewHolder.image);

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailNews.class);
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", items.get(i).getTitle());
                bundle.putString("DATE", convertTime(items.get(i).getDate()));

                bundle.putInt("ITEM", i);
                viewHolder.image.buildDrawingCache();
                Bitmap image= viewHolder.image.getDrawingCache();
                bundle.putParcelable("imagebitmap", image);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

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
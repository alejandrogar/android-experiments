package com.devexweb.materialconcept.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devexweb.materialconcept.Objects.ActivityDiary;
import com.devexweb.materialconcept.PersonDetail;
import com.devexweb.materialconcept.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityDiaryAdapter extends RecyclerView.Adapter<ActivityDiaryAdapter.ViewHolder>{

    List<ActivityDiary> items = new ArrayList<>();

    private final Context context;

    public ActivityDiaryAdapter(List<ActivityDiary> items, Context context){
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout container;
        TextView person;
        TextView title;
        TextView hour;

        public ViewHolder(View v, ActivityDiaryAdapter root) {
            super(v);

            container = (RelativeLayout) v.findViewById(R.id.container);
            person = (TextView) v.findViewById(R.id.person);
            title = (TextView) v.findViewById(R.id.title);
            hour = (TextView) v.findViewById(R.id.hour);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ActivityDiaryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;

        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_diary, viewGroup, false);
        return new ActivityDiaryAdapter.ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(final ActivityDiaryAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.title.setText(items.get(i).getTitle());
        viewHolder.person.setText(items.get(i).getPerson());
        viewHolder.hour.setText(items.get(i).getHour());

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonDetail.class);
                Bundle bundle = new Bundle();

                bundle.putString("TITLE", items.get(i).getTitle());
                bundle.putString("PERSON", items.get(i).getPerson());
                bundle.putString("HOUR", items.get(i).getHour());

                intent.putExtras(bundle);
                //context.startActivity(intent);
            }
        });

        setFadeAnimation(viewHolder.itemView);

    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1);
        view.startAnimation(anim);
    }
}
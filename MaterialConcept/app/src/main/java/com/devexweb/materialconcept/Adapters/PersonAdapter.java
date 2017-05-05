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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devexweb.materialconcept.DetailNews;
import com.devexweb.materialconcept.Objects.Person;
import com.devexweb.materialconcept.PersonDetail;
import com.devexweb.materialconcept.R;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{

    List<Person> items = new ArrayList<>();

    private final Context context;

    public PersonAdapter(List<Person> items, Context context){
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout container;
        TextView name;
        TextView role;
        ImageView image;
        private PersonAdapter root = null;

        public ViewHolder(View v, PersonAdapter root) {
            super(v);

            container = (RelativeLayout) v.findViewById(R.id.container);
            name = (TextView) v.findViewById(R.id.name);
            role = (TextView) v.findViewById(R.id.role);
            image = (ImageView) v.findViewById(R.id.image);

            this.root = root;
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;

        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_person, viewGroup, false);
        return new PersonAdapter.ViewHolder(v, this);

    }

    @Override
    public void onBindViewHolder(final PersonAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.name.setText(items.get(i).getName());
        viewHolder.role.setText(items.get(i).getRole());

        Glide
                .with(context)
                .load(items.get(i).getImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .crossFade()
                .into(viewHolder.image);

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonDetail.class);
                Bundle bundle = new Bundle();

                bundle.putString("NAME", items.get(i).getName());
                bundle.putString("ROLE", items.get(i).getRole());
                bundle.putString("IMAGE", items.get(i).getImage());
                bundle.putString("EMAIL", items.get(i).getEmail());

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
}

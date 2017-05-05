package com.jenjfood.jfood.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jenjfood.jfood.CategoryFilter;
import com.jenjfood.jfood.R;
import com.jenjfood.jfood.ViewImage;
import com.jenjfood.jfood.objects.CategoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 29/04/16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    List<CategoryItem> items = new ArrayList<>();

    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView name;
        public ImageView color;
        public TextView newcard;
        public RelativeLayout launcher;
        private CategoryAdapter padre = null;

        public ViewHolder(View v, CategoryAdapter padre) {
            super(v);

            this.padre = padre;

            name = (TextView) v.findViewById(R.id.name);
            color = (ImageView) v.findViewById(R.id.imageCirlce);
            newcard = (TextView) v.findViewById(R.id.items);
            launcher = (RelativeLayout) v.findViewById(R.id.launcher);

        }
    }

    public CategoryAdapter(List<CategoryItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_item, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        //viewHolder.newcard.setVisibility(View.GONE);

        final int item = i;

        viewHolder.newcard.setText(items.get(i).getQuantity()+"");

        viewHolder.name.setText(items.get(i).getName());
        String UrlCategory = "http://manuelgg.com/Jfood/images/categories/"+items.get(i).getId();

        Glide.with(context)
                .load(UrlCategory)
                .centerCrop()
                .crossFade()
                .into(viewHolder.color);

        viewHolder.launcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CategoryFilter.class);
                Bundle bundle =new Bundle();
                bundle.putString("QUANTITY", items.get(item).getQuantity()+"");
                bundle.putString("ID", items.get(item).getId()+"");
                bundle.putString("NAME", items.get(item).getName());
                intent.putExtras(bundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, viewHolder.color, viewHolder.color.getTransitionName());
                context.startActivity(intent, options.toBundle());
            }
        });
    }
}

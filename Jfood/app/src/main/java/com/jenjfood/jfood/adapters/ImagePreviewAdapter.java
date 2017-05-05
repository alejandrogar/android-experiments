package com.jenjfood.jfood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jenjfood.jfood.R;
import com.jenjfood.jfood.objects.CategoryItem;
import com.jenjfood.jfood.objects.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 29/04/16.
 */
public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder>{

    List<ImageItem> items = new ArrayList<>();

    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public ImageView image;

        private ImagePreviewAdapter padre = null;

        public ViewHolder(View v, ImagePreviewAdapter padre) {
            super(v);

            this.padre = padre;

            image = (ImageView) v.findViewById(R.id.image_preview_item);

        }
    }

    public ImagePreviewAdapter(List<ImageItem> items, Context context) {
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
                .inflate(R.layout.category_image_item , viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        String UrlCategory = "http://manuelgg.com/Jfood/images/categories/"+items.get(i).geturl();
        Glide.with(context)
                .load(UrlCategory)
                .centerCrop()
                .crossFade()
                .into(viewHolder.image);

        final int item = i;

    }
}

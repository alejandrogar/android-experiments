package com.jenjfood.jfood.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jenjfood.jfood.DetailActivity;
import com.jenjfood.jfood.MainActivity;
import com.jenjfood.jfood.R;
import com.jenjfood.jfood.utils.Utils;
import com.jenjfood.jfood.objects.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by root on 5/04/16.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    List<Recipe> items = new ArrayList<>();

    private final Context context;

    public RecipeAdapter(List<Recipe> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView img;
        public TextView name;
        public TextView time;
        public TextView portions;
        public ImageView cat;
        public RelativeLayout to;

        private RecipeAdapter padre = null;

        public ViewHolder(View v, RecipeAdapter padre) {
            super(v);

            this.padre = padre;

            img = (ImageView) v.findViewById(R.id.main_picture);
            name = (TextView) v.findViewById(R.id.name_recipe);
            time = (TextView) v.findViewById(R.id.time);
            portions = (TextView) v.findViewById(R.id.porcion);
            cat = (ImageView) v.findViewById(R.id.cat);
            to = (RelativeLayout) v.findViewById(R.id.trigger);

        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;

        String type = Utils.readSharedSetting(context, MainActivity.PREF_USER_VIEW_RECIPE, "true");

        if(Objects.equals(type, "list")){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item_recipe, viewGroup, false);
            return new ViewHolder(v,this);

        }else{
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_item_recipe, viewGroup, false);
            return new ViewHolder(v,this);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.name.setText(items.get(i).getName());
        viewHolder.time.setText(items.get(i).getPTime());
        viewHolder.portions.setText(items.get(i).getPortions()+"");

        String UrlCat = "http://manuelgg.com/Jfood/images/categories/"+items.get(i).getCategory();

        Glide.with(context)
                .load(UrlCat)
                .centerCrop()
                .crossFade()
                .into(viewHolder.cat);

        final int item = i;

       /* Glide.with(context)
                .load(items.get(i).getMainPicture())
                .centerCrop()
                .crossFade()
                .into(viewHolder.img);*/


        Random rand = new Random();
        int image = rand.nextInt((MainActivity.BackHeaders.length) - 1);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://jfood-d66a8.appspot.com/recipes/type/");

        String typeImage = items.get(i).getType()+"/"+items.get(i).getMainPicture();

        storageRef.child(typeImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .crossFade()
                        .into(viewHolder.img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(context)
                        .load(R.drawable.ic_action_file_cloud_off)
                        .centerCrop()
                        .crossFade()
                        .into(viewHolder.img);

            }
        });

        final int SIZE = getItemCount();

        viewHolder.to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle =new Bundle();

                bundle.putString("NAME",items.get(item).getName());
                bundle.putString("TYPE",items.get(item).getType());
                bundle.putString("DATE",items.get(item).getDate());
                bundle.putString("MAIN_PICTURE",items.get(item).getMainPicture());
                bundle.putString("PORTIONS", items.get(item).getPortions());
                bundle.putString("P_TIME",items.get(item).getPTime());
                bundle.putStringArray("STEPS",items.get(item).getSteps().split("#space#"));
                bundle.putString("CATEGORY",items.get(item).getCategory());
                bundle.putStringArray("INGREDIENTS", items.get(item).getIngredients().split("#space#"));
                bundle.putStringArray("IMAGES_GALLERY", items.get(item).getgPictures().split("#space#"));

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, viewHolder.img, viewHolder.img.getTransitionName());

                intent.putExtras(bundle);
                context.startActivity(intent, options.toBundle());

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
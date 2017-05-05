package com.jenjfood.jfood.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jenjfood.jfood.DetailActivity;
import com.jenjfood.jfood.MainActivity;
import com.jenjfood.jfood.R;
import com.jenjfood.jfood.objects.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GridAdapter extends BaseAdapter {
    private Context context;
    List<Recipe> items = new ArrayList<>();

    public GridAdapter(List<Recipe> items,Context context) {
        this.context = context;
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Recipe getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId_();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_grid, viewGroup, false);
        }

        final ImageView imagenRecipe = (ImageView) view.findViewById(R.id.imagen_Recipe);
        TextView nombreRecipe = (TextView) view.findViewById(R.id.nombre_Recipe);

        final Recipe item = getItem(i);

        /*Glide.with(imagenRecipe.getContext())
                .load(item.getMainPicture())
                .into(imagenRecipe);*/

      /*  Glide.with(context)
                .load(item.getMainPicture())
                .centerCrop()
                .crossFade()
                .into(imagenRecipe);*/

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://jfood-d66a8.appspot.com/recipes/type/");


        String typeImage = items.get(i).getType()+"/"+items.get(i).getMainPicture();

        Random rand = new Random();
        int image = rand.nextInt((MainActivity.BackHeaders.length) - 1);


        storageRef.child(typeImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .crossFade()
                        .into(imagenRecipe);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(context)
                        .load(R.drawable.ic_action_file_cloud_off)
                        .centerCrop()
                        .crossFade()
                        .into(imagenRecipe);

            }
        });

        nombreRecipe.setText(item.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(context, DetailActivity.class);
            Bundle bundle =new Bundle();

            bundle.putString("NAME",items.get(i).getName());
            bundle.putString("TYPE",items.get(i).getType());
            bundle.putString("DATE",items.get(i).getDate());
            bundle.putString("MAIN_PICTURE",items.get(i).getMainPicture());
            bundle.putString("PORTIONS", items.get(i).getPortions());
            bundle.putString("P_TIME",items.get(i).getPTime());
            bundle.putStringArray("STEPS",items.get(i).getSteps().split("#space#"));
            bundle.putString("CATEGORY",items.get(i).getCategory());
            bundle.putStringArray("INGREDIENTS", items.get(i).getIngredients().split("#space#"));
            bundle.putStringArray("IMAGES_GALLERY", items.get(i).getgPictures().split("#space#"));

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context, imagenRecipe, imagenRecipe.getTransitionName());

            intent.putExtras(bundle);
            context.startActivity(intent, options.toBundle());

            }
        });

        return view;
    }

}
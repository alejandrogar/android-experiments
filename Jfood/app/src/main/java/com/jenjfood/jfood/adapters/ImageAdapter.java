package com.jenjfood.jfood.adapters;

/**
 * Created by root on 16/04/16.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jenjfood.jfood.R;
import com.jenjfood.jfood.ViewImage;

import java.util.ArrayList;


public class ImageAdapter extends PagerAdapter {

    //private ArrayList<Integer> IMAGES;
    private ArrayList<String> URLIMAGES;
    private LayoutInflater inflater;
    private Context context;

    public ImageAdapter(Context context,ArrayList<String> URLIMAGES) {
        this.context = context;
        this.URLIMAGES=URLIMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return URLIMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slide_item_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        Glide.with(context)
                .load(URLIMAGES.get(position))
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewImage.class);
                Bundle bundle =new Bundle();
                bundle.putString("URLIMAGE", URLIMAGES.get(position) );
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        //imageView.setImageResource(URLIMAGES.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
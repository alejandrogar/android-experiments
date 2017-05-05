package com.jenjfood.jfood;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewImage  extends Activity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);


        Bundle bundle = this.getIntent().getExtras();

        String img = bundle.getString("URLIMAGE");
        imageView = (ImageView) findViewById(R.id.main_picture);

        Glide.with(this)
                .load(img)
                .into(imageView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.devexweb.materialconcept;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PersonDetail extends AppCompatActivity {

    @Bind(R.id.name) TextView name;
    @Bind(R.id.role) TextView role;
    @Bind(R.id.image) ImageView image;
    @Bind(R.id.email) TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final Bundle bundle = this.getIntent().getExtras();

        Glide
            .with(this)
            .load(bundle.getString("IMAGE"))
            .centerCrop()
            //.placeholder(R.drawable.loading_spinner)
            .crossFade()
            .into(image);

        name.setText(bundle.getString("NAME"));
        role.setText(bundle.getString("ROLE"));
        email.setText(bundle.getString("EMAIL"));

    }
}

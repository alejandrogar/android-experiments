package com.jenjfood.jfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenjfood.jfood.adapters.RecipeAdapter;
import com.jenjfood.jfood.objects.Recipe;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilter extends AppCompatActivity {

    List<Recipe> items;
    RecipeAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    SwipeRefreshLayout refresh;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadein);
        setContentView(R.layout.activity_category_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        items = new ArrayList<>();

        Bundle bundle = this.getIntent().getExtras();
        int quantity = Integer.valueOf(bundle.getString("QUANTITY"));

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(bundle.getString("NAME"));
        }

        ImageView category_image_toolbar = (ImageView) findViewById(R.id.image_category);

        Glide.with(CategoryFilter.this)
                .load("http://manuelgg.com/Jfood/images/categories/"+bundle.getString("ID"))
                .centerCrop()
                .crossFade()
                .into(category_image_toolbar);

        TextView title_app = (TextView) findViewById(R.id.title_app);
        title_app.setText(bundle.getString("NAME"));


        recycler = (RecyclerView) findViewById(R.id.drinkPL);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(CategoryFilter.this);
        recycler.setLayoutManager(lManager);
        refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        loader = (ProgressBar) findViewById(R.id.loader);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference filterRecipeRef = database.getReference("recipes/type");
        filterRecipeRef.keepSynced(true);

        Query queryRef = filterRecipeRef.child("food").orderByChild("category").equalTo(bundle.getString("ID"));
        getData(queryRef);

        queryRef = filterRecipeRef.child("drinks").orderByChild("category").equalTo(bundle.getString("ID"));
        getData(queryRef);

        queryRef = filterRecipeRef.child("desserts").orderByChild("category").equalTo(bundle.getString("ID"));
        getData(queryRef);

        refresh.setColorSchemeResources(R.color.colorPrimary);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Has añadido una estrella a esa categoría", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });
    }

    public void getData(Query filterRecipeRef){
        /*
            Leer los datos una sola vez
        */
        filterRecipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot recipesnapshot : snapshot.getChildren()) {
                    Recipe recipe = recipesnapshot.getValue(Recipe.class);
                    items.add(new Recipe(recipe.getType(),recipe.getName(),recipe.getDate(),recipe.getMainPicture(),recipe.getPortions(),recipe.getPTime(),recipe.getSteps(),recipe.getCategory(),recipe.getIngredients(),recipe.getgPictures(),recipe.getAuthor()));
                }

                adapter = new RecipeAdapter(items, CategoryFilter.this);
                recycler.setAdapter(adapter);
                refresh.setRefreshing(false);
                loader.setVisibility(View.GONE);
                Log.w("FOOD DATA", "Data was received");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("JFOOD DATA", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category_filter, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

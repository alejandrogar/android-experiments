package com.jenjfood.jfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenjfood.jfood.adapters.CategoryAdapter;
import com.jenjfood.jfood.objects.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity {

    List<CategoryItem> items = new ArrayList<>();
    CategoryAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recycler = (RecyclerView) findViewById(R.id.categoriesRecycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(Categories.this);
        recycler.setLayoutManager(lManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categiesRef = database.getReference("recipes/categories");
        categiesRef.keepSynced(true);

        categiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot recipesnapshot : snapshot.getChildren()) {
                    CategoryItem category = recipesnapshot.getValue(CategoryItem.class);
                    items.add(new CategoryItem(category.getId(), category.getName(), category.getQuantity()));
                    //Log.i("quantity of"+category.getName(),category.getQuantity()+"");
                }
                adapter = new CategoryAdapter(items, Categories.this);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FOOD DATA", "Failed to read value.", error.toException());
            }
        });
    }
}

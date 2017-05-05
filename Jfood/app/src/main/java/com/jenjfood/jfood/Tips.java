package com.jenjfood.jfood;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenjfood.jfood.adapters.TipAdapter;
import com.jenjfood.jfood.objects.Tip;

import java.util.ArrayList;
import java.util.List;

public class Tips extends AppCompatActivity {

    List<Tip> items;
    TipAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    SwipeRefreshLayout refresh;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
        recycler.setNestedScrollingEnabled(false);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        loader = (ProgressBar) findViewById(R.id.loader);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference TipRef = database.getReference("recipes/tips");
        TipRef.keepSynced(true);

        getData(TipRef);
        getDataChaged(TipRef);

        refresh.setColorSchemeResources(R.color.colorPrimary);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(TipRef);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tip AddTip = new Tip("Lorem ipsum dolor sit amet, consectetur adipisicing elit. Odio nemo dolore quia maiores vero id culpa!");
                TipRef.push().setValue(AddTip);
            }
        });
    }

    public void getData(Query foodRef){
        /*
            Leer los datos una sola vez
        */
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                items = new ArrayList<>();
                for (DataSnapshot tipsnapshot : snapshot.getChildren()) {
                    Tip tip= tipsnapshot.getValue(Tip.class);
                    items.add(new Tip(tip.getBody()));
                }
                adapter = new TipAdapter(items, getApplicationContext());
                recycler.setAdapter(adapter);
                refresh.setRefreshing(false);
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("JFOOD DATA", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    public void getDataChaged(DatabaseReference foodRef){
        /*
            Detectar cambio en la bases de datos y actualizar en tiempo real  (Opcional)
        */
        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                items = new ArrayList<>();
                for (DataSnapshot tipsnapshot : snapshot.getChildren()) {
                    Tip tip= tipsnapshot.getValue(Tip.class);
                    items.add(new Tip(tip.getBody()));
                }
                adapter = new TipAdapter(items, getApplicationContext());
                recycler.setAdapter(adapter);
                refresh.setRefreshing(false);
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("JFOOD DATA", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

}

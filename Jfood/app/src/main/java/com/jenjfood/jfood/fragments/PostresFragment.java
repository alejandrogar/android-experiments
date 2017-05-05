package com.jenjfood.jfood.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenjfood.jfood.MainActivity;
import com.jenjfood.jfood.R;
import com.jenjfood.jfood.adapters.GridAdapter;
import com.jenjfood.jfood.adapters.RecipeAdapter;
import com.jenjfood.jfood.objects.Recipe;
import com.jenjfood.jfood.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostresFragment extends Fragment {

    List<Recipe> items;

    RecipeAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    SwipeRefreshLayout refresh;
    ProgressBar loader;

    String type;

    private GridView gridView;
    private GridAdapter adaptador;

    public PostresFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_postres, container, false);
        type = Utils.readSharedSetting(getContext(), MainActivity.PREF_USER_VIEW_RECIPE, "true");

        recycler = (RecyclerView) view.findViewById(R.id.dessertPL);
        recycler.setHasFixedSize(true);
        gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setNestedScrollingEnabled(true);

        if(Objects.equals(type, "grid")){
            recycler.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }else{
            recycler.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            lManager = new LinearLayoutManager(getActivity());
            recycler.setLayoutManager(lManager);
            recycler.setNestedScrollingEnabled(false);
        }

        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        loader = (ProgressBar) view.findViewById(R.id.loader);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dessertsRef = database.getReference("recipes/type/desserts");
        dessertsRef.keepSynced(true);

        getData(dessertsRef);

        refresh.setColorSchemeResources( R.color.colorPrimary);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(dessertsRef);
            }
        });

        return view;
    }

    public void getData(DatabaseReference dessertsRef){
        /*
            Leer los datos una sola vez
        */
        dessertsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                items = new ArrayList<>();
                for (DataSnapshot recipesnapshot : snapshot.getChildren()) {
                    Recipe recipe = recipesnapshot.getValue(Recipe.class);
                    items.add(new Recipe(recipe.getType(),recipe.getName(),recipe.getDate(),recipe.getMainPicture(),recipe.getPortions(),recipe.getPTime(),recipe.getSteps(),recipe.getCategory(),recipe.getIngredients(),recipe.getgPictures(),recipe.getAuthor()));
                }
                if(Objects.equals(type, "grid")){
                    adaptador = new GridAdapter(items,getContext());
                    gridView.setAdapter(adaptador);
                }else{
                    adapter = new RecipeAdapter(items, getContext());
                    recycler.setAdapter(adapter);

                }
                refresh.setRefreshing(false);
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("JFOOD DATA", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    public void getDataChaged(DatabaseReference dessertsRef){
        /*
            Detectar cambio en la bases de datos y actualizar en tiempo real  (Opcional)
        */
        dessertsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("FOOD DATA", "Failed to read value.", error.toException());
            }
        });
    }
    
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

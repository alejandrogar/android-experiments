package com.devexweb.materialconcept.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.devexweb.materialconcept.Adapters.ActivityAdapter;
import com.devexweb.materialconcept.Adapters.NewsAdapter;
import com.devexweb.materialconcept.DetailNews;
import com.devexweb.materialconcept.Objects.NewsObj;
import com.devexweb.materialconcept.R;

import java.util.ArrayList;

public class News extends Fragment {

    String param;

    ArrayList<NewsObj> items;
    RecyclerView.LayoutManager lManager;
    RecyclerView recycler;
    NewsAdapter adapter;

    public News() {
    }
    public static News newInstance(String... params) {
        News fragment = new News();
        Bundle args = new Bundle();

        /* Obtener parametros en el fragmento*/
        args.putString("KEY", params[0]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getString("KEY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        RelativeLayout lastest = (RelativeLayout) view.findViewById(R.id.lastest_news);

        lastest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailNews.class);
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", "El mejor juego VR Shooter del año.");
                bundle.putString("DATE", "Viernes, 15 de Abril 2016");

                bundle.putInt("ITEM", 6);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        recycler = (RecyclerView) view.findViewById(R.id.recycler_news);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(lManager);
        items = new ArrayList<>();

        // String title, int image, long date
        items.add(new NewsObj("El Manuscrito Voynich, el enigmático libro que nadie puede leer, sale a la venta", R.drawable.manuscrito_voynich, 1478044800));
        items.add(new NewsObj("La NASA llevó a cabo un evento de Facebook en vivo hoy para mostrar su robot humanoide", R.drawable.robonaut, 1475539200));
        items.add(new NewsObj("Hezbolá causa polémica con el desfile militar en Siria", R.drawable.syria, 1444003200));
        items.add(new NewsObj("Trump nombró a un senador racista como ministro de Justicia", R.drawable.trump, 1476057600));
        items.add(new NewsObj("Mercedes y Red Bull pueden ser un peligro en 2017", R.drawable.f1, 1477958400));
        items.add(new NewsObj("TECH Google Assistant y Google Now ¿Son lo mismo?", R.drawable.google_nw, 1477699200));

        adapter = new NewsAdapter(items, getContext());
        recycler.setAdapter(adapter);

        return view;
    }

}

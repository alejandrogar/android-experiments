package com.devexweb.materialconcept.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devexweb.materialconcept.Adapters.ActivityAdapter;
import com.devexweb.materialconcept.Objects.ActivityObj;
import com.devexweb.materialconcept.R;

import java.util.ArrayList;
import java.util.List;

public class Activity extends Fragment {

    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    ActivityAdapter adapter;

    String param;

    List<ActivityObj> items;

    public Activity() {
    }
    public static Activity newInstance(String... params) {
        Activity fragment = new Activity();
        Bundle args = new Bundle();

        /* Obtener parametros en el fragmentos*/
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

        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.recycler_activity);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        items = new ArrayList<>();

        items.add(new ActivityObj(R.drawable.ic_action_calendar,"EVENTO", R.drawable.ic_action_whatch, "Lorem ipsum dolor sit amet, consectetur adipsicing elit.", 0, 1479494743, "time", "place"));
        items.add(new ActivityObj(R.drawable.ic_action_calendar,"NOTICIA", 0, "Este es el Smart ForTwo más poderoso en el mundo.", R.drawable.smart, 1479484521, "time", "place"));
        items.add(new ActivityObj(R.drawable.ic_action_group,"EVENTO", R.drawable.ic_action_camera, "Lorem ipsum dolor sit amet, consectetur adipsicing elit.", 0, 1479340800, "time", "place"));
        items.add(new ActivityObj(R.drawable.ic_action_calendar,"NOTICIA", 0, "Descubrí Google Now y aprendé a usarlo", R.drawable.google_nw, 1452556800, "time", "place"));

        adapter = new ActivityAdapter(items, getContext());
        recycler.setAdapter(adapter);

        return view;
    }
}

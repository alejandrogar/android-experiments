package com.devexweb.materialconcept.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.devexweb.materialconcept.Adapters.ActivityDiaryAdapter;
import com.devexweb.materialconcept.Objects.ActivityDiary;
import com.devexweb.materialconcept.R;

import java.util.ArrayList;


public class Diary extends Fragment {

    String param;

    ArrayList<ActivityDiary> items;
    RecyclerView.LayoutManager lManager;
    RecyclerView recycler;
    ActivityDiaryAdapter adapter;
    Button lun;
    public Diary() {
    }
    public static Diary newInstance(String... params) {
        Diary fragment = new Diary();
        Bundle args = new Bundle();

        /* Obtener parametros en e fragmentos*/
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

        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.recycler_diary);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(lManager);
        items = new ArrayList<>();

        items.add(new ActivityDiary("Eva green", "Introducción al desarrollo en Android", "09:30"));
        items.add(new ActivityDiary("Victoria Jensen", "La importanncia del uso de Software libre", "10:30"));
        items.add(new ActivityDiary("Thomas Charles", "El futuro de los coches autónomos", "11:00"));
        items.add(new ActivityDiary("Ariana Cooper", "A que edad es conveniente un smartphone en la infancia?", "12:45"));


        adapter = new ActivityDiaryAdapter(items, getContext());
        recycler.setAdapter(adapter);

        return view;
    }
}

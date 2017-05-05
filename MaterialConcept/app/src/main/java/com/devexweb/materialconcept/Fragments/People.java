package com.devexweb.materialconcept.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devexweb.materialconcept.Adapters.PersonAdapter;
import com.devexweb.materialconcept.Objects.Person;
import com.devexweb.materialconcept.R;

import java.util.ArrayList;


public class People extends Fragment {

    String param;

    ArrayList<Person> items;
    RecyclerView recycler;
    LinearLayoutManager lManager;
    PersonAdapter adapter;

    public People() {
    }
    public static People newInstance(String... params) {
        People fragment = new People();
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
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getActivity());
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(lManager);
        items = new ArrayList<>();

        items.add(new Person("Necati özberk", "Ponente", "necati.özberk@özberk.com", "https://randomuser.me/api/portraits/men/20.jpg","https://randomuser.me/api/portraits/thumb/men/20.jpg"));
        items.add(new Person("eva green", "Ponente", "eva.green@example.com", "https://randomuser.me/api/portraits/women/13.jpg",  "https://randomuser.me/api/portraits/thumb/women/13.jpg"));
        items.add(new Person("victoria jensen", "Ponente", "victoria.jensen@example.com", "https://randomuser.me/api/portraits/women/33.jpg",  "https://randomuser.me/api/portraits/thumb/women/33.jpg"));
        items.add(new Person("محمدعلی احمدی", "Ponente", "محمدعلی.احمدی@example.com", "https://randomuser.me/api/portraits/men/35.jpg",  "https://randomuser.me/api/portraits/thumb/men/35.jpg"));
        items.add(new Person("thomas charles", "Ponente", "thomas.charles@example.com", "https://randomuser.me/api/portraits/men/82.jpg",  "https://randomuser.me/api/portraits/thumb/men/82.jpg"));
        items.add(new Person("oğuzhan ekici", "Ponente", "oğuzhan.ekici@example.com", "https://randomuser.me/api/portraits/men/60.jpg",  "https://randomuser.me/api/portraits/thumb/men/60.jpg"));
        items.add(new Person("luka fleury", "Ponente", "luka.fleury@example.com", "https://randomuser.me/api/portraits/men/29.jpg",  "https://randomuser.me/api/portraits/thumb/men/29.jpg"));
        items.add(new Person("ariana cooper", "Ponente", "ariana.cooper@example.com", "https://randomuser.me/api/portraits/women/16.jpg",  "https://randomuser.me/api/portraits/thumb/women/16.jpg"));
        items.add(new Person("fatima vicente", "Ponente", "fatima.vicente@example.com", "https://randomuser.me/api/portraits/women/18.jpg",  "https://randomuser.me/api/portraits/thumb/women/18.jpg"));
        items.add(new Person("lyam roussel", "Ponente", "lyam.roussel@example.com", "https://randomuser.me/api/portraits/men/1.jpg",  "https://randomuser.me/api/portraits/thumb/men/1.jpg"));

        adapter = new PersonAdapter(items, getContext());
        recycler.setAdapter(adapter);

        return view;
    }
}

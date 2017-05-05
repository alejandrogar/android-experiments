package com.devexweb.materialconcept.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devexweb.materialconcept.R;


public class Places extends Fragment{

    String param;

    public Places() {
    }
    public static Places newInstance(String... params) {
        Places fragment = new Places();
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

        View view = inflater.inflate(R.layout.fragment_places, container, false);


        return view;
    }

}

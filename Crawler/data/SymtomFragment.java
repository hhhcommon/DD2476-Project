2
https://raw.githubusercontent.com/RzTutul/Covid-19/master/app/src/main/java/com/example/covid19/SymtomFragment.java
package com.example.covid19;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SymtomFragment extends Fragment {


    public SymtomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_symtom, container, false);
    }

}
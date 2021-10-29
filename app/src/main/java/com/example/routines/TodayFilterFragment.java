package com.example.routines;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TodayFilterFragment extends Fragment {

    private View rootView;
    AppCompatRadioButton switchHabits;
    AppCompatRadioButton switchTodayHabits;

    TextView fragmentLabel;
    TextView fragmentLine;
    ListView fragmentHabitsList;

    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseAuth myAuth;


    public TodayFilterFragment() {
        // Required empty public constructor
    }


    public static TodayFilterFragment newInstance() {
        TodayFilterFragment fragment = new TodayFilterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_today_filter, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentHabitsList = (ListView) rootView.findViewById(R.id.fragment_habitList);
        db = FirebaseFirestore.getInstance();
        //collectionReference = db.collection("")
    }





}
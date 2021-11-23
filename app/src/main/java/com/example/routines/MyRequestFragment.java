package com.example.routines;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRequestFragment extends Fragment implements HabitRecyclerAdapter.OnHabitClickListener {

    private View rootView;
    private  HabitRecyclerAdapter habitAdapter;
    private RecyclerView requestView;
    FrameLayout fragmentLayout;
    FirebaseFirestore db;
    String userId;
    FirebaseAuth myAuth;

    public MyRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyRequestFragment.
     */
    public static MyRequestFragment newInstance() {
        MyRequestFragment fragment = new MyRequestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_request, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onItemClick(int position) {

    }
}
package com.example.routines;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodayFilterFragment extends Fragment {
    final String TAG = "Today filter fragment";
    private View rootView;
    ListView fragmentHabitsList;
    String dayWeek;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseAuth myAuth;
    String userId;

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
        ArrayList<Habit> habitsList = new ArrayList<Habit>();
        ArrayAdapter<Habit> habitAdapter = new HabitList(getContext(), habitsList);
        fragmentHabitsList.setAdapter(habitAdapter);
        findWeekday();
        Log.d("Week day", dayWeek);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        collectionReference = db.collection("Habits")
                                .document(userId)
                                .collection("Habits");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collectionReference.document(document.getId())
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if(value != null && value.exists()){
                                            ArrayList<String> frequency = (ArrayList<String>) value.getData().get("Frequency");
                                            if(frequency.contains(dayWeek)||frequency.contains("null")){
                                                habitsList.add(new Habit( (String)value.getData().get("Habit Name"),
                                                        (String)value.getData().get("Habit Reason"),
                                                        (String)value.getData().get("Start Date")));
                                                habitAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });

                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void findWeekday(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                dayWeek = "Sunday";
                break;
            case Calendar.MONDAY:
                dayWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayWeek = "Saturday";
                break;
        }


    }






}
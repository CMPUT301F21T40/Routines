package com.example.routines;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

/**
 * 
 * This is a filter fragment and it will filter the user's habits that need to be finished today
 * This implements HabitRecyclerAdapter.OnHabitClickListener
 * @author Shanshan Wei/swei3
 * @see ViewHabitActivity
 * @see HomeActivity
 */

public class TodayFilterFragment extends Fragment implements HabitRecyclerAdapter.OnHabitClickListener{
    final String TAG = "Today filter fragment";
    private View rootView;
    ListView fragmentHabitsList;
    String dayWeek;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseAuth myAuth;
    String userId;
    private ArrayList<String> habitIdList;

    private  HabitRecyclerAdapter habitAdapter;
    private RecyclerView habitView;
    private ArrayList<Habit> habitDataList;

    /**
     * A empty constructor that can be used to initialized the fragment
     * @author Shanshan Wei/swei3
     */
    public TodayFilterFragment() {
        // Required empty public constructor
    }

    /**
     * It is used to create an instance of fragment
     * @author Shanshan Wei/swei3
     * @return HomeFragment
     */
    public static TodayFilterFragment newInstance() {
        TodayFilterFragment fragment = new TodayFilterFragment();
        return fragment;
    }

    /**
     * It inflated the fragment
     * @author Shanshan Wei/swei3
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return rootView
     */
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
        initializeView();

        findWeekday();
        Log.d("Week day", dayWeek);

        /**
         * This lines filter the today habits from all habits of the user.
         * @author Shanshan Wei/swei3
         */
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
                                                habitDataList.add(new Habit( (String)value.getData().get("Habit Name"),
                                                        (String)value.getData().get("Habit Reason"),
                                                        (String)value.getData().get("Start Date"),
                                                        (long)value.getData().get("Progress")));
                                                habitIdList.add((String) document.getId() );
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

    /**
     * This method finds which weekday today is
     * @author Shanshan Wei/swei3
     */
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

    /**
     * This will initialize views for all components like textview, buttons
     * @author Shanshan Wei/swei3
     */
    public void initializeView(){
        habitView = rootView.findViewById(R.id.fragment_habitList);
        habitDataList = new ArrayList<Habit>();
        habitIdList = new ArrayList<>();
        habitAdapter = new HabitRecyclerAdapter(habitDataList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        habitView.setLayoutManager(layoutManager);
        habitView.setAdapter(habitAdapter);
    }


    /**
     * This methods override HabitRecyclerAdapter.OnHabitClickListener
     * If the user clicks on the item of RecyclerView, it will direct the user to ViewHabitActivity
     * @author Shanshan Wei/swei3
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ViewHabitActivity.class);
        String habitId = habitIdList.get(position);
        intent.putExtra("habitId", habitId);
        startActivity(intent);
    }
}
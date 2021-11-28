package com.example.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchHabitsFragment extends Fragment implements HabitRecyclerAdapter.OnHabitClickListener{
    private View rootView;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseAuth myAuth;
    String userId;
    String actualUserId;
    private ArrayList<String> habitIdList;

    private  HabitRecyclerAdapter habitAdapter;
    private RecyclerView habitView;
    private ArrayList<Habit> habitDataList;


    public SearchHabitsFragment(String userId) {
        this.userId = userId;
        // Required empty public constructor
    }


    public static SearchHabitsFragment newInstance(String userId) {
        SearchHabitsFragment fragment = new SearchHabitsFragment(userId);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search_profile_habits, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();

        db = FirebaseFirestore.getInstance();
        actualUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        collectionReference = db.collection("Habits")
                .document(userId)
                .collection("Habits");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", "Got a document-----------");
                        collectionReference.document(document.getId())
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if(value != null && value.exists()){
                                            String privacy = (String) value.getData().get("Privacy");
                                            if (privacy != null && privacy.equals("Public")) {
                                                Log.d("TAG", "A valid habit -----------");
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

                        Log.d("TAG", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ViewHabitActivity.class);
        String habitId = habitIdList.get(position);
        intent.putExtra("sameUser", false);
        intent.putExtra("habitId", habitId);
        intent.putExtra("userId", userId);
        Log.d("Intent habit id", habitId);
        startActivity(intent);
    }

    public void initializeView(){
        habitView = rootView.findViewById(R.id.fragment_habit_search_list);
        habitDataList = new ArrayList<>();
        habitIdList = new ArrayList<>();
        habitAdapter = new HabitRecyclerAdapter(habitDataList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        habitView.setLayoutManager(layoutManager);
        habitView.setAdapter(habitAdapter);
    }
}

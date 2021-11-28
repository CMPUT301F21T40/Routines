package com.example.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class FollowingFilterFragment extends Fragment implements HabitRecyclerAdapter.OnHabitClickListener {
    final String TAG = "Following filter fragment";
    private View rootView;
    ListView fragmentHabitsList;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseAuth myAuth;
    String userId;
    String followingId;
    private ArrayList<String> habitIdList;
    ArrayList<ArrayList<String>> idList;
    private ArrayList<String> followers;

    private  HabitRecyclerAdapter habitAdapter;
    private RecyclerView habitView;
    private ArrayList<Habit> habitDataList;

    public FollowingFilterFragment() {

    }

    public static FollowingFilterFragment newInstance() {
        FollowingFilterFragment fragment = new FollowingFilterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_following_filter, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        collectionReference = db.collection("Habits");
        findFollowers(userId);


    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ViewHabitActivity.class);
        String habitId = habitIdList.get(position);
        findHabitUserId(habitId);
        ArrayList<String> list = idList.get(position);
        String followingId = list.get(1);

        Log.d("TAG", "RIGHT BEFORE INTENT CALL - " + followingId);
        intent.putExtra("userId", followingId);
        intent.putExtra("sameUser", false);
        intent.putExtra("habitId", habitId);
        startActivity(intent);

    }

    public void initializeView(){
        Log.d("TAG", "Init View -----------------");
        habitView = rootView.findViewById(R.id.fragment_habitList);
        habitDataList = new ArrayList<Habit>();
        habitIdList = new ArrayList<>();
        idList = new ArrayList<>();
        followers = new ArrayList<>();
        habitAdapter = new HabitRecyclerAdapter(habitDataList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        habitView.setLayoutManager(layoutManager);
        habitView.setAdapter(habitAdapter);
    }

    public void findHabitUserId(String habitId) {
        for (String follower : followers) {
            Log.d("TAG", follower + "------------");
            db.collection("Habits")
                    .document(follower)
                    .collection("Habits")
                    .document(habitId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "Task is successful-----------");
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "Document exists -----------");
                                    followingId = follower;
                                    Log.d("TAG", followingId + " - following ID -----");
                                    return;
                                }
                            }
                        }
                    });

        }

    }

    public void findFollowers(String userId) {
        db.collection("Notification")
                .whereEqualTo("Sender", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String status = (String) document.getData().get("Status");
                                if (status.equals("accepted")) {
                                    String followerId = (String) document.getData().get("Receiver");
                                    followers.add(followerId);
                                }
                            }
                        }
                        Integer size = followers.size();
                        setHabits();
                    }
                });
    }

    public void setHabits() {
        for (String follower : followers) {
            collectionReference
                    .document(follower)
                    .collection("Habits")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    collectionReference.document(follower).collection("Habits").document(document.getId())
                                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if (value != null && value.exists()) {
                                                        String privacy = (String) value.getData().get("Privacy");
                                                        if (privacy.equals("Public")) {
                                                            habitDataList.add(new Habit((String) value.getData().get("Habit Name"),
                                                                    (String) value.getData().get("Habit Reason"),
                                                                    (String) value.getData().get("Start Date"),
                                                                    (long) value.getData().get("Progress")));
                                                            String habitId = document.getId();
                                                            String userId = follower;
                                                            ArrayList<String> tempList = new ArrayList<>();
                                                            tempList.add(habitId);
                                                            tempList.add(userId);
                                                            idList.add(tempList);
                                                            habitIdList.add((String) document.getId());
                                                            habitAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }
                                            });

                                }
                            } else {
                            }
                        }
                    });
        }
    }

}

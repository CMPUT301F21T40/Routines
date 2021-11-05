package com.example.routines;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HabitRecyclerAdapter.OnHabitClickListener {

    private View rootView;
    private  HabitRecyclerAdapter habitAdapter;
    private RecyclerView habitView;
    private ArrayList<Habit> habitDataList;
    private ArrayList<String> habitIdList;
    FrameLayout fragmentLayout;
    FirebaseFirestore db;
    String userId;
    FirebaseAuth myAuth;

    CollectionReference habitCollection;
    DocumentReference userDocument;
    CollectionReference currentUserHabitCol;
    DocumentReference userHabitDoc;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        Get user ID
        myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        userId = user.getUid();

//        Create a separate Habits collection
        db = FirebaseFirestore.getInstance();
        habitCollection = db.collection("Habits");
        userDocument = habitCollection.document(userId);
//        Sub-collection of Habit under the current user
        currentUserHabitCol = userDocument.collection("Habits");



        fragmentLayout = rootView.findViewById(R.id.container);

        // creating a listview and the adapter so we can store all the habits in a list on the home screen
        habitView = rootView.findViewById(R.id.home_fragment_habitList);

        habitDataList = new ArrayList<>();
        habitIdList = new ArrayList<>();
        habitAdapter = new HabitRecyclerAdapter(habitDataList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        habitView.setLayoutManager(layoutManager);
        initHabitOrder();
        showList();
        habitView.setAdapter(habitAdapter);




//        Add habits from Firestore to local habit list

    }

    public void clear() {
        int size = habitDataList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                habitDataList.remove(0);
            }
            habitAdapter.notifyItemRangeRemoved(0, size);
        }
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ViewHabitActivity.class);
        String habitId = habitIdList.get(position);
        intent.putExtra("habitId", habitId);
        startActivity(intent);
    }

    public void initHabitOrder(){
        ItemTouchHelper.Callback callback = new ReorderHabits(habitAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(habitView);
        showList();
    }

    public void showList(){
        Query currentUserCol = currentUserHabitCol.orderBy("Index", Query.Direction.ASCENDING);
        currentUserCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String habitName = (String)doc.getData().get("Habit Name");
                    String habitReason = (String)doc.getData().get("Habit Reason");
                    String habitDate = (String)doc.getData().get("Start Date");
                    ArrayList<String> frequency = (ArrayList<String>) doc.getData().get("Frequency");
                    String privacy = (String) doc.getData().get("Privacy"); // recently added
                    String id = (String) doc.getId();
                    habitIdList.add(id);
                    habitDataList.add(new Habit(habitName, habitReason, habitDate, frequency, privacy));
                    habitAdapter.notifyDataSetChanged();
                }
            }
        });
    }


}
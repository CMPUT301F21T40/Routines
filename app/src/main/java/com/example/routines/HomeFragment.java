package com.example.routines;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private View rootView;
    private ArrayAdapter<Habit> habitAdapter;
    private ArrayList<Habit> habitDataList;
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


    public static HomeFragment newInstance(String param1, String param2) {
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
        ListView habitList = rootView.findViewById(R.id.habitList);

        ArrayList<Habit> habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(getContext(), habitDataList);

        habitList.setAdapter(habitAdapter);

//        Add habits from Firestore to local habit list
        currentUserHabitCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitAdapter.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String habitName = doc.getId();
                    String habitReason = (String)doc.getData().get("Habit Reason");
                    String habitDate = (String)doc.getData().get("Start Date");
                    ArrayList<String> frequency = (ArrayList<String>) doc.getData().get("Frequency");

                    habitDataList.add(new Habit(habitName, habitReason, habitDate, frequency));
                    habitAdapter.notifyDataSetChanged();
                }
            }
        });


        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ViewHabitActivity.class);
                String habitName = habitDataList.get(i).getName();
                intent.putExtra("habitName", habitName);
                startActivity(intent);
            }
        });

    }

    // this is called from the AddHabitFragment so we can add a new Habit to the list
    /**
     * This is called when the + button is pressed and the info from the pop up fragment
     * is filled out. This will add the newly created habit into the medList
     * @param newHabit
     */
    public void onOkPressed(Habit newHabit){
        String habitName = newHabit.getName();
        String habitReason = newHabit.getReason();
        String habitDate = newHabit.getDate();
        ArrayList<String> frequencyList = (ArrayList<String>) newHabit.getFrequency();
        if (frequencyList.isEmpty()) {
            frequencyList.add("Null");
        }

//        Add new habit to Firestore
        HashMap<String, Object> data = new HashMap<>();
        data.put("Habit Name", habitName);
        data.put("Habit Reason", habitReason);
        data.put("Start Date", habitDate);
        data.put("Frequency", frequencyList);
        currentUserHabitCol.document(habitName)
                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.w("Update Successfully", "Error on writing documentation on Firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Update Failed", "Error on writing documentation on Firebase");
            }
        });

//        Add to local habit list
        habitAdapter.add(newHabit);


    }

}
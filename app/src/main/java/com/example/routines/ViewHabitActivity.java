package com.example.routines;

import static android.content.ContentValues.TAG;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewHabitActivity extends AppCompatActivity implements EditHabitFragment.OnFragmentInteractionListener , DeleteHabitFragment.OnFragmentInteractionListener{
    TextView nameView;
    TextView dateView;
    TextView reasonView;
    TextView privacyView;
    TextView frequencyView;
    String frequency = "";
    String habitName;
    String habitDate;
    String habitReason;
    ArrayList<String> habitFrequency;
    String habitPrivacy;
    String completionTime;
    String estimateCompletionTime;
    String lastCompletionTime;
    String lastModifiedDate;

    Button add;
    Button view;
    FloatingActionButton edit;
    FloatingActionButton delete;
    FirebaseAuth myAuth;
    String userId;
    String actualUserId;
    String habitId;
    long habitProgress;

    HomeFragment homeFragment;
    ViewHabitActivity viewActivity;
    Object ViewHabitActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        actualUserId = user.getUid();

        userId = (String) getIntent().getStringExtra("userId");



        habitId = getIntent().getStringExtra("habitId");
        nameView = findViewById(R.id.view_habit_name);
        reasonView = findViewById(R.id.view_habit_reason);
        dateView = findViewById(R.id.view_habit_date);
        privacyView = findViewById(R.id.view_habit_privacy);
        frequencyView = findViewById(R.id.view_habit_frequency);
        add = findViewById(R.id.add_event_button);
        view = findViewById(R.id.view_event_button);
        edit = findViewById(R.id.edit_habit_button);
        delete = findViewById(R.id.delete_habit_button);




    }

    @Override
    public void onResume() {

        super.onResume();

        Boolean sameUser = getIntent().getBooleanExtra("sameUser", true);

        if (!sameUser) {
            add.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
        } else {
            userId = actualUserId;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference habitRef = db
                .collection("Habits")
                .document(userId)
                .collection("Habits")
                .document(habitId);
        habitRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                // concatenated strings we should to this in the XML later
                                habitName = (String) document.getData().get("Habit Name");
                                habitDate = (String) document.getData().get("Start Date");
                                habitReason = (String) document.getData().get("Habit Reason");
                                habitFrequency = (ArrayList<String>) document.getData().get("Frequency");
                                habitProgress = (long)document.getData().get("Progress");
                                Collections.sort(habitFrequency,comparator);
                                habitPrivacy = (String) document.getData().get("Privacy");
                                frequency = "";
                                for (int i = 0; i < habitFrequency.size(); i++){
                                    frequency += habitFrequency.get(i) + " ";
                                }

                                hideButton(habitFrequency, add);
                                String completionTime = (String) document.getData().get("Completion Time");
                                String estimateCompletionTime = (String) document.getData().get("Estimate Completion Time");
                                String lastCompletionTime = (String) document.getData().get("Last Completion Time");
                                String lastModifiedDate = (String) document.getData().get("Last Modified Date");

                                nameView.setText(habitName);
                                reasonView.setText(habitReason);
                                dateView.setText(habitDate);
                                privacyView.setText(habitPrivacy);
                                frequencyView.setText(frequency);


                                float completionPercent = 100;
                                int totalCompletion = calculateEstimateCompletionTime(lastCompletionTime, lastModifiedDate, habitFrequency);
                                if (totalCompletion != 0)
                                    completionPercent = (float) (Integer.parseInt(completionTime)/totalCompletion);
                                Log.d("TAG", "get percentage: " + completionPercent);
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            }
                            else {
                                Log.d("TAG", "No such document ", task.getException());
                            }
                        }
                        else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                intent.putExtra("habitId", habitId);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabitActivity.this, EventListActivity.class);
                intent.putExtra("habitId", habitId);
                intent.putExtra("userId", userId);
                intent.putExtra("sameUser", sameUser);
                intent.putExtra("actualUserId", actualUserId);
                startActivity(intent);
            }
        });

        /**
         * Response when the edit button is clicked, it sends the user
         * to a page to edit a given habits details
         */
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditHabitFragment.newInstance(new Habit(habitName, habitReason, habitDate, habitFrequency, habitPrivacy, completionTime, estimateCompletionTime, lastCompletionTime, lastModifiedDate, habitProgress)).show(getSupportFragmentManager(), "EDIT_HABIT");

            }
        });

        /**
         * Response when the delete button is clicked, it sends the user
         * to a page to confirm the deletion of the selected habit
         */
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteHabitFragment().newInstance(new Habit(habitName, habitReason, habitDate, habitFrequency, habitPrivacy, completionTime, estimateCompletionTime, lastCompletionTime, lastModifiedDate, habitProgress)).show(getSupportFragmentManager(), "DELETE_HABIT");
            }
        });
    }

    /**
     * When the user clicks "CONFIRM" to delete the habit, this function runs.
     * It takes the habit as a parameter then deletes it from the firebase
     * @param habit
     * @author ipaterso
     * @see DeleteHabitFragment
     */
    public void onDeletePressed(Habit habit) {
//      Connect to Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("Habits")
                .document(userId)
                .collection("Habits");
        DocumentReference docRef = collRef.document(habitId);

//      Delete old habit
        docRef
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        deleteEvent();
//      Update the Screen
        finish();
        Intent intent = getIntent();
        intent.setClass(ViewHabitActivity.this, HomeActivity.class);
        startActivity(intent);
        ViewGroup vg = findViewById(R.id.container);
    }

    /**
     * When user clicks "OK" from EditFragment, this function runs.
     * It updates the habit that was selected with the newly provided details
     * @param habit
     * @param newHabit
     * @author ipaterso
     * @see EditHabitFragment
     */
    public void onEditPressed(Habit habit, Habit newHabit) {
        habitName = newHabit.getName();
        habitReason = newHabit.getReason();
        habitDate = newHabit.getDate();
        habitPrivacy = newHabit.getPrivacy();
        habitFrequency = (ArrayList<String>) newHabit.getFrequency();
        String completionTime = newHabit.getCompletionTime();
        String estimateCompletionTime = newHabit.getEstimateCompletionDate();
        String lastCompletionTime = newHabit.getLastCompletionTime();
        String lastModifiedDate = newHabit.getLastModifiedDate();

        if (habitFrequency.isEmpty()) {
            habitFrequency.add("Null");
        }
        if (!(habitFrequency.isEmpty()) && habitFrequency.contains("Null")) {
            habitFrequency.remove("Null");
        }

        if ((habitFrequency.size() > 1) && habitFrequency.contains("Null")) {
            habitFrequency.remove("Null");
        }

//      Connect to Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("Habits")
                .document(userId)
                .collection("Habits");
        DocumentReference docRef = collRef.document(habitId);

        nameView = findViewById(R.id.view_habit_name);
        reasonView = findViewById(R.id.view_habit_reason);
        dateView = findViewById(R.id.view_habit_date);
        privacyView = findViewById(R.id.view_habit_privacy);
        frequencyView = findViewById(R.id.view_habit_frequency);



//      Update the Details Screen
        nameView.setText(habitName);
        reasonView.setText(habitReason);
        String extendedDate = "Date: " + habitDate;
        dateView.setText(habitDate);
        privacyView.setText(habitPrivacy);
        String extendedFrequency = "";
        for (int i = 0; i < habitFrequency.size(); i++){
            extendedFrequency += habitFrequency.get(i) + " ";
        }
        frequencyView.setText(extendedFrequency);

//      Update Firebase
        docRef.update(
                "Habit Name", habitName,
                "Habit Reason", habitReason,
                "Start Date", habitDate,
                "Frequency", habitFrequency,
                "Privacy", habitPrivacy,
                "Completion Time", completionTime,
                "Estimate Completion Time", estimateCompletionTime,
                "Last Completion Time", lastCompletionTime,
                "Last Modified Time", lastModifiedDate);

        Intent intent = getIntent();
        intent.putExtra("Habit Name", habitName);
        intent.putExtra("Start Date", habitDate);
        intent.putExtra("Habit Reason", habitReason);
        intent.putExtra("Frequency", habitFrequency);
        intent.putExtra("Privacy", habitPrivacy);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Comparator<String> comparator = new Comparator<String>() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public int compare(String day1, String day2) {
            return Integer.compare(DayOfWeek.valueOf(day1.toUpperCase()).getValue(),
                    DayOfWeek.valueOf(day2.toUpperCase()).getValue());
        }
    };

    /**
     * This method delete all the events of a habit
     * @author Shanshan Wei/swei3
     */
    public void deleteEvent(){
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Events");
        collectionReference.whereEqualTo("habitId", habitId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                collectionReference
                                        .document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Delete Events", "Succeed");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Delete Events", "Failure");
                                            }
                                        });
                            }
                        }
                    }
                });

    }


    public void hideButton(ArrayList<String> habitFrequency, Button add){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        Date d = new Date();
        String currentDayOfWeek = sdf.format(d);
        if (! habitFrequency.contains(currentDayOfWeek)){
            add.setVisibility(View.GONE);
        }
    }

    public int calculateDifferInDay(String lastModifiedDate){
        int days = 0;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            long diff = new Date().getTime() - sdf.parse(lastModifiedDate).getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            days = ((int) (long) hours / 24);
            Log.i(TAG, "Date "+lastModifiedDate+" Difference From Now :"+ days + " days");
        }catch (Exception e){
            e.printStackTrace();
        }
        return days+1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int calculateEstimateCompletionTime(String completionTime, String lastModifiedDate, ArrayList<String> habitFrequency) {
        int differInDay = calculateDifferInDay(lastModifiedDate);
        int days = differInDay % 7;
        int weeks = differInDay / 7;
        ArrayList<String> fullDayOfWeek = new ArrayList<>();
        fullDayOfWeek.add("Monday");
        fullDayOfWeek.add("Tuesday");
        fullDayOfWeek.add("Wednesday");
        fullDayOfWeek.add("Thursday");
        fullDayOfWeek.add("Friday");
        fullDayOfWeek.add("Saturday");
        fullDayOfWeek.add("Sunday");

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        Date d = new Date();

        String currentDayOfWeek = sdf.format(d);
        LocalDate date = LocalDate.parse(lastModifiedDate);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String previousDayOfWeek = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        int count = 0;
        if (fullDayOfWeek.indexOf(currentDayOfWeek) < fullDayOfWeek.indexOf(previousDayOfWeek)){
            for(int previousIndex = fullDayOfWeek.indexOf(previousDayOfWeek);previousIndex<7;previousIndex++){
                if (habitFrequency.contains(fullDayOfWeek.get(previousIndex)))
                    count++;
            }
            for(int currentIndex = 0;currentIndex<=fullDayOfWeek.indexOf(currentDayOfWeek);currentIndex++){
                if (habitFrequency.contains(fullDayOfWeek.get(currentIndex)))
                    count++;
            }
        }
        else {
            if (days !=0){
                for(int i = fullDayOfWeek.indexOf(previousDayOfWeek);i<=fullDayOfWeek.indexOf(currentDayOfWeek);i++){
                    if (habitFrequency.contains(fullDayOfWeek.get(i)))
                        count++;
                }
            }
        }
        int estimateCompletionTime = Integer.parseInt(completionTime) + weeks * habitFrequency.size() + count;
        return estimateCompletionTime;
    }

}

package com.example.routines;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is mainly a container for HomeFragment and TodayFilterFragment
 * The radio button will help to switch between two fragments
 */
public class HomeActivity extends AppCompatActivity  implements AddHabitFragment.OnFragmentInteractionListener{

    private HabitRecyclerAdapter habitAdapter;
    private ArrayList<Habit> habitDataList;
    AppCompatRadioButton switchHabits;
    AppCompatRadioButton switchTodayHabits;
    AppCompatRadioButton switchFollowingHabits;
    FrameLayout fragmentLayout;
    TodayFilterFragment filterFragment;
    FollowingFilterFragment followingFragment;
    HomeFragment homeFragment;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNavigator;

    FirebaseFirestore db;
    String userId;
    FirebaseAuth myAuth;
    String habitId;

    CollectionReference habitCollection;
    DocumentReference userDocument;
    CollectionReference currentUserHabitCol;
    DocumentReference userHabitDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);

        fragmentLayout = findViewById(R.id.container);

        habitToServer();
        switchActivity();
        switchRadioButton();
        updateFragment();

        // when the + at the bottom of the screen is pressed it will call AddHabitFragment
        final FloatingActionButton addHabitButton = findViewById(R.id.addHabitButton);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddHabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
            }
        });

    }

    // this is called from the AddHabitFragment so we can add a new Habit to the list
    /**
     * This is called when the + button is pressed and the info from the pop up fragment
     * is filled out. This will add the newly created habit into the habit list and to firestore
     * @param newHabit
     * @see AddHabitFragment class
     * @author lwaschuk, yyang13
     */
    public void onOkPressed(Habit newHabit){
        String habitName = newHabit.getName();
        String habitReason = newHabit.getReason();
        String habitDate = newHabit.getDate();
        String habitPrivacy = newHabit.getPrivacy();
        long habitProgress = newHabit.getProgress();
        ArrayList<String> frequencyList = (ArrayList<String>) newHabit.getFrequency();
        if (frequencyList.isEmpty()) {
            frequencyList.add("Null");
        }
        String completionTime = newHabit.getCompletionTime();
        String estimateCompletionTime = newHabit.getEstimateCompletionDate();
        String lastCompletionTime = newHabit.getLastCompletionTime();
        String lastModifiedDate = newHabit.getLastModifiedDate();

        habitId = db.collection(String.valueOf(currentUserHabitCol)).document().getId();
//        Add new habit to Firestore
        HashMap<String, Object> data = new HashMap<>();
        data.put("Habit Name", habitName);
        data.put("Habit Reason", habitReason);
        data.put("Start Date", habitDate);
        data.put("Frequency", frequencyList);
        data.put("Privacy", habitPrivacy);
        data.put("Completion Time", completionTime);
        data.put("Estimate Completion Time", estimateCompletionTime);
        data.put("Last Completion Time", lastCompletionTime);
        data.put("Last Modified Date", lastModifiedDate);

        data.put("Index", 10000);
        data.put("Progress", habitProgress);
        currentUserHabitCol.document(habitId)
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
        updateFragment();
    }

    public void drawerMenu(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

    }

    /**
     * connect to Firestore, create user collection and habit collection
     * @return void
     * @author yyang13
     */
    public void habitToServer() {
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
        currentUserHabitCol.orderBy("Index", Query.Direction.ASCENDING);
    }

    /**
     * This sets a bottom navigation bar for the user to switch between HomeActivity, SearchActivity and ProfileActivity
     * @author Shanshan Wei/swei3
     */
    public void switchActivity(){
        // The bottom Navigation bar
        bottomNavigator = findViewById(R.id.bottom_navigation);
        bottomNavigator.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){

                    case R.id.home:
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return true;
            }
        });
    }

    /**
     * This will help to switch between HomeFragment, TodayFilterFragment and FollowingFilterFragment
     * @author Shanshan Wei/swei3
     */
    public void switchRadioButton(){
        switchHabits = findViewById(R.id.switch_habits);
        switchHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(view);
            }
        });
        switchTodayHabits = findViewById(R.id.switch_today);
        switchTodayHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(view);
            }
        });
        switchFollowingHabits = findViewById(R.id.switch_following);
        switchFollowingHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(view);
            }
        });
    }

    /**
     * If the user click on "Habits" of radio button, it will ask fragment manager to attach the HomeFragment.
     * If the user click on "Today" of radio button, it will ask fragment manager to attach the TodayFilterFragment.
     * If the user clicks on the "Following" radio button, it will ask fragment manager to attach the FollowingFilterFragment.
     * @author Shanshan Wei/swei3
     * @param view
     */
    public void onClickButton(View view){
        boolean isSelected = ((AppCompatRadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.switch_habits:
                if(isSelected) {
                    switchHabits.setTextColor(Color.WHITE);
                    switchTodayHabits.setTextColor(Color.BLACK);
                    switchFollowingHabits.setTextColor(Color.BLACK);
                    if(homeFragment == null){
                        homeFragment = HomeFragment.newInstance();
                    }
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    if(filterFragment != null && filterFragment.isVisible()){
                        transaction.hide(filterFragment);
                    }
                    if(followingFragment != null && followingFragment.isVisible()){
                        transaction.hide(followingFragment);
                    }
                    transaction.replace(R.id.container, homeFragment);
                    transaction.commit();
                    Toast.makeText(getApplicationContext(), "All Habits", Toast.LENGTH_SHORT).show();
                    break;
                }
                break;
            case R.id.switch_today:
                if(isSelected){
                    switchTodayHabits.setTextColor(Color.WHITE);
                    switchHabits.setTextColor(Color.BLACK);
                    switchFollowingHabits.setTextColor(Color.BLACK);
                    if(filterFragment==null) {
                        filterFragment = TodayFilterFragment.newInstance();
                    }
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    if(homeFragment != null && homeFragment.isVisible()){
                        transaction.hide(homeFragment);
                    }
                    if(followingFragment != null && followingFragment.isVisible()){
                        transaction.hide(followingFragment);
                    }
                    transaction.replace(R.id.container, filterFragment);
                    transaction.commit();
                    Toast.makeText(getApplicationContext(), "Today's Habits", Toast.LENGTH_SHORT).show();
                    break;

                }
            case R.id.switch_following:
                if(isSelected) {
                    switchFollowingHabits.setTextColor(Color.WHITE);
                    switchTodayHabits.setTextColor(Color.BLACK);
                    switchHabits.setTextColor(Color.BLACK);
                    if(followingFragment==null) {
                        followingFragment = FollowingFilterFragment.newInstance();
                    }
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    if(homeFragment != null && homeFragment.isVisible()){
                        transaction.hide(homeFragment);
                    }
                    if(filterFragment != null && filterFragment.isVisible()){
                        transaction.hide(filterFragment);
                    }
                    transaction.replace(R.id.container, followingFragment);
                    transaction.commit();
                    Toast.makeText(getApplicationContext(), "Following Habits", Toast.LENGTH_SHORT).show();
                    break;

                }
        }
    }

    public void updateFragment(){
        homeFragment = HomeFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, homeFragment);
        transaction.commit();

    }




}
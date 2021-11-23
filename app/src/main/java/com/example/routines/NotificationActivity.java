package com.example.routines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NotificationActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigator;
    AppCompatRadioButton switchMyRequest;
    AppCompatRadioButton switchFollowerRequest;
    MyRequestFragment myRequestFragment;
    FollowerRequestFragment followerRequestFragment;
    FrameLayout fragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        fragmentLayout = findViewById(R.id.container);

        switchActivity();
        switchRadioButton();
        updateFragment();
    }


    /**
     * This sets a bottom navigation bar for the user to switch between HomeActivity, SearchActivity and ProfileActivity
     * @author Shanshan Wei
     */
    public void switchActivity(){
        bottomNavigator = findViewById(R.id.bottom_navigation);
        bottomNavigator.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.notification:
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return true;
            }
        });
    }


    /**
     * This will help to switch between HomeFragment and TodayFilterFragment
     * @author Shanshan Wei/swei3
     */
    public void switchRadioButton(){
        switchMyRequest = findViewById(R.id.switch_mine);
        switchMyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(view);
            }
        });
        switchFollowerRequest = findViewById(R.id.switch_follower);
        switchFollowerRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(view);
            }
        });
    }

    /**
     * If the user click on "Habits" of radio button, it will ask fragment manager to attach the HomeFragment.
     * If the user click on "Today" of radio button, it will ask fragment manager to attach the TodayFilterFragment.
     * @author Shanshan Wei/swei3
     * @param view
     */
    public void onClickButton(View view){
        boolean isSelected = ((AppCompatRadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.switch_mine:
                if(isSelected) {
                    switchMyRequest.setTextColor(Color.WHITE);
                    switchFollowerRequest.setTextColor(Color.BLACK);
                    if(myRequestFragment == null){
                        myRequestFragment = MyRequestFragment.newInstance();
                    }
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    if(followerRequestFragment != null && followerRequestFragment.isVisible()){
                        transaction.hide(followerRequestFragment);
                    }
                    transaction.replace(R.id.container, myRequestFragment);
                    transaction.commit();
                    Toast.makeText(getApplicationContext(), "All Habits", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch_follower:
                if(isSelected){
                    switchFollowerRequest.setTextColor(Color.WHITE);
                    switchMyRequest.setTextColor(Color.BLACK);
                    if(followerRequestFragment==null) {
                        followerRequestFragment = FollowerRequestFragment.newInstance();
                    }
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    if(myRequestFragment != null && myRequestFragment.isVisible()){
                        transaction.hide(myRequestFragment);
                    }
                    transaction.replace(R.id.container, followerRequestFragment);
                    transaction.commit();
                    Toast.makeText(getApplicationContext(), "Today's Habits", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void updateFragment(){
        myRequestFragment = MyRequestFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, myRequestFragment);
        transaction.commit();

    }
}
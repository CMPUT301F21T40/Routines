package com.example.routines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigator;
    EditText inputUserName;
    ListView userList;
    ArrayAdapter<String> userArrayAdapter;
    ArrayList<String> userArrayList;
    ArrayList<String> userIdArrayList;
    Button searchButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        switchActivity();

        userArrayList = new ArrayList<>();
        userIdArrayList = new ArrayList<>();


        inputUserName = findViewById(R.id.search_text);
        userList = findViewById(R.id.search_list);
        searchButton = findViewById(R.id.search_user_button);
        userArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userArrayList);
        userList.setAdapter(userArrayAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = inputUserName.getText().toString();
                CollectionReference userRef = db.collection("Users");
                userRef
                        .whereEqualTo("User Name", userName)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("Success", document.getId() + " => " + document.getData());
                                        String name = (String) document.getData().get("User Name");
                                        String id = (String) document.getId();

                                        Log.d(name, id);

                                        userArrayList.add(name);
                                        userIdArrayList.add(id);
                                        userArrayAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Log.d("Fail", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });


        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SearchProfileActivity.class);
                String userId = userIdArrayList.get(i);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });




    }

    public void switchActivity(){
        // The bottom Navigation bar
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
}
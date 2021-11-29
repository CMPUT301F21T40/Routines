package com.example.routines;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowUserActivity extends AppCompatActivity {

    TextView textLabel;
    TextView textLine;
    ListView usersListView;
    ArrayList<String> usersList;
    ArrayAdapter<String> usersAdapter;
    String id;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseAuth myAuth;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textLabel = findViewById(R.id.textView_label);
        type = getIntent().getStringExtra("type");
        textLabel.setText(type);
        textLine = findViewById(R.id.show_users_line);
        usersListView = findViewById(R.id.show_users_list);
        usersList = new ArrayList<>();
        usersAdapter = new ArrayAdapter<>(this, R.layout.user_content, usersList);
        usersListView.setAdapter(usersAdapter);

        myAuth = FirebaseAuth.getInstance();
        id = myAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Notification");

        if(type.equals("Followers")){
            showFollowers();
        }else{
            showFollowing();
        }

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = usersList.get(i);
                db.collection("Users")
                        .whereEqualTo("User Name", name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        String showUserId = document.getId();
                                        Intent intent = new Intent(getApplicationContext(), SearchProfileActivity.class);
                                        intent.putExtra("userId", showUserId);
                                        startActivity(intent);
                                    }


                                }
                            }
                        });
            }
        });
    }

    public void showFollowers(){
        collectionReference
                .whereEqualTo("Receiver", id)
                .whereEqualTo("Status", "accepted")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (isEmpty) {
                                Log.d("My Request", "null");
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("My Request", document.getId() + " => " + document.getData());
                                    collectionReference
                                            .document(document.getId())
                                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if (error != null) {
                                                        Log.w("Get request following", "Listen failed.", error);
                                                        return;
                                                    }

                                                    if (value != null && value.exists()) {
                                                        Log.d("Get request following", "Current data: " + value.getData());
                                                        String name = (String) value.getData().get("Sender Name");
                                                        usersList.add(name);
                                                        usersAdapter.notifyDataSetChanged();
                                                    } else {
                                                        Log.d("get request", "Current data: null");
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }


    public void showFollowing(){
        collectionReference
                .whereEqualTo("Sender", id)
                .whereEqualTo("Status", "accepted")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (isEmpty) {
                                Log.d("My Request", "null");
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("My Request", document.getId() + " => " + document.getData());
                                    collectionReference
                                            .document(document.getId())
                                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if (error != null) {
                                                        Log.w("Get request", "Listen failed.", error);
                                                        return;
                                                    }

                                                    if (value != null && value.exists()) {
                                                        Log.d("Get request", "Current data: " + value.getData());
                                                        String name = (String) value.getData().get("Receiver Name");
                                                        usersList.add(name);
                                                        Log.d("list length", String.valueOf(usersList.size()));
                                                        usersAdapter.notifyDataSetChanged();
                                                    } else {
                                                        Log.d("get request", "Current data: null");
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }



}
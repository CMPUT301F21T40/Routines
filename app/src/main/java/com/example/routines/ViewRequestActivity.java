package com.example.routines;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewRequestActivity extends AppCompatActivity {
    TextView textName;
    TextView nameShow;
    TextView emailShow;
    TextView textEmail;
    TextView textLine;
    ImageView userImage;
    RadioGroup radioGroup;
    Button applyButton;
    Button resetButton;
    String requestFrom;
    String requestStatus;
    String updatedStatus;
    int checkedId;
    FirebaseFirestore db;
    FirebaseAuth myAuth;
    CollectionReference requestReference;
    FirebaseStorage storage;
    StorageReference storageRef;
    String userId;
    String userRequestEmail;
    String requestUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestFrom = getIntent().getStringExtra("User Name");
        requestStatus = getIntent().getStringExtra("Status");

        db = FirebaseFirestore.getInstance();
        requestReference = db.collection("Notification");
        myAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("User Photo");

        initView();
        showInfo();
    }

    public void initView(){
        textName = findViewById(R.id.textView_request_user);
        nameShow = findViewById(R.id.textView_request_user2);
        emailShow = findViewById(R.id.textView_request_user_email2);
        textEmail = findViewById(R.id.textView_request_user_email);
        textLine = findViewById(R.id.textView_request_line);
        userImage = findViewById(R.id.image_request);
        radioGroup = findViewById(R.id.radioGroup);
        applyButton = findViewById(R.id.button_status_selected);
        resetButton = findViewById(R.id.button_status_reset);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedId = radioGroup.getCheckedRadioButtonId();
                if(checkedId == -1){
                    Toast.makeText(getApplicationContext(), "You haven't seleted a choice", Toast.LENGTH_SHORT).show();
                }else{
                    findRadioButton(checkedId);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.clearCheck();
                updatedStatus = "";
            }
        });
    }

    public void findRadioButton(int checkedId){
        switch (checkedId){
            case R.id.radio_accept:
                updatedStatus = "accepted";
                updateStatus();
                break;

            case R.id.radio_deny:
                updatedStatus = "denied";
                updateStatus();
                break;

        }
    }

    public void showInfo(){
        nameShow.setText(requestFrom);
        CollectionReference userCollection = FirebaseFirestore.getInstance()
                .collection("Users");
        userCollection
                .whereEqualTo("User Name", requestFrom)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                userCollection
                                        .document(document.getId())
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (error != null) {
                                                    Log.w("Get name", "Listen failed.", error);
                                                    return;
                                                }
                                                if (value != null && value.exists()){
                                                    userRequestEmail = (String) value.getData().get("Email");
                                                    requestUserId = (String) value.getData().get("User ID");
                                                    emailShow.setText(userRequestEmail);
                                                } else {
                                                    Log.d("get request", "Current data: null");
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

    }

    public void showImage(){
        StorageReference imageRef = storageRef.child(requestUserId);
        imageRef.getBytes(1024*1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                    }
                })

    }

    public void updateStatus(){



    }
}
package com.example.routines;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    final String TAG = "Profile";

    FirebaseFirestore db;
    CollectionReference Users;
    CollectionReference userNames;
    FirebaseAuth myAuth;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference fileRef;

    private String UserId;
    BottomNavigationView BottomNavigator;


    private TextView NameText;
    private TextView EmailText;
    private TextView UserName;
    private TextView UserEmail;
    private ImageView UserPhoto;
    private Uri ImageUri;
    Button LogOutButton;
    ActivityResultLauncher<String> mGetContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Users = db.collection("Users");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("User Photo");
        UserId = myAuth.getCurrentUser().getUid();

        initializeData();
        showImage();
        photoPicker();
        switchActivity();
        showInformation();



    }

    public void initializeData(){
        EmailText = findViewById(R.id.text_email_lable_profile);
        NameText = findViewById(R.id.text_user_lable_profile);
        UserEmail = findViewById(R.id.text_email_profile);
        UserName = findViewById(R.id.text_user_profile);
        UserPhoto = findViewById(R.id.image_profile);
        LogOutButton = findViewById(R.id.button_profile);
        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        UserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

    }

    public void switchActivity(){
        // The bottom Navigation bar
        BottomNavigator = findViewById(R.id.bottom_navigation);
        BottomNavigator.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        return true;

                }
                return true;
            }
        });
    }

    public void showImage(){
        StorageReference imageRef = storageRef.child(UserId);
        imageRef.getBytes(1024*1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        UserPhoto.setImageBitmap(bitmap);
                    }
                });
    }

    public void showInformation(){
        FirebaseUser User = myAuth.getCurrentUser();
        if(User != null){
            UserId = User.getUid();
            Users.document(UserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if (value != null && value.exists()) {
                        Log.d(TAG, "Current data: " + value.getData());
                        String Name = (String) value.getData().get("User Name");
                        String Email = (String) value.getData().get("Email");
                        UserName.setText(Name);
                        UserEmail.setText(Email);

                    } else {
                        Log.d(TAG, "Current data: null");
                    }

                }
            });

        }
    }

    public void photoPicker(){
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if(uri != null){
                            try {
                                ImageUri = uri;
                                InputStream imageStream = getContentResolver().openInputStream(ImageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                UserPhoto.setImageBitmap(selectedImage);
                                uploadImage();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(ProfileActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
                        }


                    }
                });
    }

    public void uploadImage(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        fileRef = storageRef.child(UserId);
        fileRef.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.d("Download url", url);
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }


}
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

/**
 * This activity will allows the user to see their profile information, upload a photo for the profile
 * It also allows user to exit the app and direct the user back to WelcomeActivity
 * @author Shanshan Wei/swei3
 * @see SearchActivity
 * @see HomeActivity
 */
public class ProfileActivity extends AppCompatActivity {

    final String TAG = "Profile";

    FirebaseFirestore db;
    CollectionReference Users;
    CollectionReference userNames;
    FirebaseAuth myAuth;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference fileRef;

    private String userId;
    BottomNavigationView bottomNavigator;


    private TextView nameText;
    private TextView emailText;
    private TextView userName;
    private TextView userEmail;
    private ImageView userPhoto;
    private Uri imageUri;
    Button logOutButton;
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
        userId = myAuth.getCurrentUser().getUid();

        initializeData();
        showImage();
        photoPicker();
        switchActivity();
        showInformation();



    }
    /**
     * This will initialize views for all components like textview, buttons
     */
    public void initializeData(){
        emailText = findViewById(R.id.text_email_lable_profile);
        nameText = findViewById(R.id.text_user_lable_profile);
        userEmail = findViewById(R.id.text_email_profile);
        userName = findViewById(R.id.text_user_profile);
        userPhoto = findViewById(R.id.image_profile);
        logOutButton = findViewById(R.id.button_profile);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        /**
         * This sets the image clickable. It will open the photo gallery
         */
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

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
                        startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        return true;

                }
                return true;
            }
        });
    }

    /**
     * This reads the user's profile photo from Firebase Storage and shows on imageview
     * @author Shanshan Wei/swei3
     */
    public void showImage(){
        StorageReference imageRef = storageRef.child(userId);
        imageRef.getBytes(1024*1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        userPhoto.setImageBitmap(bitmap);
                    }
                });
    }

    /**
     * This will reads the user's profile information and shows in textview
     * @author Shanshan Wei
     */
    public void showInformation(){
        FirebaseUser User = myAuth.getCurrentUser();
        if(User != null){
            userId = User.getUid();
            Users.document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
                        userName.setText(Name);
                        userEmail.setText(Email);

                    } else {
                        Log.d(TAG, "Current data: null");
                    }

                }
            });

        }
    }

    /**
     * This allows the user to pick a photo from the photo gallery
     * @author Shanshan Wei/swie3
     */
    public void photoPicker(){
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if(uri != null){
                            try {
                                imageUri = uri;
                                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                userPhoto.setImageBitmap(selectedImage);
                                uploadImage();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(ProfileActivity.this, "You haven't picked an image",Toast.LENGTH_LONG).show();
                        }


                    }
                });
    }

    /**
     * This will upload the user's picked photo to Storage on firebase
     * @author Shanshan Wei/swei3
     */
    public void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        fileRef = storageRef.child(userId);
        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
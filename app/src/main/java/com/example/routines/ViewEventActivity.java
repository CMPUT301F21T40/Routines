package com.example.routines;


import static android.app.appsearch.AppSearchResult.RESULT_OK;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;

import static android.content.ContentValues.TAG;

import static com.google.firebase.firestore.FieldValue.arrayRemove;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

import java.util.ArrayList;

/**
 * This activity display the details of a given event
 * It allows user to edit the details of the event
 * @author Zezhou Xiong
 * @see Event
 */
public class ViewEventActivity extends AppCompatActivity implements EditEventFragment.OnFragmentInteractionalListener, DeleteEventFragment.OnFragmentInteractionListener{

    //Text views for the details of given event
    TextView eventName;
    TextView eventComment;
    TextView eventDate;
    TextView eventLocation;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); //connect to firebase
    FloatingActionButton editButton;
    FloatingActionButton deleteButton;
    String eventId;
    ImageView eventImage;


    ActivityResultLauncher<String> mGetContent;

    private Uri imageUri;
    private Bitmap imageBitmap;
    private String pictureImagePath = "";
    StorageReference fileRef;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference collectionRef;

    String habitId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        eventName = findViewById(R.id.view_event_name);
        eventComment = findViewById(R.id.view_event_comment);
        eventDate = findViewById(R.id.view_event_date);
        eventLocation = findViewById(R.id.view_event_location);
        editButton = findViewById(R.id.edit_event_button);
        deleteButton = findViewById(R.id.delete_habit_event_button);
        eventImage = findViewById(R.id.image_show_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enable the back button

        eventId = (String) getIntent().getStringExtra("eventId"); //fetch event id from last activity
        habitId = (String) getIntent().getStringExtra("habitId"); //fetch habit id from last activity



        FirebaseAuth myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        //String actualUserId = user.getUid();
        String actualUserId = getIntent().getStringExtra("actualUserId");
        String userId = getIntent().getStringExtra("userId");
        Boolean sameUser = getIntent().getBooleanExtra("sameUser", true);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("Event photos");
        collectionRef = storageRef.child(userId);
        fileRef = collectionRef.child(eventId);

        cameraOrGallery();

        if (!sameUser) {
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        }
        /*
        if (userId != actualUserId) {
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        } else {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }*/
        albumPhoto();
        showInfo();
        showImage();


        /**
         * This sets a button listener and pops a fragment for the user to edit the event details
         * @author Shanshan Wei/swei3
         */
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameShowed = eventName.getText().toString();
                String commentShowed = eventComment.getText().toString();
                String dateShowed = eventDate.getText().toString();
                String locationShowed = eventLocation.getText().toString();
                Event eventNow = new Event(nameShowed, commentShowed, dateShowed, locationShowed);
                EditEventFragment.newInstance(eventNow).show(getSupportFragmentManager(), "Edit_Event");
            }
        });


        /**
         * This sets a button listener and pops a dialog for the user to delete the event details
         * @auther Zezhou Xiong
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteEventFragment();
                DeleteEventFragment.newInstance(eventId).show(getSupportFragmentManager(),"Delete Event");
            }
        });


    }

    /**
     * This function receive a event id and delete the corresponding event in the firebase
     * @param eventId
     * @auther zezhou
     */
    public void onDeletePressedEvent(String eventId) {
        String userId = getIntent().getStringExtra("userId");
        String habitId = getIntent().getStringExtra("habitId");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("Events")
                .document(eventId);
        eventRef
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

        DocumentReference habitRef = db.collection("Habits")
                .document(userId)
                .collection("Habits")
                .document(habitId);

        habitRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        String completion = (String) document.getData().get("Completion Time");
                        String estimateCompletion = (String) document.getData().get("Estimate Completion Time");
                        completion = Integer.toString(Integer.parseInt(completion)-1);
                        int progress = (Integer.parseInt(completion) *100 / Integer.parseInt(estimateCompletion));


                        habitRef.update("Completion Time", completion,
                                "Progress", progress,
                                "eventList", arrayRemove(eventId))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        onBackPressed();


    }




    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(ViewEventActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewEventActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }
        if (ContextCompat.checkSelfPermission(ViewEventActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewEventActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if(imageBitmap!=null){
                    eventImage.setImageBitmap(imageBitmap);
                    uploadCameraPhoto();
                }


            }
        }
    }

    public void cameraOrGallery(){
        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choice_bottom_dialog);
        LinearLayout layoutCamera = dialog.findViewById(R.id.camera_layout);
        LinearLayout layoutAlbum = dialog.findViewById(R.id.album_layout);
        LinearLayout layoutCancel = dialog.findViewById(R.id.cancel_layout);

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //'dispatchTakePictureIntent();
                checkCameraPermission();
                openBackCamera();
            }
        });

        layoutAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mGetContent.launch("image/*");
            }
        });

        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, 100);
    }

    public void albumPhoto(){
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if(uri != null){
                            try {
                                imageUri = uri;
                                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                eventImage.setImageBitmap(selectedImage);
                                uploadImage();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(ViewEventActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
                        }


                    }
                });

    }


    public void uploadImage(){
        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.d("Download url", url);
                        Toast.makeText(getApplicationContext(),"Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void uploadCameraPhoto(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w("Upload photos", "failure");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Uploading", "successful");
            }
        });

    }



    /**
     * This overrides the listener onOkPressed.
     * If the user edits the event details on EditEventFragment and clicks OK, this will start to work
     * It will update the local event and update the event doc on the firebase
     * @author Shanshan Wei/swei3
     * @param event
     */
    @Override
    public void onOkPressed(Event event) {
        String nameEdited = event.getEventName();
        String descriptionEdited = event.getDescription();
        String dateEdit = event.getEventDate();
        String locationEdit = event.getEventLocation();
        Log.d("Edited event", nameEdited+descriptionEdited);
        db.collection("Events").document(eventId)
                .update("name", nameEdited, "description", descriptionEdited, "date", dateEdit, "location", locationEdit);
        showInfo();

    }

    /**
     * This shows the information of the event
     */
    public void showInfo(){
        //fetch document from firebase with given event id
        DocumentReference eventRef = db
                .collection("Events")
                .document(eventId);
        //get fields of document and set the text to text view
        eventRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                                String name = (String) document.getData().get("name");
                                String date = (String) document.getData().get("date");
                                String comment = (String) document.getData().get("description");
                                String location = (String) document.getData().get("location");

                                eventName.setText(name);
                                eventDate.setText(date);
                                eventComment.setText(comment);
                                eventLocation.setText(location);
                            } else
                                Log.d("Fail", "Error document noe exist: ", task.getException());

                        } else
                            Log.d("Fail", "Error fail to access documents ", task.getException());
                    }

                });
    }

    public void showImage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("Event photos");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference collectionRef = storageRef.child(userId);
        StorageReference imageRef;
        try{
            imageRef = collectionRef.child(eventId);
            imageRef.getBytes(1024*1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            eventImage.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Event image", "Doesn't exist");
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

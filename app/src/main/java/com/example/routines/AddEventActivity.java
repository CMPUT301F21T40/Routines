package com.example.routines;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Purpose: An activity to allow user to complete a habit event of a habit
 * Outstanding Issues:None
 * @author Zezhou Xiong
 */
public class AddEventActivity extends AppCompatActivity implements LocationListener {
    EditText eventName;
    EditText eventDescription;
    EditText eventLocation;
    Button addButton;
    Button getLocationBtn;
    ImageView addPhoto;
    String habitId;
    String userId;
    String eventID;

    LocationManager locationManager;

    Button openMap;
    double currentLatitude = 0;
    double currentLongitude = 0;
    double latFromMap;
    double longFromMap;

    boolean loadingLocation = false;

    ActivityResultLauncher<String> mGetContent;

    private Uri imageUri;
    private Bitmap imageBitmap;
    private String pictureImagePath = "";
    StorageReference fileRef;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference collectionRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);

        //allow user to return to last activity by press a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventName = findViewById(R.id.view_habit_name);
        eventDescription = findViewById(R.id.view_habit_reason);
        eventLocation = findViewById(R.id.event_location_editText);
        addButton = findViewById(R.id.add_event_button);
        getLocationBtn = findViewById(R.id.get_location_btn);
        openMap = findViewById(R.id.open_map);

        albumPhoto();
        cameraOrGallery();



        habitId = (String) getIntent().getStringExtra("habitId");
        userId = (String) getIntent().getStringExtra("userId");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("Event photos");
        collectionRef = storageRef.child(userId);





//        Check location permission
        checkLocationPermission();

//        Get location
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                loadingLocation = true;
                if (currentLongitude == 0 || currentLatitude == 0) {
                    Toast.makeText(getApplicationContext(), "Loading Location", Toast.LENGTH_SHORT ).show();
                }
            }
        });

//        Open map
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventActivity.this, Map.class);
                intent.putExtra("currentLat", currentLatitude);
                intent.putExtra("currentLong", currentLongitude);

                startActivityForResult(intent, 1);
            }
        });


        /**
         * This is called when the add button is pressed and all the info
         * is filled out. This will add the newly created event into the habit
         */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eventName.getText().toString();
                String description = eventDescription.getText().toString();
                String locaiton = eventLocation.getText().toString();

                CollectionReference eventReference = db.collection("Events");

                if (name == "")
                    name = "null";

                if (description == "")
                    description = "null";

                if (locaiton == "") {
                    locaiton = "null";
                }

//               Set current date as default
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);


//                If get current location button is pressed, add event will be diabled until information is loaded
                if (currentLongitude == 0 && currentLatitude == 0 && loadingLocation == true) {
                    Toast.makeText(getApplicationContext(), "Loading Location", Toast.LENGTH_SHORT ).show();
                } else {
                    String date = String.format("%d-%02d-%02d", year, month, day);
                    HashMap<String, String> data = new HashMap<>();
                    data.put("name", name);
                    data.put("description", description);
                    data.put("location", locaiton);
                    data.put("habitId", habitId);
                    data.put("date", date);

                    //generate an unique id for each event
                    eventID = eventReference.document().getId();
                    fileRef = collectionRef.child(eventID);
                    eventReference
                            .document(eventID)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Success","Document added with ID: "+eventID);
                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Fail", "Error adding document", e);
                                }
                            });

                    //after successfully add a event, update the event list under the habit collection
                    if (eventID != null) {
                        if(imageUri!=null){
                            uploadImage();
                        }else{
                            uploadCameraPhoto();
                        }
                        DocumentReference habitReference = db
                                .collection("Habits").document(userId)
                                .collection("Habits").document(habitId);
                        Log.d("Success", "field added with ID: " + eventID);
                        habitReference.update("eventList", FieldValue.arrayUnion(eventID));

                    }

                    //return to the last activity after everything is done
                    eventName.setText("");
                    eventDescription.setText("");
                    eventLocation.setText("");

                    updateCompletion(habitId, userId, db);
                    onBackPressed();
                }


            }
        });

    }

    /**
     * Get result back from activity
     * @param requestCode
     * @param resultCode
     * @param data
     * @author yyang13
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                latFromMap = data.getDoubleExtra("currentLat", 0);
                longFromMap = data.getDoubleExtra("currentLong", 0);

                if (latFromMap != 0 || longFromMap != 0){
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        List<Address> addresses = geocoder.getFromLocation(latFromMap, longFromMap, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        eventLocation.setText(address);

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        if (requestCode == 100) {
            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if(imageBitmap != null){
                    addPhoto.setImageBitmap(imageBitmap);
                }

            }
        }

    }


    /**
     * Check Location Permission
     * @author yyang13
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(AddEventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddEventActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }

    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(AddEventActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddEventActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }
        if (ContextCompat.checkSelfPermission(AddEventActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddEventActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);
        }

    }


    /**
     * Get Location Service
     * @author yyang13
     */
    @SuppressLint("MissingPermission")
    public void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, AddEventActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This is called when the up button is pressed. It changes the original
     * functionality of up button. It let user back to the last activity
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get Address From Location
     * @param location
     * @author yyang13
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(AddEventActivity.this, Locale.getDefault());
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            eventLocation.setText(address);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cameraOrGallery(){
        addPhoto = findViewById(R.id.imageView_add_event);
        addPhoto.setOnClickListener(new View.OnClickListener() {
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
                                addPhoto.setImageBitmap(selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(AddEventActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
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



    public void updateCompletion(String habitId, String userId, FirebaseFirestore db){
        DocumentReference habitRef = db
                .collection("Habits")
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
                        String lastCompletionTime = (String) document.getData().get("Last Completion Time");
                        String lastModifiedDate = (String) document.getData().get("Last Modified Date");
                        ArrayList<String> habitFrequency = (ArrayList<String>) document.getData().get("Frequency");
                        completion = Integer.toString(Integer.parseInt(completion)+1);
                        int estimateCompletion = calculateEstimateCompletionTime(lastCompletionTime, lastModifiedDate, habitFrequency);

                        int progress = (Integer.parseInt(completion) *100 / estimateCompletion);

                        habitRef.update("Completion Time", completion,
                                "Progress", progress,
                                "Estimate Completion Time", Integer.toString(estimateCompletion))
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

    }

    /**
     * This date receive the lastCompletionTime, lastModifiedDate and frequency to calculate the estimate completion time
     * of a habit. It calculates the times of completion the user should finish from the time they edit the frequency
     * and then plus the times of completion the user should finish before they edit the frequency
     * @param lastCompletionTime
     * @param lastModifiedDate
     * @param habitFrequency
     * @return int estimateCompletionTime
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int calculateEstimateCompletionTime(String lastCompletionTime, String lastModifiedDate, ArrayList<String> habitFrequency) {
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
        int estimateCompletionTime = Integer.parseInt(lastCompletionTime) + weeks * habitFrequency.size() + count;
        return estimateCompletionTime;
    }

    /**
     * This function receive a date and calculator the difference in days between the current date and
     * the given date
     * @param lastModifiedDate
     * @return String days
     * @author zezhou
     */
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


}

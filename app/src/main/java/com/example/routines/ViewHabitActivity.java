package com.example.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class ViewHabitActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener {
    private ImageView backImage;
    TextView nameView;
    TextView dateView;
    TextView reasonView;
    String name;
    String date;
    String reason;
    String habitName;
    String habitDate;
    String habitReason;
    ArrayList<String> habitFrequency;
    String habitPrivacy;
    Button add;
    Button view;
    FloatingActionButton edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        backImage = (ImageView) findViewById(R.id.add_event_backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        name = (String) "Name:  "+getIntent().getStringExtra("habitName");
        nameView = findViewById(R.id.add_event_name);
        reasonView = findViewById(R.id.add_event_description);
        dateView = findViewById(R.id.habit_date);
        add = findViewById(R.id.add_event_button);
        view = findViewById(R.id.view_event_button);
        edit = findViewById(R.id.edit_habit_button);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference habitRef = db
                .collection("Habits")
                .document("test");
        habitRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                // concatenated strings we should to this in the XML later
                                date = (String) "Date Started:  "+document.getData().get("Start Date");
                                reason = (String) "Reason:  "+document.getData().get("Habit Reason");
                                habitFrequency = (ArrayList<String>) document.getData().get("Frequency");
                                habitPrivacy = (String) document.getData().get("Privacy");
                                habitName = getIntent().getStringExtra("habitName");
                                habitDate = (String) document.getData().get("Start Date");
                                habitReason = (String) document.getData().get("Habit Reason");
                                nameView.setText(name);
                                reasonView.setText(reason);
                                dateView.setText(date);
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
                Intent intent = new Intent(ViewHabitActivity.this, AddEventActivity.class);
                intent.putExtra("habitName", name);
                startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabitActivity.this, EventListActivity.class);
                intent.putExtra("habitName", name);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                Bundle bundle = getIntent().getExtras();
                Habit habit = (Habit) bundle.getSerializable();

                name =  getIntent().getStringExtra("habitName");
                reason = getIntent().getStringExtra("habitReason");
                date = getIntent().getStringExtra("habitDate");
                frequency = getIntent().getExtra("habitReason");
                 */
                EditHabitFragment.newInstance(new Habit(habitName, habitReason, habitDate, habitFrequency, habitPrivacy)).show(getSupportFragmentManager(), "EDIT_MEDICINE");

            }
        });

    }
    @Override
    public void onOkPressed(Habit habit) {

    }

    public void onEditPressed(Habit habit, String name, String date, String reason) {
        habit.setName(name);
        habit.setDate(date);
        habit.setReason(reason);
    }


}

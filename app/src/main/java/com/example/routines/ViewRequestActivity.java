package com.example.routines;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ViewRequestActivity extends AppCompatActivity {
    TextView textName;
    TextView nameShow;
    TextView emailShow;
    TextView textEmail;
    TextView textLine;
    ImageView userImage;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button applyButton;
    Button resetButton;
    String requestFrom;
    String requestStatus;
    String updatedStatus;
    int checkedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestFrom = getIntent().getStringExtra("User Name");
        requestStatus = getIntent().getStringExtra("Status");

        initView();
    }

    public void initView(){
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
                break;

            case R.id.radio_deny:
                updatedStatus = "denied";
                break;

        }
    }

    public void updateStatus(){

    }
}
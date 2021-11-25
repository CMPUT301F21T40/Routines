package com.example.routines;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewRequestActivity extends AppCompatActivity {
    TextView textName;
    TextView nameShow;
    TextView emailShow;
    TextView textEmail;
    TextView textLine;
    ImageView userImage;
    RadioGroup radioGroup;
    RadioButton radioButton;

    String requestFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        requestFrom = getIntent().getStringExtra("User Name");
    }

    public void initView(){

    }
}
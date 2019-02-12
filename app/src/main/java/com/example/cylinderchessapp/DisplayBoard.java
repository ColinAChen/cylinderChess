package com.example.cylinderchessapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayBoard extends AppCompatActivity {
    TextView textView = (TextView) findViewById(R.id.textView);
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_board);
        textView.setText("apples");
    }
}

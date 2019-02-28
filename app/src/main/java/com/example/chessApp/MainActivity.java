package com.example.chessApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.chessApp.R;
import com.example.chessApp.normal.DisplayBoardNormal;
import com.example.chessApp.normal.InstructionsNormal;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToBoard(View view) {
        Intent intent = new Intent(this, DisplayBoardNormal.class);
        startActivity(intent);
    }

    public void goToInstructions(View view) {
        Intent intent = new Intent(this, InstructionsNormal.class);
        startActivity(intent);
    }
}
package com.example.chessApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.example.chessApp.normal.InstructionsNormal;

public class MainActivity extends AppCompatActivity
{
    boolean cpuGame = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void computerOption(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();
        cpuGame = checked;
    }

    public void goToBoardNormal(View view) {
        Log.d("mainActivity", "goToBoardNormal");
        //Intent intent = new Intent(this, RoomSelect.class);
        //startActivity(intent);
        //Intent intent = new Intent(this, DisplayBoardNormal.class);
        Intent intent = new Intent(this, RoomSelect.class);
        intent.putExtra("cpuToggle", cpuGame);
        intent.putExtra("gameType", "normal");
        startActivity(intent);
    }

    public void goToBoardCylinder(View view) {
        Log.d("mainActivity", "goToBoardCylinder");
        //Intent intent = new Intent(this, DisplayBoardCylinder.class);
        Intent intent = new Intent(this, RoomSelect.class);
        intent.putExtra("gameType", "cylinder");
        startActivity(intent);
    }
    //public void goToRoomSelect(View view){
    //    Intent intent = new Intent(this, RoomSelect.class);
    //    startActivity(intent);
    //}
    public void goToInstructionsNormal(View view) {
        Intent intent = new Intent(this, InstructionsNormal.class);
        startActivity(intent);
    }
}
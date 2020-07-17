package com.example.chessApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.chessApp.R;
import com.example.chessApp.cylinder.DisplayBoardCylinder;
import com.example.chessApp.normal.DisplayBoardNormal;

//https://javapapers.com/android/get-user-input-in-android/
public class RoomSelect extends AppCompatActivity{
    Button submitName;
    EditText roomNameText;
    //TextView mText;
    boolean cpuGame = false;
    String roomName = "";
    String gameType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("roomSelect", "on create");

        Intent intent = getIntent();
        gameType = intent.getStringExtra("gameType");
        if (gameType.equals("normal")){
            cpuGame=intent.getBooleanExtra("cpuToggle", false);
        }
        Log.d("roomSelect", roomName);
        Log.d("roomSelect", gameType);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_select);

        submitName = (Button)findViewById(R.id.button1);
        submitName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                roomNameText  = (EditText)findViewById(R.id.editText1);
                //mText = (TextView)findViewById(R.id.textView1);
                roomName = roomNameText.getText().toString();
                goToRoom(view);
                //Log.d("roomSelect", "submit clicked");
                //Log.d("roomSelect", roomNameText.getText().toString());
                //Log.d("roomSelect", roomName);
                //startRoom()
                // Check for a valid string to set as a node in Firebase
                //mText.setText("Welcome "+roomName.getText().toString()+"!");
            }
        });
    }
    public void goToRoom(View view){//}, String prevIntent, String roomName, Boolean cpuGame){
        Log.d("roomSelect", "" + "Game Type: " + gameType);
        //Log.d("roomSelect", "" + )
        if (roomName.equals("")){
            return;
        }
        Intent intent = new Intent(this, DisplayBoardNormal.class);
        if (gameType.equals("normal")){
            //intent = new Intent(this, DisplayBoardNormal.class);
            intent.putExtra("cpuToggle", cpuGame);
        }
        if (gameType.equals("cylinder")){
            intent = new Intent(this, DisplayBoardCylinder.class);
        }
        intent.putExtra("roomName", roomName);
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //ntent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}


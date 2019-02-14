package com.example.cylinderchess;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DisplayBoard extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_board);
        final ArrayList<Drawable> data = new ArrayList<>();

        // data to populate the RecyclerView with
        //Drawable[] data = new Drawable[64];
        Log.d("custom message:","size = " );

        for(int x=0; x<64; x++)
        {
            Log.d("custom message:","inside of loop");
            data.add(getResources().getDrawable(R.drawable.black_pawn, null));
        }
        Log.d("custom message:","after loop");
        // set up the RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        int numberOfColumns = 8;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        final Button changePieces = findViewById(R.id.changePieces);
        changePieces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int x=0; x<64; x++)
                {
                    data.set(x, getResources().getDrawable(R.drawable.white_pawn, null));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }

    public ArrayList setUp(ArrayList<Drawable> data)
    {
        for(int x=0; x<64; x++)
        {
            data.set(x, getResources().getDrawable(R.drawable.black_pawn, null));
        }
        return data;
    }

/*    public void changePieces(View view)
    {
        for(int x=0; x<64; x++)
        {
            this.data.set(x, getResources().getDrawable(R.drawable.white_pawn, null));
        }
    }
*/
}

package com.example.cylinderchess;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Arrays;

public class DisplayBoard extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    Board board = new Board(new Piece[8][8], new Piece[64]);
    final ArrayList<Drawable> highlights = new ArrayList<>(64);
    ArrayList<int[]> prevHighlight = new ArrayList<>();
    int prevSquare[] = {-1,-1};
    final ArrayList<Drawable> drawableData = new ArrayList<>();
    boolean boardNormal = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_board);

        board.initializeBoard();

        ImageView view = findViewById(R.id.board);
        view.setImageResource(R.drawable.chessboard_wood);

        Drawable[] tempdrawables = asDrawable(board.oneDimensional);
        drawableData.addAll(Arrays.asList(tempdrawables));

        for(int x=0;x<64;x++)
            highlights.add(getResources().getDrawable(R.drawable.blank, null));

        Log.d("custom message:","1");
        // set up the RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        int numberOfColumns = 8;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        Log.d("custom message:","2");
        adapter = new MyRecyclerViewAdapter(this, drawableData, highlights);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        Log.d("custom message:","3");
        final Button changePieces = findViewById(R.id.changePieces);
        changePieces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        ArrayList<int[]> moves = board.getLegalMoves(board.oneDimensional[position]);
        if(board.oneDimensional[position] != null && board.oneDimensional[position].color != board.whiteToMove)
        {
            moves = new ArrayList<>();
        }
        if(position == 8*prevSquare[0]+prevSquare[1])
        {
            moves = new ArrayList<>();
        }
        if (moves != null && moves.size() != 0) {
            Log.i("success!", "first legal move:" + moves.get(0)[0] + "," + moves.get(0)[1]);
            Log.i("success!", "selected piece:" + board.oneDimensional[position]);
        }

        for (int x = 0; x< prevHighlight.size(); x++)
        {
            if (position == 8 * prevHighlight.get(x)[0] + prevHighlight.get(x)[1])
            {
                if (board.move(prevSquare[0], prevSquare[1], position/8, position%8))
                {
                    Log.i("success!", "moving piece" + prevSquare[0] + " , " + prevSquare[1] + " to "+  position/8+" , "+ position%8);
                    drawableData.set(position, drawableData.get(8 * prevSquare[0] + prevSquare[1]));
                    drawableData.set(8 * prevSquare[0] + prevSquare[1], getResources().getDrawable(R.drawable.blank, null));
                }
                for (int y = 0; y < 64; y++)
                {
                        highlights.set(y, getResources().getDrawable(R.drawable.blank, null));
                }
                prevSquare[0] = -1;
                prevSquare[1] = -1;
                prevHighlight.clear();
                adapter.notifyDataSetChanged();
                return;
            }
        }

        for(int x=0;x<64;x++)
        {
            highlights.set(x, getResources().getDrawable(R.drawable.blank, null));
        }

        for(int x=0;x<moves.size(); x++)
        {
            highlights.set(8*moves.get(x)[0]+moves.get(x)[1], getResources().getDrawable(R.drawable.highlight, null));
        }
        if(position == 8*prevSquare[0]+prevSquare[1])
        {
            prevSquare[0] = -1;
            prevSquare[1] = -1;
        }
        else
        {
            prevSquare[0] = position/8;
            prevSquare[1] = position%8;
        }
        prevHighlight = moves;
        adapter.notifyDataSetChanged();
    }

    public ArrayList setUp(ArrayList<Drawable> data)
    {
        for(int x=0; x<64; x++)
        {
            data.set(x, getResources().getDrawable(R.drawable.black_pawn, null));
        }
        return data;
    }

    public void showPopup(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.promotion_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //popupWindow.dismiss();
                return true;
            }
        });
    }

    public Drawable[] asDrawable(Piece[] pieces)
    {
        Drawable[] arr = new Drawable[64];
        for(int x=0; x < pieces.length;x++){
            if(pieces[x] == null)
            {
                arr[x] = getResources().getDrawable(R.drawable.blank, null);
            }
            else
            {
                String color = pieces[x].getColor();
                String name = pieces[x].getName();
                if (!pieces[x].color)
                {
                    switch(name){
                        case "p": arr[x] = getResources().getDrawable(R.drawable.black_pawn, null);
                            break;
                        case "r": arr[x] = getResources().getDrawable(R.drawable.black_rook, null);
                            break;
                        case "n": arr[x] = getResources().getDrawable(R.drawable.black_knight, null);
                            break;
                        case "b": arr[x] = getResources().getDrawable(R.drawable.black_bishop, null);
                            break;
                        case "q": arr[x] = getResources().getDrawable(R.drawable.black_queen, null);
                            break;
                        case "k": arr[x] = getResources().getDrawable(R.drawable.black_king, null);
                            break;
                        default: arr[x] = getResources().getDrawable(R.drawable.blank, null);
                    }
                }
                else
                {
                    switch(name){
                        case "p": arr[x] = getResources().getDrawable(R.drawable.white_pawn, null);
                            break;
                        case "r": arr[x] = getResources().getDrawable(R.drawable.white_rook, null);
                            break;
                        case "n": arr[x] = getResources().getDrawable(R.drawable.white_knight, null);
                            break;
                        case "b": arr[x] = getResources().getDrawable(R.drawable.white_bishop, null);
                            break;
                        case "q": arr[x] = getResources().getDrawable(R.drawable.white_queen, null);
                            break;
                        case "k": arr[x] = getResources().getDrawable(R.drawable.white_king, null);
                            break;
                        default: arr[x] = getResources().getDrawable(R.drawable.blank, null);
                    }
                }
            }
        }

        return arr;
    }

    public void redrawBoard()
    {
        board.oneFromTwo();
        Drawable[] tempdrawables = asDrawable(board.oneDimensional);
        for(int x = 0; x < 64; x++)
        {
            drawableData.set(x,tempdrawables[x]);
        }
        if(prevSquare[0] != -1) {
            ArrayList<int[]> moves = board.getLegalMoves(board.board[prevSquare[0]][prevSquare[1]]);
            for (int x = 0; x < 64; x++) {
                highlights.set(x, getResources().getDrawable(R.drawable.blank, null));
            }
            for (int x = 0; x < moves.size(); x++) {
                highlights.set(8 * moves.get(x)[0] + moves.get(x)[1], getResources().getDrawable(R.drawable.highlight, null));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void shiftLeft(View view)
    {
        if(prevSquare[0] != -1){
            if(prevSquare[1] == 0) {
                prevSquare[1] = 7;
            }
            else {
                prevSquare[1] = (prevSquare[1] - 1);
            }
        }
        invertBoard();
        board.shiftLeft();
        redrawBoard();
    }

    public void shiftRight(View view)
    {
        if(prevSquare[0] != -1){
            prevSquare[1] = (prevSquare[1] + 1) % 8;
        }
        invertBoard();
        board.shiftRight();
        redrawBoard();
    }

    public void invertBoard()
    {
        ImageView view = findViewById(R.id.board);
        if(boardNormal) {
            view.setImageResource(R.drawable.chessboard_wood_inverted);
        }
        else
        {
            view.setImageResource(R.drawable.chessboard_wood);
        }
        boardNormal = !boardNormal;
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

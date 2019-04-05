package com.example.chessApp.normal;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.chessApp.R;
import com.google.firebase.FirebaseApp;
import java.util.ArrayList;
import java.util.Arrays;

public class DisplayBoardNormal extends AppCompatActivity implements BoardAdapter.ItemClickListener {

    BoardAdapter adapter;
    BoardNormal board = new BoardNormal(new PieceNormal[8][8], new PieceNormal[64]);
    final ArrayList<Drawable> highlights = new ArrayList<>(64);
    ArrayList<int[]> prevHighlight = new ArrayList<>();
    int prevSquare[] = {-1,-1};
    final ArrayList<Drawable> drawableData = new ArrayList<>();
    boolean boardNormal = true;
    ComputerPlayer testPlayer = new ComputerPlayer(board, false, 3);
    boolean cpuGame;
    //Create a firebase realtime database reference
    //FirebaseApp.initializeApp(this);
    //FirebaseApp.
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gameRef = database.getReference("currentGame");
    // Read from the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_board_normal);
        cpuGame=getIntent().getBooleanExtra("cpuToggle", false);

        board.initializeBoard();

        ImageView view = findViewById(R.id.board);
        view.setImageResource(R.drawable.chessboard_wood);

        Drawable[] tempdrawables = asDrawable(board.oneDimensional);
        drawableData.addAll(Arrays.asList(tempdrawables));

        for(int x=0;x<64;x++)
            highlights.add(getResources().getDrawable(R.drawable.blank, null));

        // set up the RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        int numberOfColumns = 8;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        adapter = new BoardAdapter(this, drawableData, highlights);
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
        //checks if position clicked on was one of the previous move's possible moves
        for (int x = 0; x< prevHighlight.size(); x++)
        {
            if (position == 8 * prevHighlight.get(x)[0] + prevHighlight.get(x)[1])
            {
                if (board.move(prevSquare[0], prevSquare[1], position/8, position%8))
                {
                    redrawBoard();
                    cpuGame = cpuGame;
                    Log.i("success!", "moving piece " + prevSquare[0] + " , " + prevSquare[1] + " to "+  position/8+" , "+ position%8);
                    Log.i("CurrentGameBoard",board.boardToString());
                    String toStore = "" + prevSquare[0] + prevSquare[1] + (position/8) + (position%8);
                    gameRef.setValue(toStore);
                    gameRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue(String.class);
                            Log.i("CurrentValue", "Value is: " + value);
                            board.move(Character.getNumericValue(value.charAt(0)),Character.getNumericValue(value.charAt(1)),Character.getNumericValue(value.charAt(2)),Character.getNumericValue(value.charAt(3)));
                            redrawBoard();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("Failed to read", "Failed to read value.", error.toException());
                        }
                    });
                    redrawBoard();
                    board.printBoard();
                    if(board.board[position/8][position%8].getName() == "p"
                            && (position < 8 || position > 55))
                    {
                        promote(board.oneDimensional[position].color, position /8, position %8);
                    }
                    if(board.stalemate())
                    {
                        showTextPopup("Stalemate!");
                        Log.d("board:", "Stalemate!");
                    }
                    else if(board.whiteWin())
                    {
                        showTextPopup("Checkmate! White wins");
                        Log.d("board:" , "Checkmate! White wins!");
                    }
                    else if(board.blackWin())
                    {
                        showTextPopup("Checkmate! Black wins");
                        Log.d("board:" , "Checkmate! Black wins!");
                    }
                    else if(board.whiteKingInCheck() || board.blackKingInCheck())
                    {
                        showTextPopup("Check!");
                        Log.d("board:" , "Check!");
                    }

                    if(cpuGame)
                    {
                        cpuMove();
                    }
                    else {
                        prevHighlight.clear();
                        prevSquare[0] = -1;
                        prevSquare[1] = -1;
                    }
                    return;
                }
            }
        }
        //gets selected position's legal moves
        ArrayList<int[]> moves = board.getLegalMoves(board.oneDimensional[position]);
        //clears moves if clicking on blank piece, incorrect color, or previously clicked piece
        if((board.oneDimensional[position] != null && board.oneDimensional[position].color != board.whiteToMove)
                || position == 8*prevSquare[0]+prevSquare[1])
        {
            moves = new ArrayList<>();
            prevSquare[0] = -1;
            prevSquare[1] = -1;
        }
        else
        {
            prevSquare[0] = position/8;
            prevSquare[1] = position%8;
        }
        prevHighlight = moves;
        redrawBoard();
    }

    public void cpuMove()
    {
        prevHighlight.clear();
        prevSquare[0] = -1;
        prevSquare[1] = -1;
        showTextPopup("Thinking");
        testPlayer.action();
        redrawBoard();
    }

    public Drawable[] asDrawable(PieceNormal[] pieces)
    {
        Drawable[] arr = new Drawable[64];
        for(int x=0; x < pieces.length;x++){
            if(pieces[x] == null)
            {
                arr[x] = getResources().getDrawable(R.drawable.blank, null);
            }
            else
            {
                String color = pieces[x].getStringColor();
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
        for (int x = 0; x < 64; x++) {
            highlights.set(x, getResources().getDrawable(R.drawable.blank, null));
        }
        if(prevSquare[0] != -1) {
            ArrayList<int[]> moves = board.getLegalMoves(board.board[prevSquare[0]][prevSquare[1]]);
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

    public void promote(final boolean color, final int x, final int y){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.promotion_popup);

        ImageView knightButton = (ImageView) dialog.findViewById(R.id.chooseKnight);
        ImageView bishopButton = (ImageView) dialog.findViewById(R.id.chooseBishop);
        ImageView rookButton = (ImageView) dialog.findViewById(R.id.chooseRook);
        ImageView queenButton = (ImageView) dialog.findViewById(R.id.chooseQueen);

        if(color) {
            knightButton.setImageDrawable(getResources().getDrawable(R.drawable.white_knight, null));
            bishopButton.setImageDrawable(getResources().getDrawable(R.drawable.white_bishop, null));
            rookButton.setImageDrawable(getResources().getDrawable(R.drawable.white_rook, null));
            queenButton.setImageDrawable(getResources().getDrawable(R.drawable.white_queen, null));
        }
        else{
            knightButton.setImageDrawable(getResources().getDrawable(R.drawable.black_knight, null));
            bishopButton.setImageDrawable(getResources().getDrawable(R.drawable.black_bishop, null));
            rookButton.setImageDrawable(getResources().getDrawable(R.drawable.black_rook, null));
            queenButton.setImageDrawable(getResources().getDrawable(R.drawable.black_queen, null));
        }

        knightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.place(new KnightNormal("", color, x, y), x, y);
                redrawBoard();
                dialog.dismiss();
            }
        });
        bishopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.place(new BishopNormal("", color, x, y), x, y);
                redrawBoard();
                dialog.dismiss();
            }
        });
        rookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.place(new RookNormal("", color, x, y), x, y);
                redrawBoard();
                dialog.dismiss();
            }
        });
        queenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.place(new QueenNormal("", color, x, y), x, y);
                redrawBoard();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showTextPopup(String message) {
        final Dialog dialog = new Dialog(this) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                // Tap anywhere to close dialog.
                this.dismiss();
                return true;
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.text_popup);


        TextView text = dialog.findViewById(R.id.messagePopup);
        text.setText(message);

        dialog.show();


    }
}



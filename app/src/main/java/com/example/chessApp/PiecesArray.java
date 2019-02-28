package com.example.chessApp;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class PiecesArray {
    public static ArrayList<Drawable> data;

    public PiecesArray(int size)
    {
        data = new ArrayList<>(size);
    }
}

package com.example.iksoksandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassicGameActivity extends AppCompatActivity {

    //Constants
    public final int grid_dimension = 3;

    //Variables
    GridView gv_board;
    Board gameBoard;
    ArrayList<Square> boardSquares;
    BoardAdapter boardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_game);


        //Initialization
        gv_board = (GridView) findViewById(R.id.gridView);
        gameBoard = new Board();
        boardSquares = new ArrayList<>();

        //Initialization
        for(int i=0; i<grid_dimension*grid_dimension; i++)
        {
            boardSquares.add(new Square(i, 'X'));
        }

        boardSquares.set(3, new Square(3, 'O'));


        boardAdapter = new BoardAdapter(this, boardSquares);

        gv_board.setAdapter(boardAdapter);


    }
}
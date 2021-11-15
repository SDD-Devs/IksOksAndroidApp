package com.example.iksoksandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.example.iksoksandroidapp.IksOksLogic.Game;
import com.example.iksoksandroidapp.IksOksLogic.BoardAdapter;
import com.example.iksoksandroidapp.IksOksLogic.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;
import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

public class ClassicGameActivity extends AppCompatActivity {


    //Declaration
    private static GameManager gameManager;
    BoardAdapter boardAdapter;
    GridView gv_board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_game);

        //Initialization
        gameManager = new GameManager();
        gv_board = (GridView) findViewById(R.id.gridView);

        //Instantiate Game
        gameManager.startNewGame();

        boardAdapter = new BoardAdapter(this, gameManager.getGame());



        //Set the GridView adapter to the list of tiles.
        gv_board.setAdapter(boardAdapter);

    }


}
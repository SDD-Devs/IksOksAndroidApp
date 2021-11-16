package com.example.iksoksandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import com.example.iksoksandroidapp.IksOksLogic.BoardAdapter;
import com.example.iksoksandroidapp.IksOksLogic.Game;
import com.example.iksoksandroidapp.IksOksLogic.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class ClassicGameActivity extends AppCompatActivity {


    //Declaration
    BoardAdapter boardAdapter;
    TimerTask timerTask;
    Timer timer;
    Game game;

    //UI Declaration
    GridView gv_board;
    TextView txt_TimerLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_game);


        //Instantiate Game
        GameManager.startNewGame();
        game = GameManager.getGame();

        //Initialization
        gv_board = (GridView) findViewById(R.id.gridView);
        txt_TimerLabel = (TextView) findViewById(R.id.lbl_Timer);
        boardAdapter = new BoardAdapter(this, game);


        //Set the GridView adapter to the list of tiles.
        gv_board.setAdapter(boardAdapter);


        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(GameManager.getGame().getGameState() != GameState.IN_PROGRESS)
                    timerTask.cancel();

                String timestamp = new Timestamp(game.getPlayTimeMilliseconds()).toString();
                timestamp = timestamp.substring(14,19);
                txt_TimerLabel.setText(timestamp);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);


    }




}
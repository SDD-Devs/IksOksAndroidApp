package com.example.iksoksandroidapp.IksOksLogic.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.example.iksoksandroidapp.IksOksLogic.classic_backend.Game;
import com.example.iksoksandroidapp.IksOksLogic.classic_backend.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.R;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class ClassicGameActivity extends AppCompatActivity {


    //Declaration
    TimerTask timerTask;
    Timer timer;
    Game game;
    ClassicGameActivity classicGameActivity;

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
        GameManager.setClassicGameActivity(this);

        //Initialization
        txt_TimerLabel = (TextView) findViewById(R.id.lbl_Timer);


        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(GameManager.getGame().getGameState() != GameState.IN_PROGRESS)
                    timerTask.cancel();

                String timestamp = new Timestamp(game.getPlayTimeMilliseconds()).toString();
                timestamp = timestamp.substring(14,19);
                //txt_TimerLabel.setText(timestamp);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);


    }




}
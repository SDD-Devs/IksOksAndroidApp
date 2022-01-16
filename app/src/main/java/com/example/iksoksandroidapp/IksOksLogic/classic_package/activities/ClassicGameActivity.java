package com.example.iksoksandroidapp.IksOksLogic.classic_package.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.iksoksandroidapp.IksOksLogic.classic_package.backend.Game;
import com.example.iksoksandroidapp.IksOksLogic.classic_package.backend.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.R;

import java.lang.ref.SoftReference;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class ClassicGameActivity extends AppCompatActivity {

    //Declaration
    TimerTask timerTask;
    Game game;

    //UI Declaration
    TextView txt_TimerLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_game);

        //Instantiate Game
        GameManager.startNewGame();
        game = GameManager.getGame();
        GameManager.setClassicGameActivity(new SoftReference<>(this));

        //Initialization
        txt_TimerLabel = (TextView) findViewById(R.id.lbl_Timer);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (GameManager.getGame().getGameState() != GameState.IN_PROGRESS) {
                            timerTask.cancel();
                        }

                        String timestamp = new Timestamp(game.getPlayTimeMilliseconds()).toString();
                        timestamp = timestamp.substring(14, 19);

                        txt_TimerLabel.setText(timestamp);

                    }
                });
            }
        };
        new Timer().schedule(timerTask, 0, 100);

    }

}

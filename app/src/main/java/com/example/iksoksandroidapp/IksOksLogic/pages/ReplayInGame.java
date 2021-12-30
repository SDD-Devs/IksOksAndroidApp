package com.example.iksoksandroidapp.IksOksLogic.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.iksoksandroidapp.IksOksLogic.classic_backend.GameManager;
import com.example.iksoksandroidapp.R;

public class ReplayInGame extends Activity {

    //Declaration
    //Testirao replay dugme u igri
    Button btnReplayinGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_game);

        //Initialization
        //Testirao replay dugme u igri
        //btnReplayinGame = (Button) findViewById(R.id.btnReplayInGameNew);

        btnReplayinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ReplayBtnFunctionInGame();
            }
        });

    }

    private void ReplayBtnFunctionInGame() {
        Intent intent = new Intent(this, ClassicGameActivity.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}

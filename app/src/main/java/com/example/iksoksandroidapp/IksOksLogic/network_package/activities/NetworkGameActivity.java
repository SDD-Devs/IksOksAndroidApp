package com.example.iksoksandroidapp.IksOksLogic.network_package.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.iksoksandroidapp.IksOksLogic.classic_package.backend.Game;
import com.example.iksoksandroidapp.IksOksLogic.classic_package.backend.GameManager;
import com.example.iksoksandroidapp.R;

public class NetworkGameActivity extends AppCompatActivity {

    public static NetworkGameActivity instance;

    TextView roomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_network_game);

        roomTitle = (TextView) findViewById(R.id.txt_Title);
        String roomID = getIntent().getStringExtra("roomID");
        roomTitle.setText("Room:"+roomID);

        Game netGame = new Game();
        GameManager.startNewGame();


    }
}
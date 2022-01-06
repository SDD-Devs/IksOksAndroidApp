package com.example.iksoksandroidapp.IksOksLogic.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.iksoksandroidapp.IksOksLogic.classic_backend.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.net_backend.NetIksOksBoard;
import com.example.iksoksandroidapp.R;

public class PopUp extends Activity {

    //Declaration
    TextView txtResults;
    Button btnMenu;
    Button btnReplay;
    //Testirao replay dugme u igri
    //Button btnReplayinGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_layout);

        //Initialization
        txtResults = (TextView) findViewById(R.id.txtResultLabel);
        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnReplay = (Button) findViewById(R.id.btnReplay);
        //Testirao replay dugme u igri
        //btnReplayinGame = (Button) findViewById(R.id.btnReplayInGame);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.6), (int)(height*0.4));

        //Get the data from the Intent
        Bundle extras = getIntent().getExtras();
        txtResults.setText(extras.get("result").toString());

        btnMenu.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MenuBtnFunction();
                    }
                });

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplayBtnFunction();
            }
        });

    }

    private void MenuBtnFunction()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        NetworkSetupActivity.instance.client.sendMessage("EXIT-"+ NetworkSetupActivity.instance.client.getGameRoomId());
        Log.d("[NET]", "Exit signal sent.");
    }

    private void ReplayBtnFunction()
    {

        //Load an intent without screens animation of reloading.
        finish();
        GameManager.startNewGame();
        NetworkGameActivity.instance.findViewById(R.id.netIksOksBoard).invalidate();

    }

}

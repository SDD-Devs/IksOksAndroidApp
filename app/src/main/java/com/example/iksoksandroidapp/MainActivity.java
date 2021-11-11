package com.example.iksoksandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void openSettingsActivity (View view) {
        Intent enterSettings = new Intent(this, SettingsActivity.class);
        startActivity(enterSettings);
    }

    public void onButtonClassicClick (View view) {
        Intent enterClassic = new Intent(this, ClassicGameActivity.class);
        startActivity(enterClassic);
    }

    public void onButtonCustomClick (View view) {

    }

}
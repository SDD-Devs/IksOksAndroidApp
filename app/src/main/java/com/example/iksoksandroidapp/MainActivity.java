package com.example.iksoksandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;

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

    }

    public void onButtonCustomClick (View view) {

    }

}
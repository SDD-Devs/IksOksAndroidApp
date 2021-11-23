package com.example.iksoksandroidapp.IksOksLogic.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.iksoksandroidapp.BluetoothMode;
import com.example.iksoksandroidapp.R;

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

    public void onButtonBlueClick(View view) {
        Intent enterClassic = new Intent(this, BluetoothMode.class);
        startActivity(enterClassic);
    }
}
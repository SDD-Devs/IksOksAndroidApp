package com.example.iksoksandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Button themeSwitch = findViewById(R.id.themeChangeButton);
    }

    public void onButtonThemesClick (View view) {
        //SharedPreferences prefs = getSharedPreferences("theme", Context.MODE_PRIVATE);
        //int resId = prefs.getInt("resId", R.style.lightBlueTheme);
        //setTheme(resId);
    }
}
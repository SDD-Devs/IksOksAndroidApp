package com.example.iksoksandroidapp.IksOksLogic.mainpage_package.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.iksoksandroidapp.R;

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
package com.example.iksoksandroidapp.IksOksLogic.pages;

import android.app.Activity;
import android.app.GameManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.iksoksandroidapp.R;

public class PopUp extends Activity {

    //Declaration
    TextView txtResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_layout);

        //Initialization
        txtResults = (TextView) findViewById(R.id.txtResultLabel);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.6), (int)(height*0.4));

        Bundle extras = getIntent().getExtras();
        txtResults.setText(extras.get("result").toString());
    }
}

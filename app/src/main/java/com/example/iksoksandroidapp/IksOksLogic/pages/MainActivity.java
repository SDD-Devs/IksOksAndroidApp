package com.example.iksoksandroidapp.IksOksLogic.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

import com.example.iksoksandroidapp.R;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.testtheme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        startSettingsButtonRotation();

//        Animation animRotate = AnimationUtils.loadAnimation(anim, R.anim.rotate);

    }

    private void startSettingsButtonRotation(){

        ImageButton imageButton = (ImageButton) findViewById(R.id.settingsButton);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);

        rotateAnimation.setInterpolator(new OvershootInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(10000);
        imageButton.startAnimation(rotateAnimation);

    }

    public void openSettingsActivity(View view) {
        Intent enterSettings = new Intent(this, SettingsActivity.class);
        startActivity(enterSettings);
    }

    public void onButtonClassicClick(View view) {
        Intent enterClassic = new Intent(this, ClassicGameActivity.class);
        startActivity(enterClassic);
    }

    public void onButtonNetworkClick(View view)
    {
        Intent intent = new Intent(this, NetworkSetupActivity.class);
        startActivity(intent);
    }
    public void onButtonBluetoothClick(View view) {
        Intent intent = new Intent(this, BluetoothSetupActivity.class);
        startActivity(intent);
    }

}
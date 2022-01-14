package com.example.iksoksandroidapp.IksOksLogic.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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

//        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.testtheme);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();

        startSettingsButtonRotation();

//        Animation animRotate = AnimationUtils.loadAnimation(anim, R.anim.rotate);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        unregisterReceiver(broadcastReceiverBluetoothAdapterActionStateChanged);

    }

    private void startSettingsButtonRotation() {

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

    public void onButtonNetworkClick(View view) {
        Intent intent = new Intent(this, NetworkSetupActivity.class);
        startActivity(intent);
    }

    public void onButtonBluetoothClick(View view) {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {

            registerReceiver(broadcastReceiverBluetoothAdapterActionStateChanged, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

        } else {

            Intent bluetoothSetupIntent = new Intent(MainActivity.this, BluetoothSetupActivity.class);
            startActivity(bluetoothSetupIntent);

        }

    }

    private final BroadcastReceiver broadcastReceiverBluetoothAdapterActionStateChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR) == BluetoothAdapter.STATE_ON) {

                    Intent bluetoothSetupIntent = new Intent(MainActivity.this, BluetoothSetupActivity.class);
                    startActivity(bluetoothSetupIntent);

                }
            }

        }
    };

}
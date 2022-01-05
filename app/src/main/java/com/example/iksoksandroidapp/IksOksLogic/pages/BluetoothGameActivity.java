package com.example.iksoksandroidapp.IksOksLogic.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.iksoksandroidapp.IksOksLogic.blue_backend.BlueIksOksBoard;
import com.example.iksoksandroidapp.IksOksLogic.blue_backend.BluetoothConnectionService;
import com.example.iksoksandroidapp.IksOksLogic.blue_backend.BluetoothManager;
import com.example.iksoksandroidapp.IksOksLogic.classic_backend.Game;
import com.example.iksoksandroidapp.IksOksLogic.classic_backend.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.R;

import org.parceler.Parcels;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BluetoothGameActivity extends AppCompatActivity {


    //Constants
    private final static String TAG = "[DEBUG/IksOks]";


    //GUI Controls
    TextView txt_TimerLabel;
    BlueIksOksBoard blueIksOksBoard;


    //Declaration
    TimerTask timerTask;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_classic);

        //Initialization
        txt_TimerLabel = (TextView) findViewById(R.id.lbl_Timer);
        blueIksOksBoard = (BlueIksOksBoard) findViewById(R.id.blueIksOksBoard);

        IntentFilter filterCmd = new IntentFilter("incomingMessage");
        LocalBroadcastManager.getInstance(this).registerReceiver(CommandReceiver, filterCmd);

        //Instantiate Game
        GameManager.startNewGame();


        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (GameManager.getGame().getGameState() != GameState.IN_PROGRESS)
                    timerTask.cancel();

                String timestamp = new Timestamp(GameManager.getGame().getPlayTimeMilliseconds()).toString();
                timestamp = timestamp.substring(14, 19);
                //txt_TimerLabel.setText(timestamp);
            }
        };
        new Timer().schedule(timerTask, 0, 1000);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(CommandReceiver);
    }


    //Broadcast receiver
    BroadcastReceiver CommandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("theMessage");
            Log.d(TAG, "Received from Bluetooth device: " + text);

            int position = Integer.parseInt(text);
            GameManager.getGame().playTile(position);
            blueIksOksBoard.invalidate();
        }
    };

}
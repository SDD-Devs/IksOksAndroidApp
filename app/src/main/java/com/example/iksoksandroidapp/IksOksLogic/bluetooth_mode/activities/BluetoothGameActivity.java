package com.example.iksoksandroidapp.IksOksLogic.bluetooth_mode.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.iksoksandroidapp.IksOksLogic.bluetooth_mode.backend.BluetoothConnectionService;
import com.example.iksoksandroidapp.IksOksLogic.bluetooth_mode.backend.BluetoothGame;
import com.example.iksoksandroidapp.IksOksLogic.classic_mode.backend.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;
import com.example.iksoksandroidapp.R;

import java.util.concurrent.ThreadLocalRandom;

public class BluetoothGameActivity extends AppCompatActivity {

    private final static String TAG = "[DBG]";

    boolean isServer;
    BluetoothGame bluetoothGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_classic);

        isServer = getIntent().getBooleanExtra("isServer", isServer);

        GameManager.startNewBluetoothGame();
        bluetoothGame = (BluetoothGame) GameManager.getGame();

        if (isServer) {

            //If coinFlip is true, server is X
            boolean coinFlip = ThreadLocalRandom.current().nextBoolean();

            if (coinFlip) {
                BluetoothConnectionService.instance.get().write("GAME-SERVER");
                bluetoothGame.setPlayerType(PlayerType.IKS);
                Log.d(TAG, "Server will be IKS ");
            } else {
                BluetoothConnectionService.instance.get().write("GAME-CLIENT");
                bluetoothGame.setPlayerType(PlayerType.OKS);
                Log.d(TAG, "Server will be OKS");
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverInputStream, new IntentFilter("incomingMessage"));

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverInputStream);
    
    }

    private final BroadcastReceiver broadcastReceiverInputStream = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "[BluetoothGameActivity]onReceive: " + intent.getStringExtra("theMessage"));
            String[] messageArray = intent.getStringExtra("theMessage").trim().split("-");

            if ( ((BluetoothGame) GameManager.getGame()).getPlayerType() != null){
                return;
            }

            if (messageArray[1].equals("SERVER") && !isServer) {
                bluetoothGame.setPlayerType(PlayerType.OKS);
                Log.d(TAG, "Client will be OKS. ");
            }else {
                bluetoothGame.setPlayerType(PlayerType.IKS);
                Log.d(TAG, "Client will be IKS");
            }

        }
    };

}
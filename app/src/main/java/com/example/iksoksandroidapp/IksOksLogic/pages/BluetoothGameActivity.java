package com.example.iksoksandroidapp.IksOksLogic.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.iksoksandroidapp.IksOksLogic.bluetooth_backend.BluetoothConnectionService;
import com.example.iksoksandroidapp.IksOksLogic.bluetooth_backend.BluetoothGameManager;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;
import com.example.iksoksandroidapp.R;

import java.util.concurrent.ThreadLocalRandom;

public class BluetoothGameActivity extends AppCompatActivity {

    private final static String TAG = "BluGamAct";

    boolean isServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_classic);

        isServer = getIntent().getBooleanExtra("isServer", isServer);

        BluetoothGameManager.startNewGame();

        //Coinflip
        //If coinFlip is true, server is X
        boolean coinFlip = ThreadLocalRandom.current().nextBoolean();
        if (isServer) {
            if (coinFlip) {
                BluetoothConnectionService.instance.get().write("GAME-SERVER");
                BluetoothGameManager.getGame().setPlayerType(PlayerType.IKS);
            } else {
                BluetoothConnectionService.instance.get().write("GAME-CLIENT");
                BluetoothGameManager.getGame().setPlayerType(PlayerType.OKS);
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

            Log.d(TAG, "onReceive: " + intent.getStringExtra("theMessage"));
            String[] messageArray = intent.getStringExtra("theMessage").trim().split("-");

            if (!messageArray[0].equals("GAME")) {
                return;
            }

            if (messageArray[1].equals("SERVER") && !isServer) {
                BluetoothGameManager.getGame().setPlayerType(PlayerType.OKS);
            }

        }
    };

}
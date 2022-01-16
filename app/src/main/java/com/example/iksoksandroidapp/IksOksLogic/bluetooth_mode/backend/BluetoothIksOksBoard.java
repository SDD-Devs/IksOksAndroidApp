package com.example.iksoksandroidapp.IksOksLogic.bluetooth_mode.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.iksoksandroidapp.IksOksLogic.classic_mode.backend.Game;
import com.example.iksoksandroidapp.IksOksLogic.classic_mode.backend.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.classic_mode.backend.IksOksBoard;


public class BluetoothIksOksBoard extends IksOksBoard {

    private static final String TAG = "[DBG]";

    public BluetoothIksOksBoard(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiverInputStream, new IntentFilter("incomingMessage"));

    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        BluetoothGame bluetoothGame = (BluetoothGame) GameManager.getGame();

        Log.d(TAG, "Current turn: " + bluetoothGame.getCurrentTurn());
        if (!bluetoothGame.getCurrentTurn().equals(bluetoothGame.getPlayerType())) {
            return false;
        }

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        int action = motionEvent.getAction();

        if (action == MotionEvent.ACTION_DOWN) {

            int row = (int) Math.ceil(y / cellSize) - 1;
            int column = (int) Math.ceil(x / cellSize) - 1;
            int position = oneDimensionalFromTwo(row, column);

            if (!bluetoothGame.playTile(position)) {
                return false;
            }

            invalidate();

            BluetoothConnectionService.instance.get().write("GAME-" + position);

            return true;

        }

        return false;

    }

    private final BroadcastReceiver broadcastReceiverInputStream = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String received = intent.getStringExtra("theMessage");
            String[] messageArray = received.trim().split("-");
            Log.d(TAG, "onReceive: " + received);

            if (!messageArray[0].equals("GAME")) {
                return;
            }

            try {

                Game game = GameManager.getGame();

                int playedPosition = Integer.parseInt(messageArray[1]);

                game.playTile(playedPosition);

                invalidate();

            } catch (Exception ignored) {
            }

        }
    };

}

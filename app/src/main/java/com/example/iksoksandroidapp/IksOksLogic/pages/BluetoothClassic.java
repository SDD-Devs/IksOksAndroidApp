package com.example.iksoksandroidapp.IksOksLogic.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.iksoksandroidapp.IksOksLogic.blue_backend.BluetoothConnectionService;
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

public class BluetoothClassic extends AppCompatActivity {

    //Constants
    private final static String TAG = "[DEBUG/IksOks]";

    //GUI Controls
    Button btnSend;
    BluetoothConnectionService mBluetoothConnectionService;


    //Declaration
    TimerTask timerTask;
    Timer timer;
    Game game;

    //UI Declaration
    TextView txt_TimerLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_classic);


        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendCommand();
            }
        });

        //Instantiate Game
        GameManager.startNewGame();
        game = GameManager.getGame();

        //Initialization
        txt_TimerLabel = (TextView) findViewById(R.id.lbl_Timer);


        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(GameManager.getGame().getGameState() != GameState.IN_PROGRESS)
                    timerTask.cancel();

                String timestamp = new Timestamp(game.getPlayTimeMilliseconds()).toString();
                timestamp = timestamp.substring(14,19);
                //txt_TimerLabel.setText(timestamp);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);



    }

    private final BroadcastReceiver CommandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cmd = intent.getStringExtra("theMessage");
            Log.d("[COOmmmmmmmAND]", cmd);
        }
    };

    public void sendCommand() {
        //Logika koje polje je kliknuto (Darkic)


        //Parsiranje
        String cmd = "Cmd 1";
        byte[] bytes = cmd.getBytes(Charset.defaultCharset());

        //Slanje
        mBluetoothConnectionService.write(bytes);
    }

    private boolean startBTConnection(BluetoothDevice mDevice, UUID myUuidInsecure) {
        Log.d(TAG, "startBTConnection: Initiialize RFCOM Bluetooth Connection");
        if(mDevice != null){
            mBluetoothConnectionService.startClient(mDevice, myUuidInsecure);
            return true;
        }else {
            return false;
        }
    }
}
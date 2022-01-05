package com.example.iksoksandroidapp.IksOksLogic.net_backend;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.iksoksandroidapp.IksOksLogic.NetworkGameActivity;
import com.example.iksoksandroidapp.IksOksLogic.pages.BluetoothSetupActivity;
import com.example.iksoksandroidapp.IksOksLogic.pages.NetworkSetupActivity;
import com.example.iksoksandroidapp.R;

import java.io.DataInputStream;

public class ClientListenThread implements Runnable {

    DataInputStream in;
    private volatile boolean listenThreadRunning = true;



    public ClientListenThread(DataInputStream din) {
        in = din;
    }

    @Override
    public void run() {

        while (listenThreadRunning) {
            try {

                byte[] buffer = new byte[150];
                in.read(buffer);

                //String received = in.readUTF();

                StringBuilder sb = new StringBuilder("");
                System.out.println("Client Received: ");
                for (int i = 2; i < buffer.length; i++) {
                    //System.out.print((char) buffer[i]);
                    sb.append((char)buffer[i]);
                }

                String cmd =  sb.toString().trim();
                String[] cmdArr = cmd.split("-");

                //Proccess received string
                switch(cmdArr[0]){
                    case "RCREATED":
                        Log.d("[NET]", "Room created. Open activity");
                        Intent cGameIntent = new Intent(NetworkSetupActivity.instance, NetworkGameActivity.class);
                        cGameIntent.putExtra("roomID", cmdArr[1]);
                        NetworkSetupActivity.instance.startActivity(cGameIntent);
                        break;
                    case "RJOINED":
                        Log.d("[NET]", "Room Joined. open activity.");
                        Intent jGameIntent = new Intent(NetworkSetupActivity.instance, NetworkGameActivity.class);
                        jGameIntent.putExtra("roomID", cmdArr[1]);
                        NetworkSetupActivity.instance.startActivity(jGameIntent);
                        break;
                    default:
                        Log.d("[NET]", "Received: " + cmd);
                        break;
                }


            } catch (Exception e) {
                System.out.println("[EXCEPTION] connection problem");
                listenThreadRunning = false;

            }
        }
    }
}

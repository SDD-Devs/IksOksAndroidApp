package com.example.iksoksandroidapp.IksOksLogic.network_package.backend;

import android.content.Intent;
import android.util.Log;

import com.example.iksoksandroidapp.IksOksLogic.network_package.activities.NetworkGameActivity;
import com.example.iksoksandroidapp.IksOksLogic.classic_package.backend.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.network_package.activities.NetworkSetupActivity;
import com.example.iksoksandroidapp.R;

import java.io.DataInputStream;

public class ClientListenThread implements Runnable {

    DataInputStream in;
    private volatile boolean listenThreadRunning = true;
    NetIksOksBoard netIksOksBoard;


    public ClientListenThread(DataInputStream din) {
        in = din;
    }

    @Override
    public void run() {

        while (listenThreadRunning) {
            try {

                byte[] buffer = new byte[20];
                in.read(buffer);

                //String received = in.readUTF();

                StringBuilder sb = new StringBuilder("");
                System.out.println("Client Received: ");
                for (int i = 2; i < buffer.length; i++) {
                    //System.out.print((char) buffer[i]);
                    sb.append((char)buffer[i]);
                }

                String cmd =  sb.toString().trim();
                Log.d("[NET]", "Received: " + cmd);
                String[] cmdArr = cmd.split("-");

                //Proccess received string
                switch(cmdArr[0]){
                    case "RCREATED":
                        Log.d("[NET]", "Room created. Open activity");
                        NetworkSetupActivity.instance.client.gameRoomId = Integer.parseInt(cmdArr[1]);
                        Intent cGameIntent = new Intent(NetworkSetupActivity.instance, NetworkGameActivity.class);
                        cGameIntent.putExtra("roomID", cmdArr[1]);
                        NetworkSetupActivity.instance.startActivity(cGameIntent);
                        break;
                    case "RJOINED":
                        Log.d("[NET]", "Room Joined. open activity.");
                        NetworkSetupActivity.instance.client.gameRoomId = Integer.parseInt(cmdArr[1]);
                        Intent jGameIntent = new Intent(NetworkSetupActivity.instance, NetworkGameActivity.class);
                        jGameIntent.putExtra("roomID", cmdArr[1]);
                        NetworkSetupActivity.instance.startActivity(jGameIntent);
                        break;

                    case "GO":
                        //Disable clicking
                        Log.d("[NET]","This device SHOULD GO this turn");
                        NetworkSetupActivity.instance.client.yourTurn = true;
                        break;

                    case "WAIT":
                        Log.d("[NET]","This device SHOULD NOT GO this turn");
                        //Enable clicking
                        NetworkSetupActivity.instance.client.yourTurn = false;
                        break;

                    case "SE":
                        netIksOksBoard = (NetIksOksBoard) NetworkGameActivity.instance.findViewById(R.id.netIksOksBoard);
                        Log.d("[NET]", "About to play a bluetoothTile #" + Integer.parseInt(cmdArr[1]));
                        GameManager.getGame().playTile(Integer.parseInt(cmdArr[1]));
                        netIksOksBoard.postInvalidate();
                        break;
                }


            } catch (Exception e) {
                System.out.println("[EXCEPTION] connection problem");
                listenThreadRunning = false;
            }
        }
    }
}

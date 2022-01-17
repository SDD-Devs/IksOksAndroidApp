package com.example.iksoksandroidapp.IksOksLogic.network_mode.backend;

import android.content.Intent;
import android.util.Log;

import com.example.iksoksandroidapp.IksOksLogic.main_page.activities.MainActivity;
import com.example.iksoksandroidapp.IksOksLogic.network_mode.activities.NetworkSetupActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Client implements Runnable{

    private Socket cSocket;
    private DataOutputStream out;
    private DataInputStream in;
    int gameRoomId;
    public static boolean yourTurn = false;
    private String ip;
    private String TAG = "[NET]";
    int port;
    private boolean flag;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }


    @Override
    public void run() {

        Log.d(TAG, "Hello, This is client.");
        try {
            cSocket = new Socket(ip, port);
            out =new DataOutputStream(cSocket.getOutputStream());
            in= new DataInputStream(cSocket.getInputStream());
            new Thread(new ClientListenThread(in)).start();
        } catch (IOException e) {
            Log.d(TAG, "[EXCEPTION] While setting up the client.");
        }


    }


    public boolean sendMessage(String msg){

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeUTF(msg);
                    new Thread(new ClientListenThread(in)).start();
                } catch (Exception e) {
                    e.printStackTrace();
                    flag = false;
                }
            }
        });


        t1.start();

        return flag;
    }


    //Getters and Setters
    public int getGameRoomId() {
        return gameRoomId;
    }
}

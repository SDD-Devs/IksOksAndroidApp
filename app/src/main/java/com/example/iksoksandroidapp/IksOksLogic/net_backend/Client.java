package com.example.iksoksandroidapp.IksOksLogic.net_backend;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Client implements Runnable{

    private Socket cSocket;
    private DataOutputStream out;
    private DataInputStream in;
    String TAG = "[NET]";

    public Client(){

    }


    @Override
    public void run() {

        Log.d(TAG, "Hello, This is client.");
        try {
            cSocket = new Socket("192.168.0.105", 4999);
            out =new DataOutputStream(cSocket.getOutputStream());
            in= new DataInputStream(cSocket.getInputStream());
            new Thread(new ClientListenThread(in)).start();
        } catch (IOException e) {
            Log.d(TAG, "[EXCEPTION] While setting up the client.");
        }


    }


    public void sendMessage(String msg){

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeUTF(msg);
                    new Thread(new ClientListenThread(in)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();


    }


}

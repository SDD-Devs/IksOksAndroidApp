package com.example.iksoksandroidapp.IksOksLogic.blue_backend;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.parceler.Parcel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.UUID;


public class BluetoothConnectionService implements Serializable {


    //Constants
    private final static String TAG = "[DEBUG/IksOks]";
    private static final String appName = "MYAPP";
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");


    //
    Context myContext;
    private BluetoothAdapter blueAdapter;
    private BluetoothDevice myDevice;
    private UUID deviceUUID;
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private ProgressDialog mProgressDialog;


    public BluetoothDevice getMyDevice() {
        return myDevice;
    }

    public void setMyDevice(BluetoothDevice myDevice) {
        this.myDevice = myDevice;
    }


    public BluetoothConnectionService(Context context){
        myContext = context;
        blueAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }




    private class AcceptThread extends Thread{

        //Data members
        private BluetoothServerSocket blueServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try{
                tmp = blueAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Setting up server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.d(TAG, "[EXCEPTION] while setting up the server. ");
            }

            blueServerSocket = tmp;
        }

        public  void run() {
            Log.d(TAG, "AcceptThread Running...");
            BluetoothSocket blueSocket = null;

            try{
                Log.d(TAG,"run: RFCOM server socket start.");
                blueSocket = blueServerSocket.accept();
                Log.d(TAG,"run: RFCOM server socket accepted the connection.");
            }catch (IOException e) {
                e.printStackTrace();
            }

            if(blueSocket != null)
            {
                connected(blueSocket, myDevice);
            }
        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.");
            try {
                blueServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
            }
        }
    }



    private class ConnectThread extends Thread{

        BluetoothSocket blueSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            Log.d(TAG, "Connect Thread initiated.");
            myDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;

            try{
                Log.d(TAG, "Trying to create InsecureRFcommSocket using UUID");
                tmp = myDevice.createInsecureRfcommSocketToServiceRecord(deviceUUID);
            }catch (IOException e){
                Log.d(TAG, "[EXCEPTION] Failed to create InsecureRFcommSocket using UUID");
            }

            blueSocket = tmp;
            blueAdapter.cancelDiscovery();

            try{
                blueSocket.connect();
                Log.d(TAG, "Connection Successful.");
            }catch (IOException e) {
                Log.d(TAG, "[EXCEPTION] Could not connect to other device.");
                try{
                    blueSocket.close();
                }catch (IOException ex){
                    Log.d(TAG, "[EXCEPTION] Unable to close the connection socket");
                }
                Log.d(TAG, "Failed to make a connection.");
            }

            connected(blueSocket, myDevice);
        }

        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                blueSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }

    }



    private class ConnectedThread extends Thread {
        private final BluetoothSocket blueSocket;
        private final InputStream blueInStream;
        private final OutputStream blueOutStream;


        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");

            blueSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            //dismiss the progressdialog when connection is established
            try{
                mProgressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            try {
                tmpIn = blueSocket.getInputStream();
                tmpOut = blueSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }


            blueInStream = tmpIn;
            blueOutStream = tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];  // buffer store for the stream

            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                try {
                    bytes = blueInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);

                    Intent incomingMessageIntent = new Intent("incomingMessage");
                    incomingMessageIntent.putExtra("theMessage", incomingMessage);
                    LocalBroadcastManager.getInstance(myContext).sendBroadcast(incomingMessageIntent);

                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                    break;
                }
            }
        }

        //Call this from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                blueOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                blueSocket.close();
            } catch (IOException e) { }
        }

    }


    //Aditional Methods

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        //perform the write
        mConnectedThread.write(out);
    }

    public void startClient(BluetoothDevice device,UUID uuid){
        Log.d(TAG, "startClient: Started.");

        //initprogress dialog
        mProgressDialog = ProgressDialog.show(myContext,"Connecting Bluetooth"
                ,"Please Wait...",true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }



}
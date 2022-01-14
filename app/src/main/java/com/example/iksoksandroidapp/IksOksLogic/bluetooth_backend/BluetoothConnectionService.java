package com.example.iksoksandroidapp.IksOksLogic.bluetooth_backend;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.iksoksandroidapp.IksOksLogic.pages.ClassicGameActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {

    public static SoftReference<BluetoothConnectionService> instance;

    private static final String TAG = "BluConSer";
    private static final String appName = "IksOks";
    public static final UUID MY_UUID_INSECURE = UUID.fromString("15291949-c047-4693-b19c-30fee0078724");

    private final BluetoothAdapter bluetoothAdapter;
    Context context;
    ProgressDialog progressDialog;

    private AcceptThread acceptThread;

    private ConnectThread connectThread;
    private BluetoothDevice bluetoothDevice;
    private UUID deviceUUID;

    private ConnectedThread connectedThread;

    public BluetoothConnectionService(Context context) {

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;

        instance = new SoftReference<>(this);

    }

    public synchronized void startServer() {

        Log.d(TAG, "startServer: Starting server.");

        acceptThread = new AcceptThread();
        acceptThread.start();

    }

    public synchronized void startClient(BluetoothDevice bluetoothDevice1, UUID uuid) {

        Log.d(TAG, "startClient: Starting client.");

        connectThread = new ConnectThread(bluetoothDevice1, uuid);
        connectThread.start();

    }

    private void connected(BluetoothSocket bluetoothSocket, boolean isServer) {

        Log.d(TAG, "connected: Starting.");

        connectedThread = new ConnectedThread(bluetoothSocket, isServer);
        connectedThread.start();

    }

    public void write(String out) {

        Log.d(TAG, "write: Write called.");

        connectedThread.write(out.getBytes(Charset.defaultCharset()));

    }

    private class AcceptThread extends Thread {

        private BluetoothServerSocket bluetoothServerSocket;

        public AcceptThread() {

            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Setting up server using: " + MY_UUID_INSECURE);
            } catch (IOException ioException) {
                Log.d(TAG, "AcceptThread: IOException: " + ioException.getMessage());
            }

        }

        public void run() {

            Log.d(TAG, "AcceptThread: Running.");
            BluetoothSocket bluetoothSocket = null;

            try {
                Log.d(TAG, "AcceptThread: RFCOMM server socket start.");
                bluetoothSocket = bluetoothServerSocket.accept();
                Log.d(TAG, "AcceptThread: RFCOMM server socket accepted connection.");
            } catch (IOException ioException) {
                Log.d(TAG, "AcceptThread: IOException: " + ioException.getMessage());
            }

            if (bluetoothSocket == null) {
                return;
            }

            connected(bluetoothSocket, false);

        }

    }

    private class ConnectThread extends Thread {

        private BluetoothSocket bluetoothSocket;

        public ConnectThread(BluetoothDevice bluetoothDevice1, UUID uuid) {

            Log.d(TAG, "ConnectThread: Started.");
            bluetoothDevice = bluetoothDevice1;
            deviceUUID = uuid;

        }

        public void run() {

            Log.d(TAG, "ConnectThread: Running.");

            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: " + MY_UUID_INSECURE);
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException ioException) {
                Log.d(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + ioException.getMessage());
            }

            bluetoothAdapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
                Log.d(TAG, "ConnectThread: Connected.");
            } catch (IOException ioException) {
                try {
                    bluetoothSocket.close();
                    Log.d(TAG, "ConnectThread: Closed socket.");
                } catch (IOException ioException1) {
                    Log.d(TAG, "ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE);
                }
                Log.d(TAG, "ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE);
            }

            connected(bluetoothSocket, true);

        }

        public void cancel() {

            try {
                Log.d(TAG, "ConnectThread: Closing client socket.");
                bluetoothSocket.close();
            } catch (IOException ioException) {
                Log.d(TAG, "ConnectThread: close() of socket failed. " + ioException.getMessage());
            }

        }

    }

    private class ConnectedThread extends Thread {

        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        private final boolean isServer;

        public ConnectedThread(BluetoothSocket bluetoothSocket1, boolean isServer) {

            Log.d(TAG, "ConnectedThread: Starting.");

            bluetoothSocket = bluetoothSocket1;
            this.isServer = isServer;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            try {
                tmpIn = bluetoothSocket.getInputStream();
                tmpOut = bluetoothSocket.getOutputStream();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            inputStream = tmpIn;
            outputStream = tmpOut;

        }

        public void run() {

            byte[] buffer = new byte[1024];
            int bytes;

            if (isServer) {
                write(("PREGAME-CLIENT").getBytes(Charset.defaultCharset()));
            } else {
                write(("PREGAME-SERVER").getBytes(Charset.defaultCharset()));
            }

            while (true) {

                try {

                    bytes = inputStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "ConnectedThread: InputStream: " + incomingMessage);

                    Intent incomingMessageIntent = new Intent("incomingMessage");
                    incomingMessageIntent.putExtra("theMessage", incomingMessage);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(incomingMessageIntent);

                } catch (IOException ioException) {
                    Log.d(TAG, "ConnectedThread: Error reading InputStream. " + ioException.getMessage());
                    break;
                }

            }

        }

        public void write(byte[] bytes) {

            String text = new String(bytes, Charset.defaultCharset());

            Log.d(TAG, "ConnectedThread: OutputStream: " + text);

            try {
                outputStream.write(bytes);
            } catch (IOException ioException) {
                Log.d(TAG, "ConnectedThread: Error writing to OutputStream. " + ioException.getMessage());
            }

        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

}

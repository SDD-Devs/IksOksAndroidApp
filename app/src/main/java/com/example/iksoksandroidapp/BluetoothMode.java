package com.example.iksoksandroidapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class BluetoothMode extends AppCompatActivity {


    private static final int REQUEST_ENABLE_BT = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_mode);


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);


        BluetoothAdapter bluetootAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetootAdapter == null){
            Log.d("bluetooth", "Device does not support bluetooth");
            return;
        }

        if(!bluetootAdapter.isEnabled()){
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_ENABLE_BT);
        }


        Set<BluetoothDevice> pairedDevices = bluetootAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice bd : pairedDevices){
                String deviceName = bd.getName();
                String deviceHardwareAddress = bd.getAddress();
            }
        }

    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

}
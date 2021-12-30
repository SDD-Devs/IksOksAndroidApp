package com.example.iksoksandroidapp.IksOksLogic.blue_backend;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.example.iksoksandroidapp.IksOksLogic.pages.DeviceListAdapter;

import java.util.ArrayList;
import java.util.UUID;

public class BluetoothManager {

    //Constants
    public final static String TAG = "[DEBUG/IksOks]";
    public static final int REQUEST_ENABLE_BT = 999;
    public static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");


    //Bluetooth Setup Variables
    BluetoothAdapter blueAdapter;
    DeviceListAdapter mDeviceListAdapter;
    ArrayList<BluetoothDevice> availableDevices;
    BluetoothDevice myBlueDevice;

    //Bluetooth Transfer Data Variables
    BluetoothConnectionService mBluetoothConnectionService;


}

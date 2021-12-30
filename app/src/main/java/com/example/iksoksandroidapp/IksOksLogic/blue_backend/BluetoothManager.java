package com.example.iksoksandroidapp.IksOksLogic.blue_backend;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.example.iksoksandroidapp.IksOksLogic.pages.DeviceListAdapter;

import java.util.ArrayList;
import java.util.UUID;

public class BluetoothManager {

    //Constants
    private final static String TAG = "[DEBUG/IksOks]";
    private static final int REQUEST_ENABLE_BT = 999;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    //Bluetooth Setup Variables
    private BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
    private DeviceListAdapter mDeviceListAdapter;
    private ArrayList<BluetoothDevice> availableDevices = new ArrayList<>();
    private BluetoothDevice myBlueDevice;

    //Bluetooth Transfer Data Variables
    private static BluetoothConnectionService mBluetoothConnectionService;





    public static int getRequestEnableBt() {
        return REQUEST_ENABLE_BT;
    }

    public static UUID getMyUuidInsecure() {
        return MY_UUID_INSECURE;
    }

    public BluetoothAdapter getBlueAdapter() {
        return blueAdapter;
    }

    public void setBlueAdapter(BluetoothAdapter blueAdapter) {
        this.blueAdapter = blueAdapter;
    }

    public DeviceListAdapter getmDeviceListAdapter() {
        return mDeviceListAdapter;
    }

    public void setmDeviceListAdapter(DeviceListAdapter mDeviceListAdapter) {
        this.mDeviceListAdapter = mDeviceListAdapter;
    }

    public ArrayList<BluetoothDevice> getAvailableDevices() {
        return availableDevices;
    }

    public void setAvailableDevices(ArrayList<BluetoothDevice> availableDevices) {
        this.availableDevices = availableDevices;
    }

    public BluetoothDevice getMyBlueDevice() {
        return myBlueDevice;
    }

    public void setMyBlueDevice(BluetoothDevice myBlueDevice) {
        this.myBlueDevice = myBlueDevice;
    }

    public static BluetoothConnectionService getmBluetoothConnectionService() {
        return mBluetoothConnectionService;
    }
    public static void setmBluetoothConnectionService(BluetoothConnectionService mBluetoothConnectionService) {
        BluetoothManager.mBluetoothConnectionService = mBluetoothConnectionService;
    }




}

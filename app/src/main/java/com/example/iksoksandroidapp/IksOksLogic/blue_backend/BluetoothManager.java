package com.example.iksoksandroidapp.IksOksLogic.blue_backend;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.iksoksandroidapp.IksOksLogic.pages.BluetoothGameActivity;
import com.example.iksoksandroidapp.IksOksLogic.pages.BluetoothSetupActivity;
import com.example.iksoksandroidapp.IksOksLogic.pages.DeviceListAdapter;

import java.util.ArrayList;
import java.util.UUID;


public class BluetoothManager {

    //Constants
    public static final String TAG = "[DEBUG/IksOks]";
    public static final String appName = "MYAPP";
    public static final int REQUEST_ENABLE_BT = 999;
    public static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");


    //Bluetooth Setup Variables
    private static BluetoothAdapter blueAdapter;
    private static DeviceListAdapter mDeviceListAdapter;
    private static ArrayList<BluetoothDevice> availableDevices = new ArrayList<>();
    private static BluetoothDevice myBlueDevice;

    //Bluetooth Transfer Data Variables
    private static BluetoothConnectionService mBluetoothConnectionService;
    private static Context BlueSetupContext;
    private static ArrayList<BroadcastReceiver> bReceivers = new ArrayList<>();



    public static void loadContext(Context context) {
        BlueSetupContext = context;
    }

    public static ArrayList<BroadcastReceiver> getbReceivers() {
        return bReceivers;
    }

    public static BluetoothAdapter getBlueAdapter() {
        return blueAdapter;
    }

    public static DeviceListAdapter getmDeviceListAdapter() {
        return mDeviceListAdapter;
    }

    public static ArrayList<BluetoothDevice> getAvailableDevices() {
        return availableDevices;
    }

    public static BluetoothDevice getMyBlueDevice() {
        return myBlueDevice;
    }

    public static BluetoothConnectionService getmBluetoothConnectionService() {
        return mBluetoothConnectionService;
    }

    public static Context getBlueSetupContext() {
        return BlueSetupContext;
    }

    public static void setBlueAdapter(BluetoothAdapter blueAdapter) {
        BluetoothManager.blueAdapter = blueAdapter;
    }

    public static void setmDeviceListAdapter(DeviceListAdapter mDeviceListAdapter) {
        BluetoothManager.mDeviceListAdapter = mDeviceListAdapter;
    }

    public static void setAvailableDevices(ArrayList<BluetoothDevice> availableDevices) {
        BluetoothManager.availableDevices = availableDevices;
    }

    public static void setMyBlueDevice(BluetoothDevice myBlueDevice) {
        BluetoothManager.myBlueDevice = myBlueDevice;
    }

    public static void setmBluetoothConnectionService(BluetoothConnectionService mBluetoothConnectionService) {
        BluetoothManager.mBluetoothConnectionService = mBluetoothConnectionService;
    }

    public static void setBlueSetupContext(Context blueSetupContext) {
        BlueSetupContext = blueSetupContext;
    }

    public static void setbReceivers(ArrayList<BroadcastReceiver> bReceivers) {
        BluetoothManager.bReceivers = bReceivers;
    }


    //Methods for Event Handlers

    public static void BlueDiscoverability() {
        Intent DiscoverabilityIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        DiscoverabilityIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
        BlueSetupContext.startActivity(DiscoverabilityIntent);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        BlueSetupContext.registerReceiver(bReceivers.get(0),filter);

        Toast t = Toast.makeText(BlueSetupContext,"Making device discoverable for 2 minutes.", Toast.LENGTH_LONG);
        t.show();
    }

    public static void BlueDiscover() {
        Log.d(TAG, "Looking for unpaired devices.");

        if(availableDevices != null)
            availableDevices.clear();

        if(blueAdapter.isDiscovering()){
            //If already discovering start over
            blueAdapter.cancelDiscovery();

            //Check Bluetooth Permission
            checkBTPermissions();

            blueAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            BlueSetupContext.registerReceiver(bReceivers.get(1), filter);

        }else if(!blueAdapter.isDiscovering())
        {
            //Check Bluetooth Permission
            checkBTPermissions();

            blueAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            BlueSetupContext.registerReceiver(bReceivers.get(1), filter);
        }
    }

    public static void InitiateConnection() {
        if(startBTConnection(myBlueDevice, MY_UUID_INSECURE)){
            Intent intent = new Intent(BlueSetupContext, BluetoothGameActivity.class);
            BlueSetupContext.startActivity(intent);
        }
    }



    //Additional Methods
//Additional Methods
    private static void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {
            Log.d(TAG, "Checking SDK version.");
            int permissionCheck = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck = BlueSetupContext.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
                permissionCheck += BlueSetupContext.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            }
            if (permissionCheck != 0) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //BlueSetupContext.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
                }
            }
        }else
        {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private static boolean startBTConnection(BluetoothDevice mDevice, UUID myUuidInsecure) {
        Log.d(TAG, "startBTConnection: Initiialize RFCOM Bluetooth Connection");
        if(mDevice != null){
            mBluetoothConnectionService.startClient(mDevice, myUuidInsecure);
            return true;
        }else {
            return false;
        }
    }
}
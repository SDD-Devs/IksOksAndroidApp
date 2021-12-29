package com.example.iksoksandroidapp.IksOksLogic.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.iksoksandroidapp.IksOksLogic.blue_backend.BluetoothConnectionService;
import com.example.iksoksandroidapp.R;

import java.util.ArrayList;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Constants
    private final static String TAG = "[DEBUG/IksOks]";
    public static final int REQUEST_ENABLE_BT = 999;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    //GUI Components Declaration
    Button btnOnOff;
    Button btnDiscoverability;
    Button btnDiscover;
    Button btnInitiateConnection;
    ListView lstAvailableDevices;

    //Bluetooth Setup Variables
    BluetoothAdapter blueAdapter;
    DeviceListAdapter mDeviceListAdapter;
    ArrayList<BluetoothDevice> availableDevices;
    BluetoothDevice myBlueDevice;
    //Variables


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        //GUI Controls Initialization
        btnOnOff = (Button) findViewById(R.id.btnOnOff);
        btnDiscoverability = (Button) findViewById(R.id.btnDiscoverability);
        btnDiscover = (Button) findViewById(R.id.btnDiscover);
        btnInitiateConnection = (Button) findViewById(R.id.btnInitiateConnection);
        lstAvailableDevices = (ListView) findViewById(R.id.lstAvailableDevices);

        //Variable Initialization
        availableDevices = new ArrayList<>();
        blueAdapter = BluetoothAdapter.getDefaultAdapter();

        //Event Handlers
        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlueOnOff();
            }
        });
        btnDiscoverability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlueDiscoverability();
            }
        });
        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlueDiscover();
            }
        });
        btnInitiateConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitiateConnection();
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(BlueStateChangedReceiver);
        unregisterReceiver(BlueDicoverabilityReceiver);
        unregisterReceiver(BlueDiscoverReceiver);
        //unregisterReceiver(mBroadcastReceiver4);

    }


    //Broadcast Receivers
    private final BroadcastReceiver BlueStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, blueAdapter.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "BlueStateChangedReceiver: STATE_OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "BlueStateChangedReceiver: STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "BlueStateChangedReceiver: STATE_ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "BlueStateChangedReceiver: STATE_TURNING_ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver BlueDicoverabilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                final int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, blueAdapter.ERROR);

                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "BlueDicoverabilityReceiver: SCAN_MODE_CONNECTABLE_DISCOVERABLE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "BlueDicoverabilityReceiver: SCAN_MODE_CONNECTABLE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "BlueDicoverabilityReceiver: SCAN_MODE_NONE");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "BlueDicoverabilityReceiver: STATE_CONNECTING");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "BlueDicoverabilityReceiver: STATE_CONNECTED");
                        break;
                }

            }
        }
    };

    private final BroadcastReceiver BlueDiscoverReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                availableDevices.add(device);

                Log.d(TAG, "BlueDiscoverReceiver - Device Found: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, availableDevices);
                lstAvailableDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };




    private void BlueOnOff() {
        String feedback = "";

        if(blueAdapter == null){
            //Bluetooth not supported by this device
            Log.d(TAG, "This device does not support bluetooth.");
        }
        if(!blueAdapter.isEnabled())
        {
            //Bluetooth is off. Call intent to turn it on
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetooth);

            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(BlueStateChangedReceiver, filter);

            feedback = "Bluetooth Turned On.";
        }
        if(blueAdapter.isEnabled())
        {
            //If on, turn it off.
            blueAdapter.disable();

            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(BlueStateChangedReceiver, filter);

            feedback = "Bluetooth turned Off";
        }

        Toast t = Toast.makeText(getApplicationContext(),feedback, Toast.LENGTH_LONG);
        t.show();
    }

    private void BlueDiscoverability() {
        Intent DiscoverabilityIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        DiscoverabilityIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
        startActivity(DiscoverabilityIntent);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(BlueDicoverabilityReceiver,filter);

        Toast t = Toast.makeText(getApplicationContext(),"Making device discoverable for 2 minutes.", Toast.LENGTH_LONG);
        t.show();
    }

    private void BlueDiscover() {
        Log.d(TAG, "Looking for unpaired devices.");
        availableDevices.clear();

        if(blueAdapter.isDiscovering()){
            //If already discovering start over
            blueAdapter.cancelDiscovery();

            //Check Bluetooth Permission
            checkBTPermissions();

            blueAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(BlueDiscoverReceiver, filter);

        }else if(!blueAdapter.isDiscovering())
        {
            //Check Bluetooth Permission
            checkBTPermissions();

            blueAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(BlueDiscoverReceiver, filter);
        }
    }

    private void InitiateConnection() {

    }


    //Additional Methods
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {
            Log.d(TAG, "Checking SDK version.");
            int permissionCheck = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
                permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            }
            if (permissionCheck != 0) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
                }
            }
        }else
        {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }




    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        blueAdapter.cancelDiscovery();

        String deviceName = availableDevices.get(i).getName();
        String deviceAddress = availableDevices.get(i).getAddress();


        //Initiate the bond
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            Log.d(TAG, "Trying to pair with device ["+deviceName+"/"+deviceAddress+"]");
            availableDevices.get(i).createBond();

            myBlueDevice = availableDevices.get(i);
            //mBluetoothConnectionService = new BluetoothConnectionService(MainActivity.this);
        }else{
            Toast t = Toast.makeText(this,"Toast Not valid SDK", Toast.LENGTH_LONG);
            t.show();
        }
    }
}
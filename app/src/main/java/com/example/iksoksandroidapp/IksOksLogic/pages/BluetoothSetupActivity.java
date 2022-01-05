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
import com.example.iksoksandroidapp.IksOksLogic.blue_backend.BluetoothManager;
import com.example.iksoksandroidapp.R;

import java.nio.charset.Charset;
import java.util.UUID;


public class BluetoothSetupActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Constants
    public final static String TAG = "[DEBUG/IksOks]";


    //GUI Components Declaration
    Button btnDiscoverability;
    Button btnDiscover;
    Button btnInitiateConnection;
    ListView lstAvailableDevices;
    Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);


        //GUI Controls Initialization
        btnDiscoverability = (Button) findViewById(R.id.btnDiscoverability);
        btnDiscover = (Button) findViewById(R.id.btnDiscover);
        btnInitiateConnection = (Button) findViewById(R.id.btnInitiateConnection);
        lstAvailableDevices = (ListView) findViewById(R.id.lstAvailableDevices);
        btnSend = (Button) findViewById(R.id.btnSend);


        //Event Handlers
        btnDiscoverability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothManager.BlueDiscoverability();
            }
        });
        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothManager.BlueDiscover();
            }
        });
        btnInitiateConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BluetoothManager.InitiateConnection();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothManager.sendCommand("test");
            }
        });


        lstAvailableDevices.setOnItemClickListener(BluetoothSetupActivity.this);

        //Player's next move receiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(BlueBondChangedReceiver, filter);

        registerReceiver(BlueConnectedReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
        registerReceiver(BlueConnectedReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));

        //Bluetooth manager Setup
        BluetoothManager.setBlueAdapter(BluetoothAdapter.getDefaultAdapter());
        BluetoothManager.loadContext(this);
        BluetoothManager.getbReceivers().add(BlueDicoverabilityReceiver);
        BluetoothManager.getbReceivers().add(BlueDiscoverReceiver);
        BluetoothManager.getbReceivers().add(BlueBondChangedReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(BlueDicoverabilityReceiver);
        unregisterReceiver(BlueDiscoverReceiver);
        unregisterReceiver(BlueBondChangedReceiver);
        //unregisterReceiver(CommandReceiver);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BluetoothManager.getBlueAdapter().cancelDiscovery();

        String deviceName = BluetoothManager.getAvailableDevices().get(i).getName();
        String deviceAddress = BluetoothManager.getAvailableDevices().get(i).getAddress();

        Log.d(TAG, "onItemClick: You clicked on a device! [" + deviceName + "," + deviceAddress + "]");

        //Initiate the bond
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d(TAG, "Trying to pair with device [" + deviceName + "/" + deviceAddress + "]");
            BluetoothManager.getAvailableDevices().get(i).createBond();

            BluetoothManager.setMyBlueDevice(BluetoothManager.getAvailableDevices().get(i));
            BluetoothManager.setmBluetoothConnectionService(new BluetoothConnectionService(BluetoothSetupActivity.this));

            BluetoothManager.InitiateConnection();
        }
    }




    //Broadcast Receivers
    private final BroadcastReceiver BlueDicoverabilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                final int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothManager.getBlueAdapter().ERROR);

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
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothManager.getAvailableDevices().add(device);

                Log.d(TAG, "BlueDiscoverReceiver - Device Found: " + device.getName() + ": " + device.getAddress());
                BluetoothManager.setmDeviceListAdapter(new DeviceListAdapter(context, R.layout.device_adapter_view, BluetoothManager.getAvailableDevices()));
                lstAvailableDevices.setAdapter(BluetoothManager.getmDeviceListAdapter());
            }
        }
    };

    private final BroadcastReceiver BlueBondChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases: bonded already | in process | broken bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "mBroadcastReceiver4: BOND_BONDED");
                    BluetoothManager.setMyBlueDevice(mDevice);
                    Toast t = Toast.makeText(BluetoothSetupActivity.this, "Device Connected to " + mDevice.getName(), Toast.LENGTH_LONG);
                    t.show();
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "mBroadcastReceiver4: BOND_BONDING");
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "mBroadcastReceiver4: BOND_NONE");
                }

            }
        }
    };


    private final BroadcastReceiver BlueConnectedReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action))
            {
                Log.d(TAG, "Device is connected.");
                //BluetoothManager.InitiateConnection();
            }
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action))
            {
                Log.d(TAG, "Device is disconnected.");
            }
        }
    };




}
package com.example.iksoksandroidapp.IksOksLogic.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.iksoksandroidapp.IksOksLogic.blue_backend.BlueIksOksBoard;
import com.example.iksoksandroidapp.IksOksLogic.blue_backend.BluetoothConnectionService;
import com.example.iksoksandroidapp.IksOksLogic.blue_backend.BluetoothManager;
import com.example.iksoksandroidapp.IksOksLogic.classic_backend.GameManager;
import com.example.iksoksandroidapp.R;

import org.parceler.Parcels;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

//    //Constants
//    private final static String TAG = "[DEBUG/IksOks]";
//    public static final int REQUEST_ENABLE_BT = 999;
//    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    public final static String TAG = "[DEBUG/IksOks]";


    //GUI Components Declaration
    Button btnOnOff;
    Button btnDiscoverability;
    Button btnDiscover;
    Button btnInitiateConnection;
    ListView lstAvailableDevices;
    Button btnSend;

    BluetoothManager blueManager;

//    //Bluetooth Setup Variables
//    BluetoothAdapter blueAdapter;
//    DeviceListAdapter mDeviceListAdapter;
//    ArrayList<BluetoothDevice> availableDevices;
//    BluetoothDevice myBlueDevice;
//
//    //Bluetooth Transfer Data Variables
//    BluetoothConnectionService mBluetoothConnectionService;
//
//

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
        btnSend = (Button) findViewById(R.id.btnSend);


        blueManager = new BluetoothManager();

//        //Variable Initialization
//        availableDevices = new ArrayList<>();
//        blueAdapter = BluetoothAdapter.getDefaultAdapter();

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
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand();
            }
        });


        lstAvailableDevices.setOnItemClickListener(BluetoothActivity.this);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(BlueBondChangedReceiver, filter);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(BlueStateChangedReceiver);
        unregisterReceiver(BlueDicoverabilityReceiver);
        unregisterReceiver(BlueDiscoverReceiver);
        unregisterReceiver(BlueBondChangedReceiver);
        //unregisterReceiver(CommandReceiver);
    }


    //Broadcast Receivers
    private final BroadcastReceiver BlueStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, blueManager.getBlueAdapter().ERROR);
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
                final int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, blueManager.getBlueAdapter().ERROR);

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
                blueManager.getAvailableDevices().add(device);

                Log.d(TAG, "BlueDiscoverReceiver - Device Found: " + device.getName() + ": " + device.getAddress());
                blueManager.setmDeviceListAdapter(new DeviceListAdapter(context, R.layout.device_adapter_view, blueManager.getAvailableDevices()));
                lstAvailableDevices.setAdapter(blueManager.getmDeviceListAdapter());
            }
        }
    };

    private final BroadcastReceiver BlueBondChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
            {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases: bonded already | in process | broken bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED)
                {
                    Log.d(TAG, "mBroadcastReceiver4: BOND_BONDED");
                    blueManager.setMyBlueDevice(mDevice);
                    Toast t = Toast.makeText(BluetoothActivity.this, "Device Connected to " + mDevice.getName(), Toast.LENGTH_LONG);
                    t.show();
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING)
                {
                    Log.d(TAG, "mBroadcastReceiver4: BOND_BONDING");
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE)
                {
                    Log.d(TAG, "mBroadcastReceiver4: BOND_NONE");
                }

            }
        }
    };




    private void BlueOnOff() {
        String feedback = "";

        if(blueManager.getBlueAdapter() == null){
            //Bluetooth not supported by this device
            Log.d(TAG, "This device does not support bluetooth.");
        }
        if(!blueManager.getBlueAdapter().isEnabled())
        {
            //Bluetooth is off. Call intent to turn it on
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetooth);

            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(BlueStateChangedReceiver, filter);

            feedback = "Bluetooth Turned On.";
        }
        if(blueManager.getBlueAdapter().isEnabled())
        {
            //If on, turn it off.
            blueManager.getBlueAdapter().disable();

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

        if(blueManager.getAvailableDevices() != null)
            blueManager.getAvailableDevices().clear();

        if(blueManager.getBlueAdapter().isDiscovering()){
            //If already discovering start over
            blueManager.getBlueAdapter().cancelDiscovery();

            //Check Bluetooth Permission
            checkBTPermissions();

            blueManager.getBlueAdapter().startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(BlueDiscoverReceiver, filter);

        }else if(!blueManager.getBlueAdapter().isDiscovering())
        {
            //Check Bluetooth Permission
            checkBTPermissions();

            blueManager.getBlueAdapter().startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(BlueDiscoverReceiver, filter);
        }
    }

    private void InitiateConnection() {
        if(startBTConnection(blueManager.getMyBlueDevice(), BluetoothManager.getMyUuidInsecure())) {
            Intent intent = new Intent(this, BluetoothClassic.class);
            startActivity(intent);
        }
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

    private boolean startBTConnection(BluetoothDevice mDevice, UUID myUuidInsecure) {
        Log.d(TAG, "startBTConnection: Initiialize RFCOM Bluetooth Connection");
        if(mDevice != null){
            blueManager.getmBluetoothConnectionService().startClient(mDevice, myUuidInsecure);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        blueManager.getBlueAdapter().cancelDiscovery();

        String deviceName = blueManager.getAvailableDevices().get(i).getName();
        String deviceAddress = blueManager.getAvailableDevices().get(i).getAddress();

        Log.d(TAG, "onItemClick: You clicked on a device! ["+deviceName+","+deviceAddress+"]");

        //Initiate the bond
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            Log.d(TAG, "Trying to pair with device ["+deviceName+"/"+deviceAddress+"]");
            blueManager.getAvailableDevices().get(i).createBond();

            blueManager.setMyBlueDevice(blueManager.getAvailableDevices().get(i));
            BluetoothManager.setmBluetoothConnectionService(new BluetoothConnectionService(BluetoothActivity.this));
        }
    }

    public void sendCommand() {
        //Logika koje polje je kliknuto (Darkic)


        //Parsiranje
        String cmd = "Cmd 1";
        byte[] bytes = cmd.getBytes(Charset.defaultCharset());

        //Slanje
        blueManager.getmBluetoothConnectionService().write(bytes);
    }


}
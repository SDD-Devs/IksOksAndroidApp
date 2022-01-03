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
                BluetoothManager.InitiateConnection();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand();
            }
        });


        lstAvailableDevices.setOnItemClickListener(BluetoothSetupActivity.this);


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(BlueBondChangedReceiver, filter);


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


    //Getters and Setters


    public BroadcastReceiver getBlueDicoverabilityReceiver() {
        return BlueDicoverabilityReceiver;
    }

    public BroadcastReceiver getBlueDiscoverReceiver() {
        return BlueDiscoverReceiver;
    }

    public BroadcastReceiver getBlueBondChangedReceiver() {
        return BlueBondChangedReceiver;
    }




    //Broadcast Receivers
    private final BroadcastReceiver BlueDicoverabilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
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
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
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
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
            {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases: bonded already | in process | broken bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED)
                {
                    Log.d(TAG, "mBroadcastReceiver4: BOND_BONDED");
                    BluetoothManager.setMyBlueDevice(mDevice);
                    Toast t = Toast.makeText(getApplicationContext(), "Device Connected to " + mDevice.getName(), Toast.LENGTH_LONG);
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





//    private void BlueDiscoverability() {
//        Intent DiscoverabilityIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        DiscoverabilityIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
//        startActivity(DiscoverabilityIntent);
//
//        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        registerReceiver(BlueDicoverabilityReceiver,filter);
//
//        Toast t = Toast.makeText(getApplicationContext(),"Making device discoverable for 2 minutes.", Toast.LENGTH_LONG);
//        t.show();
//    }
//
//    private void BlueDiscover() {
//        Log.d(TAG, "Looking for unpaired devices.");
//
//        if(blueManager.getAvailableDevices() != null)
//            blueManager.getAvailableDevices().clear();
//
//        if(blueManager.getBlueAdapter().isDiscovering()){
//            //If already discovering start over
//            blueManager.getBlueAdapter().cancelDiscovery();
//
//            //Check Bluetooth Permission
//            checkBTPermissions();
//
//            blueManager.getBlueAdapter().startDiscovery();
//            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            registerReceiver(BlueDiscoverReceiver, filter);
//
//        }else if(!blueManager.getBlueAdapter().isDiscovering())
//        {
//            //Check Bluetooth Permission
//            checkBTPermissions();
//
//            blueManager.getBlueAdapter().startDiscovery();
//            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            registerReceiver(BlueDiscoverReceiver, filter);
//        }
//    }
//
//    private void InitiateConnection() {
//        if(startBTConnection(blueManager.getMyBlueDevice(), BluetoothManager.getMyUuidInsecure())) {
//            Intent intent = new Intent(this, BluetoothClassic.class);
//            startActivity(intent);
//        }
//    }




    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BluetoothManager.getBlueAdapter().cancelDiscovery();

        String deviceName = BluetoothManager.getAvailableDevices().get(i).getName();
        String deviceAddress = BluetoothManager.getAvailableDevices().get(i).getAddress();

        Log.d(TAG, "onItemClick: You clicked on a device! ["+deviceName+","+deviceAddress+"]");

        //Initiate the bond
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            Log.d(TAG, "Trying to pair with device ["+deviceName+"/"+deviceAddress+"]");
            BluetoothManager.getAvailableDevices().get(i).createBond();

            BluetoothManager.setMyBlueDevice(BluetoothManager.getAvailableDevices().get(i));
            BluetoothManager.setmBluetoothConnectionService(new BluetoothConnectionService(BluetoothSetupActivity.this));
        }
    }



    public void sendCommand() {
        //Logika koje polje je kliknuto (Darkic)


        //Parsiranje
        String cmd = "Cmd 1";
        byte[] bytes = cmd.getBytes(Charset.defaultCharset());

        //Slanje
        BluetoothManager.getmBluetoothConnectionService().write(bytes);
    }



}
package com.example.iksoksandroidapp.IksOksLogic.bluetooth_package.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.iksoksandroidapp.IksOksLogic.bluetooth_package.backend.BluetoothConnectionService;
import com.example.iksoksandroidapp.IksOksLogic.bluetooth_package.other.DeviceListAdapter;
import com.example.iksoksandroidapp.IksOksLogic.mainpage_package.activities.MainActivity;
import com.example.iksoksandroidapp.R;

import java.util.ArrayList;
import java.util.UUID;

public class BluetoothSetupActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private static final UUID MY_UUID_INSECURE = BluetoothConnectionService.MY_UUID_INSECURE;
    private static final int LOCATION_PERMISSION_REQUEST = 101;

    BluetoothAdapter bluetoothAdapter;
    ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    DeviceListAdapter deviceListAdapter;
    ListView listView;

    BluetoothConnectionService bluetoothConnectionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_setup);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        listView = findViewById(R.id.discoveredDevicesListView);

        listView.setOnItemClickListener(BluetoothSetupActivity.this);

        bluetoothConnectionService = new BluetoothConnectionService(BluetoothSetupActivity.this);
        bluetoothConnectionService.startServer();

        registerReceivers();

    }

    @Override
    protected void onDestroy() {

        Log.d(TAG, "onDestroy: Called.");
        super.onDestroy();

        unregisterReceivers();

    }

    public void onToggleDiscoverabilityButtonClick(View view) {

        Log.d(TAG, "onToggleDiscoverabilityClick: Making device discoverable for 120 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivity(discoverableIntent);

    }

    public void onDiscoverDevicesButtonClick(View view) {

        enableBluetooth();

        if (deviceListAdapter != null) {
            bluetoothDevices.clear();
            deviceListAdapter.notifyDataSetChanged();
        }

        Log.d(TAG, "onDiscoverDevicesButtonClick: Looking for unpaired devices.");

        checkBluetoothPermissions();

        if (bluetoothAdapter.isDiscovering()) {

            bluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "onDiscoverDevicesButtonClick: Canceling discovery.");

            bluetoothAdapter.startDiscovery();
            Log.d(TAG, "onDiscoverDevicesButtonClick: Starting discovery.");

        } else if (!bluetoothAdapter.isDiscovering()) {

            bluetoothAdapter.startDiscovery();
            Log.d(TAG, "onDiscoverDevicesButtonClick: Starting discovery.");

        }

    }

    public void startBluetoothConnection(BluetoothDevice bluetoothDevice, UUID uuid) {

        Log.d(TAG, "startBluetoothConnection: Initializing RFCOMM Bluetooth connection.");

        bluetoothConnectionService.startClient(bluetoothDevice, uuid);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d(TAG, "onItemClick: You Clicked on a device.");

        bluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: Canceled discovery.");

        BluetoothDevice bluetoothDevice = bluetoothDevices.get(i);

        String deviceName = bluetoothDevice.getName();
        String deviceAddress = bluetoothDevice.getAddress();

        Log.d(TAG, "onItemClick: deviceName: " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress: " + deviceAddress);

        Log.d(TAG, "Trying to connect with " + deviceName);

        if (bluetoothAdapter.getBondedDevices().contains(bluetoothDevice)) {

            Log.d(TAG, "onItemClick: Devices already bonded, connecting...");
            startBluetoothConnection(bluetoothDevice, MY_UUID_INSECURE);

        } else {

            Log.d(TAG, "onItemClick: Devices not yet bonded, bonding...");
            bluetoothDevice.createBond();

        }

    }

    private void registerReceivers() {

        registerReceiver(broadcastReceiverBluetoothAdapterActionStateChanged, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(broadcastReceiverBluetoothAdapterActionScanModeChanged, new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
        registerReceiver(broadcastReceiverBluetoothDeviceActionFound, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(broadcastReceiverBluetoothDeviceActionBondStateChanged, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverInputStream, new IntentFilter("incomingMessage"));

    }

    private void unregisterReceivers() {

        unregisterReceiver(broadcastReceiverBluetoothAdapterActionStateChanged);
        unregisterReceiver(broadcastReceiverBluetoothAdapterActionScanModeChanged);
        unregisterReceiver(broadcastReceiverBluetoothDeviceActionFound);
        unregisterReceiver(broadcastReceiverBluetoothDeviceActionBondStateChanged);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverInputStream);

    }

    private void enableBluetooth() {

        if (bluetoothAdapter == null) {

            Log.d(TAG, "toggleBluetooth: This device does not support Bluetooth.");
            return;

        }

        if (!bluetoothAdapter.isEnabled()) {

            Log.d(TAG, "toggleBluetooth: Enabling Bluetooth.");

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

        }

    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void checkBluetoothPermissions() {
//
//        if (this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION") + this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION") != 0) {
//
//            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
//
//        }
//
//    }


    private void checkBluetoothPermissions(){
        if(ContextCompat.checkSelfPermission(BluetoothSetupActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BluetoothSetupActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
            }else {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Location Permission is Required. \n Please grant permission.")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkBluetoothPermissions();
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(BluetoothSetupActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .create().show();

            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private final BroadcastReceiver broadcastReceiverBluetoothAdapterActionStateChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_OFF) {
                    Log.d(TAG, "onReceive: STATE OFF");
                } else if (state == BluetoothAdapter.STATE_TURNING_OFF) {
                    Log.d(TAG, "onReceive: STATE TURNING OFF");
                } else if (state == BluetoothAdapter.STATE_ON) {
                    Log.d(TAG, "onReceive: STATE ON");
                } else if (state == BluetoothAdapter.STATE_TURNING_ON) {
                    Log.d(TAG, "onReceive: STATE TURNING ON");
                }

            }

        }
    };

    private final BroadcastReceiver broadcastReceiverBluetoothAdapterActionScanModeChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                final int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                if (mode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    Log.d(TAG, "onReceive: Discoverability Enabled.");
                } else if (mode == BluetoothAdapter.SCAN_MODE_CONNECTABLE) {
                    Log.d(TAG, "onReceive: Discoverability Disabled. Able to receive connections.");
                } else if (mode == BluetoothAdapter.SCAN_MODE_NONE) {
                    Log.d(TAG, "onReceive: Discoverability Disabled. Not able to receive connections.");
                } else if (mode == BluetoothAdapter.STATE_CONNECTING) {
                    Log.d(TAG, "onReceive: Connecting...");
                } else if (mode == BluetoothAdapter.STATE_CONNECTED) {
                    Log.d(TAG, "onReceive: Connected.");
                }

            }

        }
    };

    private final BroadcastReceiver broadcastReceiverBluetoothDeviceActionFound = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {

                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                boolean alreadyAdded = false;
                for (BluetoothDevice bluetoothDevice1 : bluetoothDevices) {
                    if (bluetoothDevice1.getAddress().equals(bluetoothDevice.getAddress())) {
                        alreadyAdded = true;
                        break;
                    }
                }
                if (!alreadyAdded) {
                    bluetoothDevices.add(bluetoothDevice);
                }

                Log.d(TAG, "onReceive: " + "Device name: " + bluetoothDevice.getName() + " Device address: " + bluetoothDevice.getAddress());

                deviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, bluetoothDevices);

                listView.setAdapter(deviceListAdapter);

            }
        }
    };

    private final BroadcastReceiver broadcastReceiverBluetoothDeviceActionBondStateChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {

                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "onReceive: BOND_BONDED.");
                    startBluetoothConnection(bluetoothDevice, MY_UUID_INSECURE);
                }
                if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "onReceive: BOND_BONDING.");
                }
                if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "onReceive: BOND_NONE.");
                }

            }

        }
    };

    private final BroadcastReceiver broadcastReceiverInputStream = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: " + intent.getStringExtra("theMessage"));
            String[] messageArray = intent.getStringExtra("theMessage").trim().split("-");

            if (!messageArray[0].equals("PREGAME")) {
                return;
            }

            if (messageArray[1].equals("CLIENT")) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BluetoothSetupActivity.this);

                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage("Would you like to start a game?");
                alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {

                    bluetoothConnectionService.write("PREGAME-ACCEPTED");

                    Intent bluetoothGameActivityIntent = new Intent(BluetoothSetupActivity.this, BluetoothGameActivity.class);
                    bluetoothGameActivityIntent.putExtra("isServer", false);
                    startActivity(bluetoothGameActivityIntent);

                });
                alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> {
                    bluetoothConnectionService.write("PREGAME-DENIED");
                });

                alertDialogBuilder.create().show();

            } else if (messageArray[1].equals("ACCEPTED")) {

                Intent bluetoothGameActivityIntent = new Intent(BluetoothSetupActivity.this, BluetoothGameActivity.class);
                bluetoothGameActivityIntent.putExtra("isServer", true);
                startActivity(bluetoothGameActivityIntent);

            } else if (messageArray[1].equals("DENIED")) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BluetoothSetupActivity.this);

                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage("Your game request has been declined.");
                alertDialogBuilder.setPositiveButton("Ok", null);

                alertDialogBuilder.create().show();

            }

        }

    };

}
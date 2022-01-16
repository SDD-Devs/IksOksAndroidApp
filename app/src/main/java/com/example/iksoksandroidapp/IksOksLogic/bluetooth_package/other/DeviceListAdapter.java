package com.example.iksoksandroidapp.IksOksLogic.bluetooth_package.other;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.iksoksandroidapp.R;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private LayoutInflater layoutInflater;
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private int viewResourceID;

    public DeviceListAdapter(Context context, int viewResourceID, ArrayList<BluetoothDevice> bluetoothDevices) {

        super(context, viewResourceID, bluetoothDevices);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bluetoothDevices = bluetoothDevices;
        this.viewResourceID = viewResourceID;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(viewResourceID, null);

        BluetoothDevice bluetoothDevice = bluetoothDevices.get(position);

        if (bluetoothDevice == null) return convertView;

        TextView deviceName = (TextView) convertView.findViewById(R.id.deviceNameTextView);
        TextView deviceAddress = (TextView) convertView.findViewById(R.id.deviceAddressTextView);

        if (deviceName != null) {
            deviceName.setText(bluetoothDevice.getName());
        }

        if (deviceAddress != null) {
            deviceAddress.setText(bluetoothDevice.getAddress());
        }

        return convertView;

    }

}

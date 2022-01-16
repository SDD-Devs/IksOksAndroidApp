package com.example.iksoksandroidapp.IksOksLogic.bluetooth_package.backend;

import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

import java.util.ArrayList;
import java.util.List;

public class BluetoothBoard {

    private ArrayList<BluetoothTile> bluetoothTiles = new ArrayList<>();

    public BluetoothBoard() {
        for (int i = 0; i < 9; i++) {
            bluetoothTiles.add(new BluetoothTile(i, TileState.EMPTY) );
        }
    }

    public void reset(){
        for (BluetoothTile bluetoothTile : bluetoothTiles) {
            bluetoothTile.setState(TileState.EMPTY);
        }
    }

    public BluetoothTile getTileByID(int ID) {
        for (BluetoothTile bluetoothTile : bluetoothTiles) {
            if (bluetoothTile.getID() == ID) {
                return bluetoothTile;
            }
        }
        return null;
    }

    public List<Integer> getTileIDSForTileState(TileState tileState) {
        List<Integer> tileIDS = new ArrayList<>();
        for (BluetoothTile bluetoothTile : bluetoothTiles) {
            if (bluetoothTile.getState().equals(tileState)) {
                tileIDS.add(bluetoothTile.getID());
            }
        }
        return tileIDS;
    }

    public ArrayList<BluetoothTile> getTiles() {
        return bluetoothTiles;
    }

    public void setTiles(ArrayList<BluetoothTile> bluetoothTiles) {
        this.bluetoothTiles = bluetoothTiles;
    }
}

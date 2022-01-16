package com.example.iksoksandroidapp.IksOksLogic.bluetooth_package.backend;

import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

public class BluetoothTile {

    private int ID;
    private TileState state;

    public BluetoothTile(int ID, TileState tileState) {
        this.ID = ID;
        this.state = tileState;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

}

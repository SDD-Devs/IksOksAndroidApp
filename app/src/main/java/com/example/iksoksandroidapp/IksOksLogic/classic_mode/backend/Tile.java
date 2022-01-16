package com.example.iksoksandroidapp.IksOksLogic.classic_mode.backend;

import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

public class Tile {

    private int ID;
    private TileState state;

    public Tile(int ID, TileState tileState) {
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

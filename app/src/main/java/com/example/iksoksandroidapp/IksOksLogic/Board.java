package com.example.iksoksandroidapp.IksOksLogic;

import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

import java.util.ArrayList;

public class Board {

    private ArrayList<Tile> tiles = new ArrayList<>();

    public Board() {
        for (int i = 0; i < 9; i++) {
            tiles.add(new Tile(i, TileState.EMPTY) );
        }
    }

    public Tile getTileByID(int ID) {
        for (Tile tile : tiles) {
            if (tile.getID() == ID) {
                return tile;
            }
        }
        return null;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }
}

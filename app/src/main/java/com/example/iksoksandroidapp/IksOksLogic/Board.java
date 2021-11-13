package com.example.iksoksandroidapp.IksOksLogic;

import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

public class Board {

    private Tile[] tiles = new Tile[9];

    public Board() {
        for (int i = 0; i < 9; i++) {
            tiles[i] = new Tile(i, TileState.EMPTY);
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

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

}

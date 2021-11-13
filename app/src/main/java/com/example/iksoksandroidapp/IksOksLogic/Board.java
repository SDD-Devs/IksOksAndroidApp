package com.example.iksoksandroidapp.IksOksLogic;

import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Tile> tiles = new ArrayList<>();

    public Board() {
        for (int i = 0; i < 9; i++) {
            tiles.add(new Tile(i, TileState.EMPTY));
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

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

}

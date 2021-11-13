package com.example.iksoksandroidapp.IksOksLogic;

import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

import java.util.ArrayList;
import java.util.List;

public class Board {

    //The board is 3x3 in size, a List of tiles holds all 9 of them, their IDs go from 0 (top left) to 8 (bottom right)

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

    public List<Integer> getTileIDSForTileState(TileState tileState) {
        List<Integer> tileIDS = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile.getState().equals(tileState)) {
                tileIDS.add(tile.getID());
            }
        }
        return tileIDS;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

}

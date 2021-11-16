package com.example.iksoksandroidapp.IksOksLogic.classic_backend;

import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private ArrayList<Tile> tiles = new ArrayList<>();

    public Board() {
        for (int i = 0; i < 9; i++) {
            tiles.add(new Tile(i, TileState.EMPTY) );
        }
    }

    public void reset(){
        for (Tile tile : tiles) {
            tile.setState(TileState.EMPTY);
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

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }
}

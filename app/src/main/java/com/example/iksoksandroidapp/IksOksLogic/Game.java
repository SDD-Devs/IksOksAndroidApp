package com.example.iksoksandroidapp.IksOksLogic;

import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;
import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

import java.util.Arrays;

public class Game {

    private Board board;
    private GameState gameState;
    private PlayerType currentTurn;

    public Game() {
        this.board = new Board();
        this.gameState = GameState.IN_PROGRESS;
        this.currentTurn = PlayerType.IKS;
    }

    public boolean playTile(int tileID) {

        Tile tile = board.getTileByID(tileID);

        if (!tile.getState().equals(TileState.EMPTY)) return false;
        if (!gameState.equals(GameState.IN_PROGRESS)) return false;

        if (currentTurn.equals(PlayerType.IKS)) {
            tile.setState(TileState.IKS);
            currentTurn = PlayerType.OKS;
            calculateGameState();
            return true;
        } else if (currentTurn.equals(PlayerType.OKS)) {
            tile.setState(TileState.OKS);
            currentTurn = PlayerType.IKS;
            calculateGameState();
            return true;
        }

        return false;

    }

    public void calculateGameState() {

        {   //Rows

            all_tiles:
            for (Tile tile : board.getTiles()) {
                if (tile.getState().equals(TileState.EMPTY)) continue;
                if (!Arrays.asList(0, 3, 6).contains(tile.getID())) continue;

                for (int i = 1; i <= 2; i += 1) {
                    Tile checkedTile = board.getTileByID(tile.getID() + i);
                    if (!checkedTile.getState().equals(tile.getState())) continue all_tiles;
                }

                gameState = GameManager.getGameStateFromTileState(tile.getState());
                return;

            }

        }

        {   //Columns

            all_tiles:
            for (Tile tile : board.getTiles()) {
                if (tile.getState().equals(TileState.EMPTY)) continue;
                if (!Arrays.asList(0, 1, 2).contains(tile.getID())) continue;

                for (int i = 3; i <= 6; i += 3) {
                    Tile checkedTile = board.getTileByID(tile.getID() + i);
                    if (!checkedTile.getState().equals(tile.getState())) continue all_tiles;
                }

                gameState = GameManager.getGameStateFromTileState(tile.getState());
                return;

            }

        }

        break_label:
        {   //Diagonal top left to bottom right

            Tile tile = board.getTileByID(0);

            if (tile.getState().equals(TileState.EMPTY)) break break_label;

            for (int i = 4; i <= 8; i += 4) {
                Tile checkedTile = board.getTileByID(tile.getID() + i);
                if (!checkedTile.getState().equals(tile.getState())) break break_label;
            }

            gameState = GameManager.getGameStateFromTileState(tile.getState());
            return;

        }

        break_label:
        {   //Diagonal top right to bottom left

            Tile tile = board.getTileByID(2);

            if (tile.getState().equals(TileState.EMPTY)) break break_label;

            for (int i = 2; i <= 4; i += 2) {
                Tile checkedTile = board.getTileByID(tile.getID() + i);
                if (!checkedTile.getState().equals(tile.getState())) break break_label;
            }

            gameState = GameManager.getGameStateFromTileState(tile.getState());
            return;

        }

        {   //Tie

            boolean allFilled = true;
            for (Tile tile : board.getTiles()) {
                if (tile.getState().equals(TileState.EMPTY)) {
                    allFilled = false;
                    break;
                }
            }

            if (allFilled) {
                gameState = GameState.TIE;
                return;
            }

        }

    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public PlayerType getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(PlayerType currentTurn) {
        this.currentTurn = currentTurn;
    }

}

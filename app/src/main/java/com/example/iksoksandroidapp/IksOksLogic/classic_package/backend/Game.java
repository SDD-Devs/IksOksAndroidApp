package com.example.iksoksandroidapp.IksOksLogic.classic_package.backend;

import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;
import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

    private Board board;
    private GameState gameState;
    private PlayerType currentTurn;
    private long gameStartTime;
    private PlayerType playerType;

    private List<List<Integer>> winningPositions = new ArrayList<>();

    public Game() {

        this.board = new Board();
        this.gameState = GameState.IN_PROGRESS;
        this.currentTurn = PlayerType.IKS;
        this.gameStartTime = System.currentTimeMillis();

        //Caching all 8 possible winning positions
        winningPositions.add(Arrays.asList(0, 1, 2));
        winningPositions.add(Arrays.asList(3, 4, 5));
        winningPositions.add(Arrays.asList(6, 7, 8));
        winningPositions.add(Arrays.asList(0, 3, 6));
        winningPositions.add(Arrays.asList(1, 4, 7));
        winningPositions.add(Arrays.asList(2, 5, 8));
        winningPositions.add(Arrays.asList(0, 4, 8));
        winningPositions.add(Arrays.asList(2, 4, 6));

    }

    public void reset() {
        board.reset();
        gameState = GameState.IN_PROGRESS;
        gameStartTime = System.currentTimeMillis();
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public long getPlayTimeMilliseconds() {
        return System.currentTimeMillis() - gameStartTime;
    }

    public int getPlayTimeSeconds() {
        return (int) ((System.currentTimeMillis() - gameStartTime) / 1000);
    }

    public boolean playTile(int tileID) {

        Tile bluetoothTile = board.getTileByID(tileID);

        if (!bluetoothTile.getState().equals(TileState.EMPTY)) return false;
        if (!gameState.equals(GameState.IN_PROGRESS)) return false;

        if (currentTurn.equals(PlayerType.IKS)) {
            bluetoothTile.setState(TileState.IKS);
            currentTurn = PlayerType.OKS;
            calculateGameState();
            return true;
        } else if (currentTurn.equals(PlayerType.OKS)) {
            bluetoothTile.setState(TileState.OKS);
            currentTurn = PlayerType.IKS;
            calculateGameState();
            return true;
        }

        return false;

    }



    public void calculateGameState() {

        // The Mode can be set to 0-1
        // Mode 0 uses the method of checking the current state of the board with all 8 possible winning combinations
        // Mode 1 checks rows, columns and diagonals
        // Mode 1 can be up to 9x faster (more efficient for situations with less filled tiles, around 9 times faster than Mode 0 at the start, around 3 times faster at the end)
        int mode = 1;

        mode_label:
        {
            if (mode != 0) break mode_label;

            // Check for IKS
            for (List<Integer> position : winningPositions) {
                if (board.getTileIDSForTileState(TileState.IKS).containsAll(position)) {
                    gameState = GameState.IKS_WON;
                    return;
                }
            }
            // Check for OKS
            for (List<Integer> position : winningPositions) {
                if (board.getTileIDSForTileState(TileState.OKS).containsAll(position)) {
                    gameState = GameState.OKS_WON;
                    return;
                }
            }

        }

        mode_label:
        {
            if (mode != 1) break mode_label;

            {   //Rows
                //Checks the tiles with IDs 0, 3, 6, keeps checking to the right until it reaches a different TileState

                all_tiles:
                for (Tile bluetoothTile : board.getTiles()) {
                    if (bluetoothTile.getState().equals(TileState.EMPTY)) continue;
                    if (!Arrays.asList(0, 3, 6).contains(bluetoothTile.getID())) continue;

                    for (int i = 1; i <= 2; i += 1) {
                        Tile checkedTile = board.getTileByID(bluetoothTile.getID() + i);
                        if (!checkedTile.getState().equals(bluetoothTile.getState())) continue all_tiles;
                    }

                    gameState = GameManager.getGameStateFromTileState(bluetoothTile.getState());
                    return;

                }

            }

            {   //Columns
                //Checks the tiles with IDs 0, 1, 2, keeps checking to down until it reaches a different TileState

                all_tiles:
                for (Tile bluetoothTile : board.getTiles()) {
                    if (bluetoothTile.getState().equals(TileState.EMPTY)) continue;
                    if (!Arrays.asList(0, 1, 2).contains(bluetoothTile.getID())) continue;

                    for (int i = 3; i <= 6; i += 3) {
                        Tile checkedTile = board.getTileByID(bluetoothTile.getID() + i);
                        if (!checkedTile.getState().equals(bluetoothTile.getState())) continue all_tiles;
                    }

                    gameState = GameManager.getGameStateFromTileState(bluetoothTile.getState());
                    return;

                }

            }

            break_label:
            {   //Diagonal top left to bottom right
                //Checks the tiles with IDs 0, 4, 8

                Tile bluetoothTile = board.getTileByID(0);

                if (bluetoothTile.getState().equals(TileState.EMPTY)) break break_label;

                for (int i = 4; i <= 8; i += 4) {
                    Tile checkedTile = board.getTileByID(bluetoothTile.getID() + i);
                    if (!checkedTile.getState().equals(bluetoothTile.getState())) break break_label;
                }

                gameState = GameManager.getGameStateFromTileState(bluetoothTile.getState());
                return;

            }

            break_label:
            {   //Diagonal top right to bottom left
                //Checks the tiles with IDs 2, 4, 6

                Tile bluetoothTile = board.getTileByID(2);

                if (bluetoothTile.getState().equals(TileState.EMPTY)) break break_label;

                for (int i = 2; i <= 4; i += 2) {
                    Tile checkedTile = board.getTileByID(bluetoothTile.getID() + i);
                    if (!checkedTile.getState().equals(bluetoothTile.getState())) break break_label;
                }

                gameState = GameManager.getGameStateFromTileState(bluetoothTile.getState());
                return;

            }

        }

        {   //Tie

            boolean allFilled = true;
            for (Tile bluetoothTile : board.getTiles()) {
                if (bluetoothTile.getState().equals(TileState.EMPTY)) {
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

package com.example.iksoksandroidapp.IksOksLogic.bluetooth_backend;

import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;
import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BluetoothGame {

    private BluetoothBoard bluetoothBoard;
    private GameState gameState;
    private PlayerType currentTurn;
    private long gameStartTime;
    private PlayerType playerType;

    private List<List<Integer>> winningPositions = new ArrayList<>();

    public BluetoothGame() {

        this.bluetoothBoard = new BluetoothBoard();
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
        bluetoothBoard.reset();
        gameState = GameState.IN_PROGRESS;
        gameStartTime = System.currentTimeMillis();
    }

    public long getPlayTimeMilliseconds() {
        return System.currentTimeMillis() - gameStartTime;
    }

    public int getPlayTimeSeconds() {
        return (int) ((System.currentTimeMillis() - gameStartTime) / 1000);
    }

    public boolean playTile(int tileID) {

        BluetoothTile bluetoothTile = bluetoothBoard.getTileByID(tileID);

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
                if (bluetoothBoard.getTileIDSForTileState(TileState.IKS).containsAll(position)) {
                    gameState = GameState.IKS_WON;
                    return;
                }
            }
            // Check for OKS
            for (List<Integer> position : winningPositions) {
                if (bluetoothBoard.getTileIDSForTileState(TileState.OKS).containsAll(position)) {
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
                for (BluetoothTile bluetoothTile : bluetoothBoard.getTiles()) {
                    if (bluetoothTile.getState().equals(TileState.EMPTY)) continue;
                    if (!Arrays.asList(0, 3, 6).contains(bluetoothTile.getID())) continue;

                    for (int i = 1; i <= 2; i += 1) {
                        BluetoothTile checkedBluetoothTile = bluetoothBoard.getTileByID(bluetoothTile.getID() + i);
                        if (!checkedBluetoothTile.getState().equals(bluetoothTile.getState())) continue all_tiles;
                    }

                    gameState = BluetoothGameManager.getGameStateFromTileState(bluetoothTile.getState());
                    return;

                }

            }

            {   //Columns
                //Checks the tiles with IDs 0, 1, 2, keeps checking to down until it reaches a different TileState

                all_tiles:
                for (BluetoothTile bluetoothTile : bluetoothBoard.getTiles()) {
                    if (bluetoothTile.getState().equals(TileState.EMPTY)) continue;
                    if (!Arrays.asList(0, 1, 2).contains(bluetoothTile.getID())) continue;

                    for (int i = 3; i <= 6; i += 3) {
                        BluetoothTile checkedBluetoothTile = bluetoothBoard.getTileByID(bluetoothTile.getID() + i);
                        if (!checkedBluetoothTile.getState().equals(bluetoothTile.getState())) continue all_tiles;
                    }

                    gameState = BluetoothGameManager.getGameStateFromTileState(bluetoothTile.getState());
                    return;

                }

            }

            break_label:
            {   //Diagonal top left to bottom right
                //Checks the tiles with IDs 0, 4, 8

                BluetoothTile bluetoothTile = bluetoothBoard.getTileByID(0);

                if (bluetoothTile.getState().equals(TileState.EMPTY)) break break_label;

                for (int i = 4; i <= 8; i += 4) {
                    BluetoothTile checkedBluetoothTile = bluetoothBoard.getTileByID(bluetoothTile.getID() + i);
                    if (!checkedBluetoothTile.getState().equals(bluetoothTile.getState())) break break_label;
                }

                gameState = BluetoothGameManager.getGameStateFromTileState(bluetoothTile.getState());
                return;

            }

            break_label:
            {   //Diagonal top right to bottom left
                //Checks the tiles with IDs 2, 4, 6

                BluetoothTile bluetoothTile = bluetoothBoard.getTileByID(2);

                if (bluetoothTile.getState().equals(TileState.EMPTY)) break break_label;

                for (int i = 2; i <= 4; i += 2) {
                    BluetoothTile checkedBluetoothTile = bluetoothBoard.getTileByID(bluetoothTile.getID() + i);
                    if (!checkedBluetoothTile.getState().equals(bluetoothTile.getState())) break break_label;
                }

                gameState = BluetoothGameManager.getGameStateFromTileState(bluetoothTile.getState());
                return;

            }

        }

        {   //Tie

            boolean allFilled = true;
            for (BluetoothTile bluetoothTile : bluetoothBoard.getTiles()) {
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

    public BluetoothBoard getBoard() {
        return bluetoothBoard;
    }

    public void setBoard(BluetoothBoard bluetoothBoard) {
        this.bluetoothBoard = bluetoothBoard;
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

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }
}

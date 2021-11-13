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

    public void printGame() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getStringFromTileState(board.getTileByID(0).getState()));
        stringBuilder.append("|");
        stringBuilder.append(getStringFromTileState(board.getTileByID(1).getState()));
        stringBuilder.append("|");
        stringBuilder.append(getStringFromTileState(board.getTileByID(2).getState()));

        stringBuilder.append("\n-----\n");

        stringBuilder.append(getStringFromTileState(board.getTileByID(3).getState()));
        stringBuilder.append("|");
        stringBuilder.append(getStringFromTileState(board.getTileByID(4).getState()));
        stringBuilder.append("|");
        stringBuilder.append(getStringFromTileState(board.getTileByID(5).getState()));

        stringBuilder.append("\n-----\n");

        stringBuilder.append(getStringFromTileState(board.getTileByID(6).getState()));
        stringBuilder.append("|");
        stringBuilder.append(getStringFromTileState(board.getTileByID(7).getState()));
        stringBuilder.append("|");
        stringBuilder.append(getStringFromTileState(board.getTileByID(8).getState()));

        System.out.println(stringBuilder);

    }

    private String getStringFromTileState(TileState tileState) {
        if (tileState.equals(TileState.IKS)) return "X";
        if (tileState.equals(TileState.OKS)) return "O";
        return " ";
    }

    public boolean playTile(int tileID) {

        Tile tile = board.getTileByID(tileID);

        if (!tile.getState().equals(TileState.EMPTY)) return false;

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

                for (int i = 1; i <= 2; i++) {
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

                for (int i = 3; i <= 6; i++) {
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

            for (int i = 4; i <= 8; i++) {
                Tile checkedTile = board.getTileByID(tile.getID() + i);
                if (!checkedTile.getState().equals(tile.getState())) break break_label;
            }

            gameState = GameManager.getGameStateFromTileState(tile.getState());
            return;

        }

        break_label:
        {   //Diagonal top right to bottom left

            Tile tile = board.getTileByID(0);

            if (tile.getState().equals(TileState.EMPTY)) break break_label;

            for (int i = 2; i <= 4; i++) {
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
                System.out.println(gameState);
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

package com.example.iksoksandroidapp.IksOksLogic;

import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;
import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

public class GameManager {

    private static Game game;

    public static void startNewGame() {
        game = new Game();
    }

    public static Game getGame() {
        return game;
    }

    public static PlayerType getPlayerTypeFromTileState(TileState tileState) {
        if (tileState.equals(TileState.IKS)) return PlayerType.IKS;
        if (tileState.equals(TileState.OKS)) return PlayerType.OKS;
        return null;
    }

    public static GameState getGameStateFromTileState(TileState tileState) {
        if (tileState.equals(TileState.IKS)) return GameState.IKS_WON;
        if (tileState.equals(TileState.OKS)) return GameState.OKS_WON;
        return null;
    }

}

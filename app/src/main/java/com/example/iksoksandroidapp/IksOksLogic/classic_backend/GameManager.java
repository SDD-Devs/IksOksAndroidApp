package com.example.iksoksandroidapp.IksOksLogic.classic_backend;

import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;
import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;
import com.example.iksoksandroidapp.IksOksLogic.pages.ClassicGameActivity;

import java.lang.ref.SoftReference;

public class GameManager {

    private static Game game;
    private static SoftReference<ClassicGameActivity> classicGameActivity;

    public static void startNewGame() {
        game = new Game();
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        GameManager.game = game;
    }

    public static SoftReference<ClassicGameActivity> getClassicGameActivity() {
        return classicGameActivity;
    }

    public static void setClassicGameActivity(SoftReference<ClassicGameActivity> classicGameActivity) {
        GameManager.classicGameActivity = classicGameActivity;
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

    public static String getStringFromTileState(TileState tileState) {
        if (tileState.equals(TileState.IKS)) return "X";
        if (tileState.equals(TileState.OKS)) return "O";
        return " ";
    }

}

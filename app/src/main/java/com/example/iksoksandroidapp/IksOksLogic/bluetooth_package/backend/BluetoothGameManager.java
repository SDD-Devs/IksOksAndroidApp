package com.example.iksoksandroidapp.IksOksLogic.bluetooth_package.backend;

import com.example.iksoksandroidapp.IksOksLogic.classic_package.activities.ClassicGameActivity;
import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;
import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;

import java.lang.ref.SoftReference;

public class BluetoothGameManager {

    private static BluetoothGame bluetoothGame;
    private static SoftReference<ClassicGameActivity> classicGameActivity;

    public static void startNewGame() {
        bluetoothGame = new BluetoothGame();
    }

    public static BluetoothGame getGame() {
        return bluetoothGame;
    }

    public static void setGame(BluetoothGame bluetoothGame) {
        BluetoothGameManager.bluetoothGame = bluetoothGame;
    }

    public static SoftReference<ClassicGameActivity> getClassicGameActivity() {
        return classicGameActivity;
    }

    public static void setClassicGameActivity(SoftReference<ClassicGameActivity> classicGameActivity) {
        BluetoothGameManager.classicGameActivity = classicGameActivity;
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

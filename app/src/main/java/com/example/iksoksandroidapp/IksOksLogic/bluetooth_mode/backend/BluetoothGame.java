package com.example.iksoksandroidapp.IksOksLogic.bluetooth_mode.backend;

import com.example.iksoksandroidapp.IksOksLogic.classic_mode.backend.Game;
import com.example.iksoksandroidapp.IksOksLogic.enums.PlayerType;

public class BluetoothGame extends Game {

    private PlayerType playerType;

    public BluetoothGame() {
        super();
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

}

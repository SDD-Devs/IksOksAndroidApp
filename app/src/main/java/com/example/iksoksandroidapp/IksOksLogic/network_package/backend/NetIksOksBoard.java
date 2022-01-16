package com.example.iksoksandroidapp.IksOksLogic.network_package.backend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.Nullable;
import com.example.iksoksandroidapp.IksOksLogic.classic_package.backend.Game;
import com.example.iksoksandroidapp.IksOksLogic.classic_package.backend.GameManager;
import com.example.iksoksandroidapp.IksOksLogic.classic_package.backend.IksOksBoard;
import com.example.iksoksandroidapp.IksOksLogic.network_package.activities.NetworkSetupActivity;


public class NetIksOksBoard extends IksOksBoard {


    public NetIksOksBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if(NetworkSetupActivity.instance.client.yourTurn) {
            return false;
        }

        Game game = GameManager.getGame();

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        int action = motionEvent.getAction();

        if (action == MotionEvent.ACTION_DOWN) {

            int row = (int) Math.ceil(y / cellSize) - 1;
            int column = (int) Math.ceil(x / cellSize) - 1;
            int position = super.oneDimensionalFromTwo(row, column);

            if (!game.playTile(position)) {
                return false;
            }

            //Send the play to another player.
            NetworkSetupActivity.instance.client.sendMessage("SE-"+NetworkSetupActivity.instance.client.gameRoomId+"-"+position);

            invalidate();

            return true;

        }

        return false;

        //return super.onTouchEvent(motionEvent);
    }
}



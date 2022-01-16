package com.example.iksoksandroidapp.IksOksLogic.bluetooth_package.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.IksOksLogic.enums.TileState;
import com.example.iksoksandroidapp.IksOksLogic.other.PopUp;
import com.example.iksoksandroidapp.R;

public class BluetoothIksOksBoard extends View {

    private final int boardColor;
    private final int XColor;
    private final int OColor;
    private final int winningLineColor;

    private int cellSize = getWidth() / 3;

    private final Paint paint = new Paint();

    public BluetoothIksOksBoard(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IksOksBoard, 0, 0);

        try {
            boardColor = typedArray.getInteger(R.styleable.IksOksBoard_boardColor, 0);
            XColor = typedArray.getInteger(R.styleable.IksOksBoard_XColor, 0);
            OColor = typedArray.getInteger(R.styleable.IksOksBoard_OColor, 0);
            winningLineColor = typedArray.getInteger(R.styleable.IksOksBoard_winningLineColor, 0);
        } finally {
            typedArray.recycle();
        }

        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiverInputStream, new IntentFilter("incomingMessage"));

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        BluetoothGame bluetoothGame = BluetoothGameManager.getGame();

        if (!bluetoothGame.getCurrentTurn().equals(bluetoothGame.getPlayerType())) {
            return false;
        }

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        int action = motionEvent.getAction();

        if (action == MotionEvent.ACTION_DOWN) {

            int row = (int) Math.ceil(y / cellSize) - 1;
            int column = (int) Math.ceil(x / cellSize) - 1;
            int position = oneDimensionalFromTwo(row, column);

            if (!bluetoothGame.playTile(position)) {
                return false;
            }

            invalidate();

            BluetoothConnectionService.instance.get().write("GAME-" + position);

            return true;

        }

        return false;

    }

    @Override
    protected void onMeasure(int width, int height) {

        super.onMeasure(width, height);

        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());
        cellSize = dimension / 3;

        setMeasuredDimension(dimension, dimension);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        drawGameBoard(canvas);
        drawMarkers(canvas);

        BluetoothGame bluetoothGame = BluetoothGameManager.getGame();
        if (bluetoothGame.getGameState() != GameState.IN_PROGRESS) {
            Intent popupIntent = new Intent(getContext(), PopUp.class);
            popupIntent.putExtra("result", bluetoothGame.getGameState().toString());
            getContext().startActivity(popupIntent);
        }

    }

    private void drawGameBoard(Canvas canvas) {

        paint.setColor(boardColor);
        paint.setStrokeWidth(16);

        for (int c = 1; c < 3; c++) {
            canvas.drawLine(cellSize * c, 0, cellSize * c, canvas.getWidth(), paint);
        }

        for (int r = 1; r < 3; r++) {
            canvas.drawLine(0, cellSize * r, canvas.getWidth(), cellSize * r, paint);
        }

    }

    private void drawMarkers(Canvas canvas) {

        for (BluetoothTile bluetoothTile : BluetoothGameManager.getGame().getBoard().getTiles()) {
            if (bluetoothTile.getState().equals(TileState.IKS)) {
                drawX(canvas, twoDimensionalFromOne(bluetoothTile.getID())[0], twoDimensionalFromOne(bluetoothTile.getID())[1]);
            } else if (bluetoothTile.getState().equals(TileState.OKS)) {
                drawO(canvas, twoDimensionalFromOne(bluetoothTile.getID())[0], twoDimensionalFromOne(bluetoothTile.getID())[1]);
            }
        }

    }

    private void drawX(Canvas canvas, int row, int col) {

        paint.setColor(XColor);

        canvas.drawLine(
                (float) ((col + 1) * cellSize - cellSize * 0.2),
                (float) (row * cellSize + cellSize * 0.2),
                (float) (col * cellSize + cellSize * 0.2),
                (float) ((row + 1) * cellSize - cellSize * 0.2),
                paint);

        canvas.drawLine(
                (float) (col * cellSize + cellSize * 0.2),
                (float) (row * cellSize + cellSize * 0.2),
                (float) ((col + 1) * cellSize - cellSize * 0.2),
                (float) ((row + 1) * cellSize - cellSize * 0.2),
                paint);

    }

    private void drawO(Canvas canvas, int row, int col) {

        paint.setColor(OColor);

        canvas.drawOval(
                (float) (col * cellSize + cellSize * 0.2),
                (float) (row * cellSize + cellSize * 0.2),
                (float) ((col * cellSize + cellSize) - cellSize * 0.2),
                (float) ((row * cellSize + cellSize) - cellSize * 0.2),
                paint);

    }

    private int oneDimensionalFromTwo(int x, int y) {

        return 3 * x + y;

    }

    private int[] twoDimensionalFromOne(int number) {

        int[] xy = new int[2];

        xy[0] = (number - (number % 3)) / 3;
        xy[1] = number % 3;

        return xy;

    }

    private final BroadcastReceiver broadcastReceiverInputStream = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String[] messageArray = intent.getStringExtra("theMessage").trim().split("-");

            if (!messageArray[0].equals("GAME")) {
                return;
            }

            try {

                BluetoothGame bluetoothGame = BluetoothGameManager.getGame();

                int playedPosition = Integer.parseInt(messageArray[1]);

                bluetoothGame.playTile(playedPosition);

                invalidate();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

}

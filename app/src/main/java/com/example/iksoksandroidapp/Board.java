package com.example.iksoksandroidapp;

public class Board {


    private Square[] squares;

    public Board()
    {
        squares = new Square[9];
    }


    public Square[] getSquares() {
        return squares;
    }

    public void setSquares(Square[] squares) {
        this.squares = squares;
    }
}

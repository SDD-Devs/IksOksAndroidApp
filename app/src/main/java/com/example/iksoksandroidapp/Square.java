/*
Project         : Iks/Oks
Date            : 11/11/2021
Description     : Klasa za svaku individualnu kockicu u tabeli. Sadrzi redoslednu poziciju
                u tabeli i value 'X' ili 'O' ili ' '.
 */

package com.example.iksoksandroidapp;

public class Square {



    //Data Members
    private int position;
    private char value;



    //Constructor
    public Square(int p, char v)
    {
        position = p;
        value = v;
    }



    //Getters & Setters
    public int getPosition() {
        return position;
    }
    public char getValue() {
        return value;
    }

}

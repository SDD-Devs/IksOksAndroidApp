/*
Project         : Iks/Oks
Date            : 11/11/2021
Description     : Klasa koja prestavlja Adapter za komunikaciju izmedju ArrayList<Tiles>
                kockica/polja i UI komponente u GridView-u. Ovo se zove Data-Binding i korisno je
                je jer kad se nesto izmeni u ArrayList, izmenice se i u UI-u.
 */
package com.example.iksoksandroidapp.IksOksLogic.classic_backend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.iksoksandroidapp.IksOksLogic.enums.GameState;
import com.example.iksoksandroidapp.IksOksLogic.pages.ClassicGameActivity;
import com.example.iksoksandroidapp.IksOksLogic.pages.PopUp;
import com.example.iksoksandroidapp.R;

public class BoardAdapter extends ArrayAdapter<Tile>{

    //Data Members
    Game game;
    Intent popupIntent;
    Context context;

    //Constructor
    public BoardAdapter(Context context, Game game) {
        super(context,0, game.getBoard().getTiles());
        this.game = game;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tile, parent, false);
        }


        //Declare and Initialize
        Tile tile = getItem(position);
        TextView txt_TileState = (TextView) convertView.findViewById(R.id.value);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("[TestLog]", "Tile clicked!");
                if (!game.playTile(position)) return;
                    txt_TileState.setText(GameManager.getStringFromTileState(game.getBoard().getTileByID(position).getState()));

                if (game.getGameState() != GameState.IN_PROGRESS)
                {
                    popupIntent = new Intent(getContext() , PopUp.class);
                    popupIntent.putExtra("result", game.getGameState().toString());
                    context.startActivity(popupIntent);
                }


            }
        });

        return convertView;
    }
}
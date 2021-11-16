/*
Project         : Iks/Oks
Date            : 11/11/2021
Description     : Klasa koja prestavlja Adapter za komunikaciju izmedju ArrayList<Tiles>
                kockica/polja i UI komponente u GridView-u. Ovo se zove Data-Binding i korisno je
                je jer kad se nesto izmeni u ArrayList, izmenice se i u UI-u.
 */
package com.example.iksoksandroidapp.IksOksLogic;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.iksoksandroidapp.R;

public class BoardAdapter extends ArrayAdapter<Tile>{

    //Data Members
    Game game;

    //Constructor
    public BoardAdapter(Context context, Game game) {
        super(context,0, game.getBoard().getTiles());
        this.game = game;
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

                switch (game.getGameState())
                {
                    case TIE:
                        break;
                    case IKS_WON:
                        break;
                    case OKS_WON:
                        break;
                    case IN_PROGRESS:

                        break;
                }


            }
        });

        return convertView;
    }
}

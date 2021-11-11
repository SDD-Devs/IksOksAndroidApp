/*
Project         : Iks/Oks
Date            : 11/11/2021
Description     : Klasa koja prestavlja Adapter za komunikaciju izmedju ArrayList<Squares>
                kockica/polja i UI komponente u GridView-u. Ovo se zove Data-Binding i korisno je
                je jer kad se nesto izmeni u ArrayList, izmenice se i u UI-u.
 */
package com.example.iksoksandroidapp;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends ArrayAdapter<Square>{


    //Constructor
    public BoardAdapter(Context context, ArrayList<Square> squares) {
        super(context,0, squares);
    }



    /*
    Function name       : getView
    Function desc       : Inherited function koja se je odgovorna za primenu naseg layout-a
                        u GridView i promene njenog izgleda,
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Square sq = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.square, parent, false);
        }

        TextView txt_Value = (TextView) convertView.findViewById(R.id.value);
        txt_Value.setText(String.valueOf(sq.getValue()) );

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("[TEST]:", "View Item clicked!");
            }
        });

        return convertView;
    }
}

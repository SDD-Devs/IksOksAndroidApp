package com.example.iksoksandroidapp.IksOksLogic.other;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.iksoksandroidapp.R;


public class SimpleFragment extends Fragment {

    private static final int YES = 0;
    private static final int NO = 1;


    public SimpleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_simple, container, false);
        final RadioGroup radioGroup = rootView.findViewById(R.id.radio_group);

        // TODO: Set the radioGroup onCheckedChanged listener.
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                TextView textView = rootView.findViewById(R.id.fragment_header);
                switch (index) {
                    case YES: // User chose "Yes."
                        textView.setText("YES");
                        break;
                    case NO: // User chose "No."
                        textView.setText("NO");
                        break;
                    default: // No choice made.
                        // Do nothing.
                        break;
                }
            }
        });


        // Return the View for the fragment's UI.
        return rootView;







        //return inflater.inflate(R.layout.fragment_simple, container, false);


    }
}
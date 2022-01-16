package com.example.iksoksandroidapp.IksOksLogic.network_mode.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.iksoksandroidapp.IksOksLogic.network_mode.backend.Client;
import com.example.iksoksandroidapp.R;

public class NetworkSetupActivity extends AppCompatActivity {

    Button btnCreateRoom, btnJoinRoom;
    EditText etxtCode;
    public Client client;
    public static NetworkSetupActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_network_setup);



        //Controols
        btnCreateRoom = (Button) findViewById(R.id.btn_Create);
        btnJoinRoom = (Button) findViewById(R.id.btn_Join);
        etxtCode = (EditText) findViewById(R.id.etxt_Code);


        client = new Client();
        new Thread(client).start();


        //Event listeners
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoom();

            }
        });

        btnJoinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinRoom();
            }
        });

    }

    private void createRoom() {
        String cmd = "CR";
        client.sendMessage(cmd);
    }

    private void joinRoom(){
        String code = etxtCode.getText().toString();
        String cmd = "JR-"+code;
        client.sendMessage(cmd);
    }
}
package com.example.businesscardreader;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Switch;
import androidx.cardview.widget.CardView;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

// initiate a Switch
        Switch simpleSwitch = (Switch) findViewById(R.id.switch1);

// check current state of a Switch (true or false).
        Boolean switchState = simpleSwitch.isChecked();

    }
}

package com.example.businesscardreader;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;

import com.example.businesscardreader.CameraReaderActivity;

public class MainActivity extends AppCompatActivity {

    CardView bt_scancard;
    CardView bt_contactslist;
    CardView bt_connectToDb;
    CardView bt_settings;
    CardView bt_contactus;
    CardView bt_aboutus;
    CardView bt_scantxt;
    CardView bt_savedtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_scancard = (CardView) findViewById(R.id.scancard);
        bt_contactslist = (CardView) findViewById(R.id.contactlist);
        bt_connectToDb = (CardView) findViewById(R.id.connectapi);
        bt_settings = (CardView) findViewById(R.id.settings);
        bt_contactus = (CardView) findViewById(R.id.contactus);
        bt_aboutus = (CardView) findViewById(R.id.aboutus);
        bt_scantxt = (CardView) findViewById(R.id.scantxt);
        bt_savedtxt = (CardView) findViewById(R.id.savedtxt);

        bt_scancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraReaderActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt_contactslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileListActivity.class);
                startActivity(intent);

            }
        });
        bt_connectToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConnectToApi.class);
                startActivity(intent);

            }
        });
        bt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);

            }
        });
        bt_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactUs.class);
                startActivity(intent);

            }
        });
        bt_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent);

            }
        });
        bt_scantxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveTxt.class);
                startActivity(intent);

            }
        });
        bt_savedtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SavedTxt.class);
                startActivity(intent);

            }
        });
    }
}

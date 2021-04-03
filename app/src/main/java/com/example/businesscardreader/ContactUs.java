package com.example.businesscardreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactUs extends AppCompatActivity {

    EditText emailsender;
    EditText emailsubject;
    EditText emailbody;
    Button sendemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        emailsender = findViewById(R.id.emailsender);
        emailbody = findViewById(R.id.emailbody);
        emailsubject = findViewById(R.id.emailsubject);
        sendemail = findViewById(R.id.btn_sendemail);

        String emailrecipients = "gmjatoi789@gmail.com";
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + emailrecipients));
                intent.putExtra(Intent.EXTRA_SUBJECT, emailsubject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, emailbody.getText().toString());
                startActivity(intent);

//                try {
//                    GMailSender sender = new GMailSender("username@gmail.com", "password");
//                    sender.sendMail(
//                            ""+emailsubject.getText().toString(),
//                            ""+emailbody.getText().toString(),
//                            ""+emailsender.getText().toString(),
//                            ""+emailrecipients);
//                    Log.e("SendMail", "Sended" );
//                } catch (Exception e) {
//                    Log.e("SendMail", e.getMessage(), e);
//                }

            }
        });
    }
}

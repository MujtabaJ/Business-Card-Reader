package com.example.businesscardreader;


import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfileListActivity extends AppCompatActivity {

    private ProfileDao profileDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        profileDao = ProfileDao.getInstance(this);
        ListView listView = (ListView) findViewById(R.id.list_view);

        Cursor profileData = profileDao.loadDataForMinimalList();
        if (profileData == null){
            Utils.displayErrorDialog(this);
        }else {
            List<String> profileItems = new ArrayList<String>();
            for(profileData.moveToFirst(); !profileData.isAfterLast(); profileData.moveToNext()){
                StringBuilder sb = new StringBuilder();
                String[] contact = new String[4];
                contact[0]= profileData.getString(1);
                contact[1]= profileData.getString(2);
                contact[2]= profileData.getString(3);
                contact[3]= profileData.getString(4);

                sb.append(profileData.getString(1))
                        .append(" / ")
                        .append(profileData.getString(2))
                        .append(profileData.getString(3))
                        .append(profileData.getString(4))
                        .append(ProfileArrayAdapter.DELIMITER)
                        .append(profileData.getString(0));
                profileItems.add(sb.toString());
                Profile profile = new Profile(profileData.getString(0), contact[0],contact[0],contact[0],contact[0]);
                //profileItems.add(profile);
            }
            Log.d(ProfileListActivity.class.getName(), "Found "+profileItems.size()+" profiles in list");
            profileData.close();

            ProfileArrayAdapter adapter = new ProfileArrayAdapter(
                    this,
                    profileItems.toArray(new String[profileItems.size()])
            );
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }


    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ProfileListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

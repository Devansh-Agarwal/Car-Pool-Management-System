package com.swe.cpms;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button mDriver, mRider, mLogout, mView_profile, mMyRides, mHelpcentre;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        System.out.println("in main act\n");

        mRider = (Button) findViewById(R.id.Rider);
        mDriver = (Button) findViewById(R.id.Driver);
        mLogout=(Button) findViewById(R.id.log_out);
        mView_profile=(Button) findViewById(R.id.view_profile);
        mMyRides = findViewById(R.id.my_rides);
        mHelpcentre = (Button) findViewById(R.id.view_help_centre);


        mRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RideRequestActivity.class);
                startActivity(intent);
                //finish();
                //return;
            }
        });

        mDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RideOfferActivity.class);
                startActivity(intent);
//                finish();
//                return;
            }
        });
        mView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewProfile.class);
                startActivity(intent);
//                finish();
//                return;
            }
        });


        mMyRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyRides.class);
                  startActivity(intent);
//                finish();
//                return;
            }
        });

        mHelpcentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
//                finish();
//                return;
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, AskPhoneNumber.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }


}

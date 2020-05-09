package com.swe.cpms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button mDriver, mRider,mLogout,mView_profile,mView_upcoming, mMyRides, mHelpcentre, ,mSos;

    public static int getValue(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key,0);
    }
    Context homeActivity;



  


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeActivity= this;
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("hakuna", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "The token is "+token;
                        FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();
                        Log.d("hakuna", msg);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> userToken = new HashMap<>();
                        userToken.put("token", token);
                        // Add a new document with a generated ID
                        db.collection("user_notification_token")
                                .document(user_auth.getUid())
                                .set(userToken)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("hakuna", "Token successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("hakuna", "Error writing token", e);
                                    }
                                });
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        mRider = (Button) findViewById(R.id.Rider);
        mDriver = (Button) findViewById(R.id.Driver);
        mLogout=(Button) findViewById(R.id.log_out);
        mView_profile=(Button) findViewById(R.id.view_profile);
        mView_upcoming=(Button) findViewById(R.id.view_upcoming);
        mMyRides = findViewById(R.id.my_rides);
        mHelpcentre = (Button) findViewById(R.id.view_help_centre);
        mSos = (Button)  findViewById(R.id.view_sos);


       // String temp="Upcoming Rides "+"("+ getValue("upcoming_rides",this)+")";
        mView_upcoming.setText("Upcoming Rides");
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

        mView_upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Upcoming_rides.class);
                    startActivity(intent);
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
        mSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SosActivity.class);
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            String temp="Upcoming Rides "+"("+ getValue("upcoming_rides",homeActivity)+")";
            mView_upcoming.setText(temp);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(mMessageReceiver, new IntentFilter("notification"));
    }

    //Must unregister onPause()
    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mMessageReceiver);
    }


    //This is the handler that will manager to process the broadcast intent



}

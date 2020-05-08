package com.swe.cpms;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.location.Geocoder;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;


public class Upcoming_rides extends AppCompatActivity {

    TextView mRideDetails;
    Button mStartTrip,mEndTrip;
    LinearLayout mUpcomingButtons;
    private FirebaseFunctions mFunctions;

    private Task<String> endTrip() {
        return mFunctions
                .getHttpsCallable("endTrip")
                .call()
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        return result;
                    }

                });
    }

    private Task<String> startTrip() {
        // Create the arguments to the callable function.
        return mFunctions
                .getHttpsCallable("startTrip")
                .call()
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }

    private Task<String> fetchDetails() {
        // Create the arguments to the callable function.
        return mFunctions
                .getHttpsCallable("fetchDetails")
                .call()
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_rides);
        mRideDetails=(TextView) findViewById(R.id.upcoming_rideDetails);
        mStartTrip=(Button) findViewById(R.id.upcoming_start_trip);
        mEndTrip=(Button) findViewById(R.id.upcoming_end_trip);
        mUpcomingButtons=(LinearLayout) findViewById(R.id.upcoming_buttons);
        mFunctions = FirebaseFunctions.getInstance();

        fetchDetails()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }
                            Log.e("hakuna", "fetching upcoming ride details not sucessfull: "+ task.getException() );
                        }
                        Log.d("hakuna", "fetching upcoming ride details sucessful"+task.getResult());
                        mRideDetails.setText(task.getResult());
                        try {
                            JSONObject jsonObject = new JSONObject(task.getResult());
                            String isDriver=jsonObject.get("idDriver").toString();
                            if(isDriver.equals("true"))
                            {
                                mUpcomingButtons.setVisibility(0);
                            }
                        }catch (JSONException err){
                            Log.d("hakuna", err.toString());
                        }
                    }
                });

        mStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hakuna","start tip clicked");
                startTrip()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                        Object details = ffe.getDetails();
                                    }
                                    Log.e("hakuna", "startTrip not sucessfull: "+ task.getException() );
                                }
                                Log.d("hakuna", "start trip sucessful "+task.getResult());
                            }
                        });
            }
        });
        mEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hakuna","end trip clicked");
                startTrip()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                        Object details = ffe.getDetails();
                                    }
                                    Log.e("hakuna", "end trip not sucessfull :"+task.getException() );
                                }
                                Log.d("hakuna", "end trip sucessful "+task.getResult());
                            }
                        });
            }
        });


    }
}

package com.swe.cpms;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
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
    Button mStartTrip,mEndTrip,mTracking,mTrackingPassenger;
    LinearLayout mUpcomingButtons,mUpcomingButtonsPassenger;
    private FirebaseFunctions mFunctions;

    private Task<String> endTrip(String text) {
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);
        return mFunctions
                .getHttpsCallable("endTrip")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        Object result= task.getResult().getData();
                        Log.d("hakuna", "then: the value of object is ");
                        return "dfsdfsfs";
                    }

                });
    }

    private Task<String> startTrip(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);
        return mFunctions
                .getHttpsCallable("startTrip")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        Object result= task.getResult().getData();
                        Log.d("hakuna", "then: the value of object is ");
                        return "dfsdfsfs";
                    }
                });
    }

    private Task<String> fetchDetails(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);
        return mFunctions
                .getHttpsCallable("fetchDetails")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result= task.getResult().getData().toString();
                        Log.d("hakuna", "then: the value of object is ");
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
        mTracking=(Button) findViewById(R.id.upcoming_live_tracking);
        mTrackingPassenger=(Button) findViewById(R.id.upcoming_liveTracking_passenger);
        mUpcomingButtons=(LinearLayout) findViewById(R.id.upcoming_buttons);
        mUpcomingButtonsPassenger=(LinearLayout) findViewById(R.id.upcoming_buttons_passenger);
        mFunctions = FirebaseFunctions.getInstance();

        fetchDetails("dummy")
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
                            Log.e("hakuna", "fetching upcoming ride details not successful: "+ task.getException() );
                        }
                        Log.d("hakuna", "fetching upcoming ride details successful"+task.getResult());
                        mRideDetails.setText(task.getResult());
                        try {
                            JSONObject jsonObject = new JSONObject(task.getResult());
                            String isDriver=jsonObject.get("isDriver").toString();
                            if(isDriver.equals("true"))
                            {
                                mUpcomingButtons.setVisibility(View.VISIBLE);
                            }
                            else{
                                mUpcomingButtonsPassenger.setVisibility(View.VISIBLE);
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
                startTrip("dummy")
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
                endTrip("dummy")
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
                                finish();

                            }
                        });
            }
        });
        mTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String details = (String)mRideDetails.getText();
                try {
                    JSONObject jsonObject = new JSONObject(details);
                    String rideID=jsonObject.get("rideId").toString();
                    Intent serviceIntent = new Intent(DriverTrackingActivity.class.getName());
                    serviceIntent.putExtra("rideID", rideID);
    //                Context context;
    //                context.startService(serviceIntent);
                    startService(serviceIntent);

                }catch (JSONException err){
                    Log.d("hakuna", err.toString());
                }
                Log.d("details", details);
//
            }
        });
        mTrackingPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String details = (String)mRideDetails.getText();
                try {
                    JSONObject jsonObject = new JSONObject(details);
                    String rideID=jsonObject.get("rideId").toString();
                    Intent intent = new Intent(Upcoming_rides.this, PassengerTrackingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("rideID",rideID);
                    intent.putExtras(bundle);
                    Log.d("bla", "inRideRequest");
                    startActivity(intent);
                }catch (JSONException err){
                    Log.d("hakuna", err.toString());
                }
            }
        });


    }
}

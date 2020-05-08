package com.swe.cpms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.DocumentCollections;


import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MyRides extends AppCompatActivity {
    LinearLayout myLinearLayout, outerLayout;

    private FirebaseUser user_auth;
    Geocoder geocoder;
    Double ecoMetDist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides);

        user_auth = FirebaseAuth.getInstance().getCurrentUser();
        geocoder = new Geocoder(MyRides.this , Locale.getDefault());


        myLinearLayout = (LinearLayout) findViewById(R.id.frameMyRides);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("AllRides").document(user_auth.getUid()).collection("Rides");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot coll = task.getResult();
                    List<DocumentSnapshot> rides= coll.getDocuments();
                    for(int i=0;i<rides.size();i++){
                        Map<String,Object> data = rides.get(i).getData();

                        Double lat = (Double) data.get("StartLat");
                        Double lng = (Double) data.get("StartLon");
                        LatLng source = new LatLng(lat, lng);

                        lat = (Double) data.get("DestLat");
                        lng = (Double) data.get("destLon");
                        LatLng dest = new LatLng(lat, lng);


                        String startTime = data.get("StartTime").toString();
//                        String endTime = data.get("EndTime").toString();
                        String date = data.get("date").toString();
                        int seats = Integer.parseInt(data.get("numberOfPeople").toString().trim());
                        try {
                            displayMyRides(source, dest, startTime, date, seats);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    Toast.makeText(MyRides.this,"Rides fetched successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyRides.this,"Failed to fetch ride", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void displayMyRides(LatLng source, LatLng dest, String startTime, String date, int seats) throws IOException {
        myLinearLayout = (LinearLayout) findViewById(R.id.frameMyRides);
        Context context;
        TextView textview, textview1, textview2, textview3, textview4, textview5, textview6;
        LinearLayout innerLayout1,innerLayout2;
        CardView cardview;
        context = getApplicationContext();

        final FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();
        findDis(source, dest);

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 40, 40, 10);
       List<Address> addresses;

        cardview = new CardView(context);
        cardview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));




        cardview.setRadius(15);
        cardview.setPadding(25, 25, 25, 25);
        cardview.setCardBackgroundColor(Color.WHITE);
        cardview.setMaxCardElevation(30);
        cardview.setMaxCardElevation(6);
        cardview.setUseCompatPadding(true);

        outerLayout = new LinearLayout(context);
        outerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        outerLayout.setOrientation(LinearLayout.VERTICAL);


        textview3 = new TextView(context);
        params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 40, 40, 10);
        textview3.setLayoutParams(params);
        addresses = geocoder.getFromLocation(source.latitude, source.longitude, 1);

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();

        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();

        address = address + " " + city + " " + state + " " + country;
        textview3.setText("From: "+address);

        textview4 = new TextView(context);
        textview4.setLayoutParams(params);
        addresses = geocoder.getFromLocation(dest.latitude, dest.longitude, 1);

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
        state = addresses.get(0).getAdminArea();
        country = addresses.get(0).getCountryName();

        address = address + " " + city + " " + state + " " + country;
        textview4.setText("To: "+address);


        outerLayout.addView(textview3);
        outerLayout.addView((textview4));

        innerLayout2 = new LinearLayout(context);
        innerLayout2.setOrientation(LinearLayout.HORIZONTAL);

        textview5 = new TextView(context);

        params = new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                3
        );
        params.setMargins(10, 40, 10, 10);
        textview5.setLayoutParams(params);
        textview5.setGravity(Gravity.LEFT);
        textview5.setText("Time: "+date + " " + startTime);

        textview6 = new TextView(context);
        textview6.setLayoutParams(params);
        textview6.setGravity((Gravity.RIGHT));
        textview6.setText("Co-passengers: "+Integer.toString(seats));


        Double ecoMetric=0.0;
        ecoMetDist = Math.sqrt((source.latitude - dest.latitude)*(source.latitude - dest.latitude)
                                + (source.longitude - dest.longitude)*(source.longitude - dest.longitude));

        ecoMetric = Math.abs((ecoMetDist * 10 * 132 * 0.75) / (seats));

        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);


        textview1 = new TextView(context);
        params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 40, 10, 10);
        textview1.setLayoutParams(params);
        textview1.setGravity(Gravity.LEFT);
        String s = "You reduced " + df.format(ecoMetric) + "g of carbon emission with this trip";
        textview1.setText(s);

//        button = new Button(context);
//        button.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
//        button.setText("Request ride");





        innerLayout2.addView(textview5);
        innerLayout2.addView(textview6);

        outerLayout.addView(innerLayout2);

        outerLayout.addView(textview1);
        cardview.addView(outerLayout);
        myLinearLayout.addView(cardview);

//        }
    }

    private void findDis(LatLng source, LatLng dest) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+Double.toString(source.latitude)+
                ","+Double.toString(source.longitude)+"&destination="+Double.toString(dest.latitude)+","+
                Double.toString(dest.longitude)+"&sensor=false&mode=driving&alternatives=true"+"&key="+getString(R.string.google_api_key);

        Log.d("eco", url);
        new MyRides.connectAsyncTask(url).execute();
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(MyRides.this);
            progressDialog.setMessage("Fetching data, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            MyRides.JSONParser jParser = new MyRides.JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                Log.d("eco","result is null");
                getDist(result);
            }
        }
    }

    private void getDist(String result) {
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);

            JSONArray legArr = routes.getJSONArray("legs");
            JSONObject legs = legArr.getJSONObject(0);
            JSONObject duration = legs.getJSONObject("distance");
            ecoMetDist = Double.valueOf(duration.getString("value"));
        } catch (Exception e) {
            Log.d("eco", "exception");
            e.printStackTrace();
        }
    }

    public static class JSONParser {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // constructor
        public JSONParser() {
        }

        public String getJSONFromUrl(String url) {

            // Making HTTP request
            try {
                // defaultHttpClient
                URL myUrl = new URL(url);
                HttpsURLConnection conn = null;
                conn = (HttpsURLConnection)myUrl.openConnection();
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String inputLine, data;
                data="";
                while ((inputLine = br.readLine()) != null) {
                    System.out.println(inputLine);
                    data+=inputLine;
                }
                br.close();
                return data;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            ]
            return "";
        }



    }
}
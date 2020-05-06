package com.swe.cpms;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
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
import com.google.maps.android.PolyUtil;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class FetchedRidesActivity extends AppCompatActivity {

    LinearLayout myLinearLayout, outerLayout;

    LatLng reqSource, reqDest;
    String reqTime, reqDate;
    int reqSeats;

    long timeDiff;
    Polyline polyline;
    List <LatLng> routePoints;

    Geocoder geocoder = new Geocoder(FetchedRidesActivity.this ,Locale.getDefault());
    List<Address> addresses;

    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetched_rides);

        Log.d("bla", "before database");
        bundle = getIntent().getExtras();

        String lat = bundle.getString("finalSourceLat");
        String lng = bundle.getString("finalSourceLng");
        reqSource = new LatLng(Double.valueOf(lat), Double.valueOf(lng));

        lat = bundle.getString("finalDestLat");
        lng = bundle.getString("finalDestLng");
        reqDest = new LatLng(Double.valueOf(lat), Double.valueOf(lng));

        reqTime = bundle.getString("time");
        reqDate = bundle.getString("date");
        reqSeats = Integer.valueOf(bundle.getString("seats"));

        Log.d("bla", "before database");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference  colRef = db.collection("OfferedRides");

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot coll = task.getResult();
                    List<DocumentSnapshot> rides= coll.getDocuments();

                    for(int i=0;i<rides.size();i++){
                        Map<String,Object> data = rides.get(0).getData();

                        Double lat = (Double) data.get("startLat");
                        Double lng = (Double) data.get("startLon");
                        LatLng source = new LatLng(lat, lng);

                        lat = (Double) data.get("DestLat");
                        lng = (Double) data.get("DestLon");
                        LatLng dest = new LatLng(lat, lng);

                        String time = data.get("StartTime").toString();
                        String date = data.get("date").toString();
                        int seats = (Integer)data.get("totalNumberOfSeats");

                        boolean ans = checkIfMatches(source, dest, time, date, seats);
                        if(ans || true){
                            Log.d("bla", "after checkIfMatches");
                            try {
                                displayDriver(source, dest, time, date, seats);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
                else{
                    Toast.makeText(FetchedRidesActivity.this,"Collection query failed",Toast.LENGTH_SHORT).show();
                    Log.d("hakuna", "Collection query failed");
                }
            }
        });


        myLinearLayout = (LinearLayout) findViewById(R.id.frame);
//        final int N = 20; // total number of textviews to add
//
//        Context context;
//        CardView cardview;
//        TextView textview, textview1, textview2, textview3, textview4, textview5, textview6;
//        Button button;
//        LinearLayout innerLayout1,innerLayout2;
//
//        context = getApplicationContext();
//        //Get the bundle
//        String fare = bundle.getString("fare");
//
//
//        TextView mFare = (TextView)findViewById(R.id.fare);
//        mFare.setText(fare);
//
//        for (int i = 0; i < N; i++) {
//
//            cardview = new CardView(context);
//
//            cardview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//
//            cardview.setRadius(15);
//            cardview.setPadding(25, 25, 25, 25);
//            cardview.setCardBackgroundColor(Color.WHITE);
//            cardview.setMaxCardElevation(30);
//            cardview.setMaxCardElevation(6);
//            cardview.setUseCompatPadding(true);
//
//            outerLayout = new LinearLayout(context);
//            outerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//            outerLayout.setOrientation(LinearLayout.VERTICAL);
//
//            innerLayout1 = new LinearLayout(context);
//            innerLayout1.setOrientation(LinearLayout.HORIZONTAL);
//
//            textview1 = new TextView(context);
//            textview1.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 4));
//            textview1.setText("name");
//
//            textview2 = new TextView(context);
//            textview2.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
//            textview2.setText("rating");
//
//            innerLayout1.addView(textview1);
//            innerLayout1.addView(textview2);
//
//            outerLayout.addView(innerLayout1);
//
//            textview3 = new TextView(context);
//            textview3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//            textview3.setText("source");
//
//            textview4 = new TextView(context);
//            textview4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//            textview4.setText("destination");
//
//            outerLayout.addView(textview3);
//            outerLayout.addView((textview4));
//
//            innerLayout2 = new LinearLayout(context);
//            innerLayout2.setOrientation(LinearLayout.HORIZONTAL);
//
//            textview5 = new TextView(context);
//            textview5.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
//            textview5.setText("time");
//
//            textview6 = new TextView(context);
//            textview6.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
//            textview6.setText("seats");
//
//            button = new Button(context);
//            button.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
//            button.setText("Request ride");
//
//            innerLayout2.addView(textview5);
//            innerLayout2.addView(textview6);
//            innerLayout2.addView(button);
//
//            outerLayout.addView(innerLayout2);
//            cardview.addView(outerLayout);
//            myLinearLayout.addView(cardview);
//        }
    }

    private void displayDriver(LatLng offSource, LatLng offDest, String offTime, String offDate, int offSeats) throws IOException {
        final int N = 20; // total number of textviews to add

        Context context;
        CardView cardview;
        TextView textview, textview1, textview2, textview3, textview4, textview5, textview6;
        Button button;
        LinearLayout innerLayout1,innerLayout2;

        context = getApplicationContext();
        //Get the bundle
        String fare = bundle.getString("fare");


        TextView mFare = (TextView)findViewById(R.id.fare);
        mFare.setText(fare);

//       List<Address> addresses;

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

        innerLayout1 = new LinearLayout(context);
        innerLayout1.setOrientation(LinearLayout.HORIZONTAL);

        textview1 = new TextView(context);
        textview1.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 4));
        textview1.setText("name");

        textview2 = new TextView(context);
        textview2.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
        textview2.setText("rating");

        innerLayout1.addView(textview1);
        innerLayout1.addView(textview2);

        outerLayout.addView(innerLayout1);

        textview3 = new TextView(context);
        textview3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addresses = geocoder.getFromLocation(offSource.latitude, offSource.longitude, 1);

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();

        address = address + " " + city + " " + " state" + " " + country;
        textview3.setText(address);

        textview4 = new TextView(context);
        textview4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        List<Address> addresses = geocoder.getFromLocation(offSource.latitude, offSource.longitude, 1);

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
        state = addresses.get(0).getAdminArea();
        country = addresses.get(0).getCountryName();

        address = address + " " + city + " " + " state" + " " + country;
        textview4.setText(address);

        outerLayout.addView(textview3);
        outerLayout.addView((textview4));

        innerLayout2 = new LinearLayout(context);
        innerLayout2.setOrientation(LinearLayout.HORIZONTAL);

        textview5 = new TextView(context);
        textview5.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
        textview5.setText(offDate + " " + offTime);

        textview6 = new TextView(context);
        textview6.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
        textview6.setText(offSeats);

        button = new Button(context);
        button.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
        button.setText("Request ride");

        innerLayout2.addView(textview5);
        innerLayout2.addView(textview6);
        innerLayout2.addView(button);

        outerLayout.addView(innerLayout2);
        cardview.addView(outerLayout);
        myLinearLayout.addView(cardview);
//        }
    }

    private boolean checkIfMatches(LatLng offSource, LatLng offDest, String offTime, String offDate, int offSeats) {
        Log.d("bla", "inside check if matches");
        timeFun(offSource, reqSource);//calculates time taken from driver start point to rider start point

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String ts = offDate + " " + offTime;
            Date date = df.parse(ts);
            long epochSrc = date.getTime()/1000;

            ts = reqDate + " " + reqTime;
            date = df.parse(ts);
            long epochDest = date.getTime()/1000;

            long actualTimeDiff = epochDest - epochSrc;

            if((reqSeats <= offSeats) && (Math.abs(actualTimeDiff - timeDiff))<300 &&
                    PolyUtil.isLocationOnEdge(reqSource, routePoints, true) &&
                    PolyUtil.isLocationOnEdge(reqDest, routePoints, true)){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    void  timeFun(LatLng src, LatLng dest){

        String url= "https://maps.googleapis.com/maps/api/directions/json?origin="+Double.toString(src.latitude)+
                ","+Double.toString(src.longitude)+"&destination="+Double.toString(dest.latitude)+","+
                Double.toString(dest.longitude)+"&sensor=false&mode=driving&alternatives=true"+"&key="+getString(R.string.google_api_key);;
        new FetchedRidesActivity.connectAsyncTask(url).execute();
//        return time;
    }

    //used for polyline
    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(RideOfferActivity.this);
//            progressDialog.setMessage("Fetching route, Please wait...");
//            progressDialog.setIndeterminate(true);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            progressDialog.hide();
            if (result != null) {
                getTimeAndPath(result);
            }
        }
    }

    private void getTimeAndPath(String result) {
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);

            JSONObject legs = routes.getJSONObject("legs");
            JSONObject duration = routes.getJSONObject("duration");
            timeDiff = Long.valueOf(duration.getString("value"));

            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");

            String encodedString = overviewPolylines.getString("points");
            routePoints = decodePoly(encodedString);



        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
    //used for polyline
    public class JSONParser {

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
            return "";
        }
    }


}



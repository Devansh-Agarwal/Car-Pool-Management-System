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
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FieldValue;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class FetchedRidesActivity1 extends AppCompatActivity {

    LinearLayout myLinearLayout, outerLayout;
    CardView cardview;

    LatLng reqSource, reqDest;
    String reqTime, reqDate;
    int reqSeats;

    LatLng offSource, offDest;
    String offTime, offDate;
    int offSeats, offPassenger;

    int reqCount=0;

    final FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<Double> timeDiff, timeDiffDest;
    Polyline polyline;
    List <LatLng> routePoints;

    Geocoder geocoder;
    List<Address> addresses;
    boolean sleep=true;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetched_rides);


        geocoder = new Geocoder(FetchedRidesActivity1.this ,Locale.getDefault());

        bundle = getIntent().getExtras();

        String lat = bundle.getString("finalSourceLat");
        String lng = bundle.getString("finalSourceLng");
        reqSource = new LatLng(Double.valueOf(lat), Double.valueOf(lng));


        lat = bundle.getString("finalDestLat");
        lng = bundle.getString("finalDestLng");
        reqDest = new LatLng(Double.valueOf(lat), Double.valueOf(lng));

        reqTime = bundle.getString("startTime");
        reqDate = bundle.getString("date");
        reqSeats = Integer.valueOf(bundle.getString("seats"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference  colRef = db.collection("OfferedRides");

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot coll = task.getResult();
                    List<DocumentSnapshot> rides = coll.getDocuments();

                    String origins = "";
                    int flag = 0;
                    reqCount = rides.size();
                    for (int i = 0; i < rides.size(); i++) {
                        Map<String, Object> data = rides.get(i).getData();

                        Double lat = Double.valueOf(data.get("StartLat").toString());
                        Double lng = (Double) data.get("StartLon");

                        if(i!=0)origins+="|";
                        origins+=data.get("StartLat").toString()+","+data.get("StartLon").toString();
                    }

                    String destinationUrl = Double.toString(reqSource.latitude)+","+Double.toString(reqSource.longitude);
                    destinationUrl += "|" + Double.toString(reqDest.latitude)+","+Double.toString(reqDest.longitude);
                    String url="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origins+"&destinations="+destinationUrl+"&key="+getString(R.string.google_api_key);
                    Log.d("imp",url);
                    new FetchedRidesActivity1.connectAsyncTask(url).execute();
                }
            }
        });
    }

    private void addIdToMatchedRidesTable(final String rideid, final String driverUid) {
        final CollectionReference collRef= FirebaseFirestore.getInstance().collection("MatchedRides");
        DocumentReference docIdRef = collRef.document(rideid);
        Log.d("update", user_auth.getUid());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("DriverId", driverUid);
                        docData.put("PassengerIds", Arrays.asList(user_auth.getUid()));
                        Log.d("update", user_auth.getUid());
                        collRef.document(rideid).set(docData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("update", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("update", "Error writing document", e);
                                    }
                                });
                        ;
                    } else {
                        collRef.document(rideid).update("PassengerIds", FieldValue.arrayUnion(user_auth.getUid()));
                    }
                } else {
                    Log.d("update", "Failed with: ", task.getException());
                }
            }
        });
    }
    private void UpdDocOfferRides(String driverUid, String rideId, int numberOfSeats, int numberOfPeople) {
        CollectionReference collRef = FirebaseFirestore.getInstance()
                .collection("OfferedRides");
        Map<String, Object> ride = new HashMap<>();
        ride.put("totalNumberOfSeats", Integer.toString(numberOfSeats));
        ride.put("numberOfPeople", Integer.toString(numberOfPeople));
        collRef.document(rideId)
                .update(ride)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("hakuna", "OfferedRides successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("hakuna", "Error updating OfferedRides", e);
                    }
                });


    }

    private void delDocOfferRides(String driverUid, String rideId) {
        CollectionReference collRef = FirebaseFirestore.getInstance()
                .collection("OfferedRides");
        collRef.document(rideId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("hakuna", "Ride successfully deleted from OfferedRides");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("hakuna", "Error deleting from offered rides", e);
                    }
                });
    }

    private void upDocAllRidesDriver(String driverUid, String rideId, int numberOfSeats, Object passList, int numberOfPeople) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("AllRides").document(driverUid).collection("Rides").document(rideId);
        Map<String, Object> ride = new HashMap<>();
        ride.put("totalNumberOfSeats", Integer.toString(numberOfSeats));
        ride.put("numberOfPeople", Integer.toString(numberOfPeople));
        if(passList == null) {
            ride.put("passengerUid", Arrays.asList(user_auth.getUid()));
        }
        else {
            ride.put("passengerUid", FieldValue.arrayUnion(user_auth.getUid()));
        }
        docRef
                .update(ride)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("hakuna", "All Rides successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("hakuna", "Error updating All Rides", e);
                    }
                });
    }

    private void upDocAllRidesPassenger(String driverId, String rideId, String date, String time, String seats, LatLng finalSource, LatLng finalDest, String passengers) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("AllRides").document(user_auth.getUid()).collection("Rides").document(rideId);
        Map<String, Object> ride = new HashMap<>();
        ride.put("date", date);
        ride.put("StartTime", time);
        ride.put("endTime", null);
        ride.put("totalNumberOfSeats", seats);
        ride.put("StartLat", finalSource.latitude);
        ride.put("StartLon", finalSource.longitude);
        ride.put("DestLat", finalDest.latitude);
        ride.put("destLon", finalDest.longitude);
        ride.put("isDriver", false);
        ride.put("driverUid", driverId);
        ride.put("passengerUid", null);
        ride.put("numberOfPeople",passengers);
        docRef.
                set(ride)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("hakuna", "DocumentSnapshot successfully written!");
//                    Toast.makeText(RideOfferActivity.this,"Ride successfully stored in AllRides database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("hakuna", "Error writing document", e);
//                    Toast.makeText(RideOfferActivity.this,"Ride storage unsuccessful in AllRides", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void displayDriver(String uid) throws IOException {
        myLinearLayout = (LinearLayout) findViewById(R.id.frame);
        Context context;
        TextView textview, textview1, textview2, textview3, textview4, textview5, textview6;
        LinearLayout innerLayout1,innerLayout2;

        context = getApplicationContext();
//        String fare = bundle.getString("fare");
        double approxDistance = getApproxDistance(reqSource.latitude, reqSource.longitude, reqDest.latitude, reqDest.longitude, 'k');
        double fare = 0.25 * 4 * reqSeats * approxDistance;
        DecimalFormat df = new DecimalFormat("#.##");
        fare = Double.valueOf(df.format(fare));
        TextView mFare = (TextView)findViewById(R.id.fare);
        TextView eFare = (TextView)findViewById(R.id.fareName);
        eFare.setText("Estimated Fare");
        mFare.setText("Rs." + Double.toString( fare));

        final String[] name = new String[1];
        final int[] rating = new int[1];

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String,Object> data = document.getData();
                        name[0] = data.getOrDefault("name", "Ashok").toString();
                        rating[0] = Integer.parseInt(data.get("avg_rating").toString().trim());
                    } else {
                        Log.d("bla", "No such document");
                    }
                } else {
                    Log.d("bla", "get failed with ", task.getException());
                }
            }
        });

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 40, 40, 10);
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
        params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 40, 40, 10);
        innerLayout1.setOrientation(LinearLayout.HORIZONTAL);

        textview1 = new TextView(context);
        params = new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                4
        );
        params.setMargins(10, 40, 10, 10);
        textview1.setLayoutParams(params);
        textview1.setGravity(Gravity.LEFT);
        if(name[0]==""){
            textview1.setText("Ashok");
        }
        else{
            textview1.setText(name[0]);
        }

        textview2 = new TextView(context);
        params = new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                2
        );
        params.setMargins(10, 40, 10, 10);
        textview2.setLayoutParams(params);
        textview2.setGravity(Gravity.RIGHT);
        textview2.setText(Integer.toString(rating[0])+"stars");


        innerLayout1.addView(textview1);
        innerLayout1.addView(textview2);

        outerLayout.addView(innerLayout1);

        textview3 = new TextView(context);
        params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 40, 40, 10);
        textview3.setLayoutParams(params);
        addresses = geocoder.getFromLocation(offSource.latitude, offSource.longitude, 1);

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();

        address = address + " " + city + " " + " state" + " " + country;
        textview3.setText("From: "+address);

        textview4 = new TextView(context);
        textview4.setLayoutParams(params);
        List<Address> addresses = geocoder.getFromLocation(offDest.latitude, offDest.longitude, 1);

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
        state = addresses.get(0).getAdminArea();
        country = addresses.get(0).getCountryName();

        address = address + " " + city + " " + " state" + " " + country;
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
        textview5.setText("Time: "+offDate + " " + offTime);

        textview6 = new TextView(context);
        textview6.setLayoutParams(params);
        textview6.setGravity((Gravity.RIGHT));
        textview6.setText("Seats Avaialable: "+Integer.toString(offSeats));

//        button = new Button(context);
//        button.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
//        button.setText("Request ride");

        innerLayout2.addView(textview5);
        innerLayout2.addView(textview6);

        outerLayout.addView(innerLayout2);
        cardview.addView(outerLayout);
        myLinearLayout.addView(cardview);
//        }
    }

    private boolean checkIfMatches(int i) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String ts = offDate + " " + offTime;
            Log.d("async", "offer "+ts);

            Date date = df.parse(ts);
            long epochSrc = date.getTime()/1000;

            ts = reqDate + " " + reqTime;
            Log.d("async", "request "+ts);
            date = df.parse(ts);
            long epochDest = date.getTime()/1000;

            long actualTimeDiff = epochDest - epochSrc;
            Log.d("async", Long.toString(epochDest));
            Log.d("async", Long.toString(epochSrc));
            Log.d("async actual time diff", Long.toString(actualTimeDiff));
            Log.d("async real time diff", Double.toString(timeDiff.get(i)));
            Log.d("async diff dest", Double.toString(timeDiffDest.get(i)));
            boolean check1, check2, check3;
            Log.d("async", Double.toString(reqSource.latitude)+" "+Double.toString(reqSource.longitude));
            check1 = PolyUtil.isLocationOnPath(reqSource, routePoints, true, 100);
            Log.d("async", Double.toString(reqDest.latitude)+" "+Double.toString(reqDest.longitude));
            check2 = PolyUtil.isLocationOnPath(reqDest, routePoints, true, 100);
//                LatLng l = new LatLng(17.06019, 79.28231);
            check3 = PolyUtil.isLocationOnPath(offSource, routePoints, true, 100) ;
            Log.d("async", Double.toString(offSource.latitude)+" "+Double.toString(offSource.longitude));

            if(check1)Log.d("sync", "source lies on route");
            if(check2)Log.d("sync", "dest lies on route");
            if(check3)Log.d("sync", "dont know why");

            if((reqSeats <= offSeats) && (Math.abs(actualTimeDiff - timeDiff.get(i)))<=1500 && check1 && check2 && (timeDiff.get(i) < timeDiffDest.get(i))){
                Log.d("async", "matched");
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    //used for polyline
    class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
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
//                getTimeAndPath(result);
                matchingAlgo(result);
            }
        }
    }

    private void matchingAlgo(String result) {
        getTimeAndPath(result);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference  colRef = db.collection("OfferedRides");

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@ NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot coll = task.getResult();
                    List<DocumentSnapshot> rides= coll.getDocuments();

                    int flag=0;
                    for(int i=0;i<rides.size();i++){
                        Map<String,Object> data = rides.get(i).getData();

                        Double lat = Double.valueOf(data.get("StartLat").toString());
                        Double lng = (Double) data.get("StartLon");

                        offSource = new LatLng(lat, lng);
//                        timeFun(source, reqSource);
                        lat = (Double) data.get("DestLat");
                        lng = (Double) data.get("destLon");
                        offDest = new LatLng(lat, lng);

                        offTime = data.get("StartTime").toString();
                        offDate = data.get("date").toString();
                        offSeats = Integer.parseInt(data.get("totalNumberOfSeats").toString().trim());
                        offPassenger = Integer.parseInt(data.get("numberOfPeople").toString().trim());
                        routePoints = new ArrayList<>();
                        List<Object> locations = (List<Object>) data.get("routePoints");
                        if(locations!=null) {
                            for (Object locationObj : locations) {
                                Map<String, Object> location = (Map<String, Object>) locationObj;
                                LatLng latLng = new LatLng((Double) location.get("latitude"), (Double) location.get("longitude"));
                                routePoints.add(latLng);
                            }
                        }
//                        routePoints = (List) data.get("routePoints");
                        boolean ans = checkIfMatches(i);
                        if(ans) {
                            flag = 1;
                            try {
                                displayDriver(data.get("driverUid").toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (offSeats - reqSeats == 0) {
                                //remove the ride from the table

                                delDocOfferRides(data.get("driverUid").toString(), rides.get(i).getId());
//                                delDoc(rides.get(i));

                            } else {
                                //update table
                                UpdDocOfferRides(data.get("driverUid").toString(), rides.get(i).getId(), offSeats - reqSeats, offPassenger + reqSeats);
                            }
                            upDocAllRidesDriver(data.get("driverUid").toString(), rides.get(i).getId(),offSeats - reqSeats, data.get("passengerUid"), offPassenger + reqSeats);
                            upDocAllRidesPassenger(data.get("driverUid").toString(), rides.get(i).getId(), reqDate, reqTime, Integer.toString(offSeats - reqSeats), reqSource, reqDest,  Integer.toString(offPassenger + reqSeats));
                            addIdToMatchedRidesTable(rides.get(i).getId(), data.get("driverUid").toString());
                            break;
                        }
                    }
                    if(flag==0){
                        TextView tv;
                        tv = new TextView(getApplicationContext());
                        tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                        tv.setGravity(Gravity.CENTER);
                        tv.setText("No drivers available");
                        myLinearLayout = (LinearLayout) findViewById(R.id.frame);
                        myLinearLayout.addView(tv);
                    }

                }
                else{
                    Toast.makeText(FetchedRidesActivity1.this,"Collection query failed",Toast.LENGTH_SHORT).show();
                    Log.d("hakuna", "Collection query failed");
                }
            }
        });
    }

    private void getTimeAndPath(String result) {
        try {
            timeDiff = new ArrayList();
            timeDiffDest = new ArrayList();
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("rows");
            Log.d("imp", Integer.toString(reqCount));

            for (int i = 0; i < reqCount; i++) {
                JSONObject row = routeArray.getJSONObject(i);
                JSONArray elements = row.getJSONArray("elements");

                JSONObject element = elements.getJSONObject(0);
                JSONObject duration = element.getJSONObject("duration");
                Double temp = duration.getDouble("value");
                Log.d("imp", Double.toString(temp));
                timeDiff.add(temp);

                JSONObject elementDest = elements.getJSONObject(1);
                JSONObject durationDest = elementDest.getJSONObject("duration");
                Double tempDest = durationDest.getDouble("value");
                Log.d("imp", Double.toString(tempDest));
                timeDiffDest.add(tempDest);
            }
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

    private double getApproxDistance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}

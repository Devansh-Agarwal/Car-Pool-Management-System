package com.swe.cpms;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.PolyUtil;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class RideOfferActivity extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    private EditText mDate, mTime, mSeats;
    private AutocompleteSupportFragment mSource;
    private AutocompleteSupportFragment mDestination;
    private Button mOffer;
    private LatLng finalSource=null, finalDest=null;
    String finalDate, finalTime, finalSeats;
    final private FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();
    Polyline polyline;

    List<LatLng> routePoints;

    GoogleMap gmap;
    SupportMapFragment mapFragment;

    private static final int REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_offer);

        //to get current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.dMap);
        mSource = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.dSource);
        mDestination = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.dDestination);
        mDate = (EditText) findViewById(R.id.dDate);
        mTime = (EditText)  findViewById(R.id.dTime);
        mSeats = (EditText) findViewById(R.id.dSeats);
        mOffer = (Button) findViewById(R.id.offer);

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
        }

        mSource.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mSource.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latlng = place.getLatLng();
                finalSource = latlng;
                gmap.addMarker(new MarkerOptions().position(finalSource).title("Source"));
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(RideOfferActivity.this, "select start address", Toast.LENGTH_SHORT).show();
            }
        });

        mDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latlng = place.getLatLng();
                finalDest = latlng;
                gmap.addMarker(new MarkerOptions().position(finalDest).title("Destination"));
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(finalDest, 10));
            }
            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(RideOfferActivity.this, "select end address", Toast.LENGTH_SHORT).show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        //datePicker
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RideOfferActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                        mDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                datePickerDialog.show();
            }
        });

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        //timepicker
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(RideOfferActivity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay+":"+minute;
                        mTime.setText(time);
                    }
                }, hour, minute, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                timePickerDialog.show();
            }
        });

//        handling post ride button
        mOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDate = mDate.getText().toString();
                finalTime = mTime.getText().toString();
                finalSeats = mSeats.getText().toString();

                if(finalSource==null){
                    Toast.makeText(RideOfferActivity.this, "Enter start address", Toast.LENGTH_SHORT).show();
                }
                else if(finalDest==null){
                    Toast.makeText(RideOfferActivity.this, "Enter end address", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(finalDate)){
                    Toast.makeText(RideOfferActivity.this, "Enter date", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(finalTime)){
                    Toast.makeText(RideOfferActivity.this, "Enter time", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(finalSeats)){
                    Toast.makeText(RideOfferActivity.this, "Enter the number of seats", Toast.LENGTH_SHORT).show();
                }
                else{
                    polylineLFunc();

                }
            }
        });
    }

    //gets set of points between src and destination
    private void polylineLFunc() {
        String url= getUrl();
        new connectAsyncTask(url).execute();
    }

    //used for polyline
    private String getUrl() {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+Double.toString(finalSource.latitude)+
                        ","+Double.toString(finalSource.longitude)+"&destination="+Double.toString(finalDest.latitude)+","+
                Double.toString(finalDest.longitude)+"&sensor=false&mode=driving&alternatives=true"+"&key="+getString(R.string.google_api_key);
        return url;
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
            super.onPreExecute();
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
                drawPath(result);
            }
        }
    }

    //used for polyline
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
            return "";
        }
    }

    //used for polyline
    public void drawPath(String result) {
        if (polyline != null) {
            gmap.clear();
        }
        gmap.addMarker(new MarkerOptions().position(finalSource).title("Source"));
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(finalSource, 10));

        gmap.addMarker(new MarkerOptions().position(finalDest).title("destination"));
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(finalDest, 10));

        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");

            String encodedString = overviewPolylines.getString("points");
            routePoints = decodePoly(encodedString);
            addOfferDetailsToDb(finalDate, finalTime, finalSeats, finalSource, finalDest, routePoints);

            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (int z = 0; z < routePoints.size() ; z++) {
//                LatLng src = list.get(z);
//                LatLng dest = list.get(z + 1);
//                polyline = gmap.addPolyline(new PolylineOptions()
//                        .add(new LatLng(src.latitude, src.longitude),
//                                new LatLng(dest.latitude, dest.longitude))
//                        .width(5).color(Color.BLUE).geodesic(true));
                LatLng point = routePoints.get(z);
                options.add(point);
            }
            polyline = gmap.addPolyline(options);

            for (int z = 0; z < routePoints.size() ; z++) {
//                LatLng src = list.get(z);
//                LatLng dest = list.get(z + 1);
//                polyline = gmap.addPolyline(new PolylineOptions()
//                        .add(new LatLng(src.latitude, src.longitude),
//                                new LatLng(dest.latitude, dest.longitude))
//                        .width(5).color(Color.BLUE).geodesic(true));
                LatLng point = routePoints.get(z);
                boolean ans = PolyUtil.isLocationOnEdge(point, routePoints, true);
                if(ans){
                    Log.d("isLoc", Double.toString(z));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //used for polyline
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

    //    wirtes it to db - incomplete
    private void addOfferDetailsToDb(String date, String time, String seats, LatLng finalSource, LatLng finalDest , List routePoints){
//            write it to database

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> ride = new HashMap<>();
        ride.put("date", date);
        ride.put("StartTime", time);
        ride.put("endTime", null);
        ride.put("totalNumberOfSeats", seats);
        ride.put("StartLat", finalSource.latitude);
        ride.put("StartLon", finalSource.longitude);
        ride.put("DestLat", finalDest.latitude);
        ride.put("destLon", finalDest.longitude);
        ride.put("isDriver", true);
        ride.put("driverUid",user_auth.getUid());
        ride.put("passengerUid", null);
        ride.put("numberOfPeople",0);
        ride.put("routePoints", routePoints);
        String rideID = UUID.randomUUID().toString();
        // Add new ride to db
        db.collection("AllRides")
                .document(user_auth.getUid())
                .collection("Rides")
                .document(rideID)
                .set(ride)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("hakuna", "DocumentSnapshot successfully written!");
                        Toast.makeText(RideOfferActivity.this,"Ride successfully offered", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("hakuna", "Error writing document", e);
                        Toast.makeText(RideOfferActivity.this,"Ride storage unsuccessful in AllRides", Toast.LENGTH_SHORT).show();
                    }
                });
        db.collection("OfferedRides")
                .document(rideID)
                .set(ride)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("hakuna", "DocumentSnapshot successfully written!");
                //        Toast.makeText(RideOfferActivity.this,"Ride successfully stored in Offered Rides database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(" hakuna", "Error writing document", e);
                        Toast.makeText(RideOfferActivity.this,"Ride storage unsuccessful in Offered Rides", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //to get current location
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(RideOfferActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap=googleMap;
        if(currentLocation!=null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            googleMap.addMarker(markerOptions);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }
}


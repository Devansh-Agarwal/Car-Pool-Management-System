package com.swe.cpms;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class PassengerTrackingActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String rideID;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_tracking);

        bundle = getIntent().getExtras();

        rideID = bundle.getString("rideID");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.tMap);
        mapFragment.getMapAsync(this);

        //need to fix this
//        while(1){
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            CollectionReference colRef = db.collection("TrackLocation");
//
//
//            colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        QuerySnapshot coll = task.getResult();
//                        List<DocumentSnapshot> rides = coll.getDocuments();
//
//                        String origins = "";
//                        int flag = 0;
////                        reqCount = rides.size();
//                        for (int i = 0; i < rides.size(); i++) {
//                            if(rides.get(i).getId()==rideID ){
//                                Map<String, Object> data = rides.get(i).getData();
//                                boolean track = (boolean) data.get("isDriverSharing");
//                                if(track){
//                                    Double lat = (Double) data.get("latitude");
//                                    Double lng = (Double) data.get("longitude");
//                                    LatLng latLng = new LatLng(lat, lng);
//                                    mMap.addMarker(new MarkerOptions().position(latLng));
//                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//
//                                }
//                            }
//                        }
//                    }
//                }
//            });
//
//
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}

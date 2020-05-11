package com.swe.cpms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DriverTrackingActivity extends Service {
    private static final String TAG = DriverTrackingActivity.class.getSimpleName();
    private static final String NOTIFICATION_CHANNEL_ID_SERVICE = "com.mypackage.service";
    private static final String NOTIFICATION_CHANNEL_ID_INFO = "com.mypackage.download_info";
    //    String rideID = "22fbb568-5ef7-4f83-b41e-0162cc385a7b";
    String rideID;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        rideID = intent.getStringExtra("rideID");
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("track", "activity opened");
        initChannel();
        buildNotification();
        requestLocationUpdates();
//        loginToFirebase();
    }

//Create the persistent notification//

    public void initChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, "App Service", NotificationManager.IMPORTANCE_DEFAULT));
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_INFO, "Download Info", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        Notification.Builder builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID_INFO)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Live Tracking is on")
                .setOngoing(true)
                .setContentIntent(broadcastIntent);
//                .setSmallIcon(R.drawable.tracking_enabled);
//        Notification notification = builder.build();
        startForeground(1, builder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(stopReceiver);

            stopSelf();
            stopForeground(true);
        }
    };

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        Log.d("track", "requestLocationUpdates");

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

            client.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                final Location location = locationResult.getLastLocation();
//Get a reference to the database, so your app can perform read and write operations//

                final CollectionReference collRef= FirebaseFirestore.getInstance().collection("TrackLocation");
                DocumentReference docIdRef = collRef.document(rideID);
                Log.d("track", "creating for the first time");
                Map<String, Object> docData = new HashMap<>();
                docData.put("isDriverSharing", true);
                docData.put("latitude", location.getLatitude());
                docData.put("longitude", location.getLongitude());
                docIdRef.set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("hakuna", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("hakuna", "Error writing document", e);
                    }
                });


            }
        }, null);
    }
}

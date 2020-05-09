package com.swe.cpms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();

    public static void setValue(String key, int value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    static void updateMyActivity(Context context, String message) {

        Intent intent = new Intent("notification");

        //put whatever data you want to send, if any
        intent.putExtra("message", message);

        //send broadcast
        context.sendBroadcast(intent);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("hakuna","This is a test message for notification");
        if(Objects.equals(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), "Upcoming Carpool Ride")) {
            Log.d("hakuna", "onMessageReceived: Upcoming ride +1 executed");
           setValue("upcoming_rides",1,this);
           updateMyActivity(this,"carpool created");

        }
        else if(Objects.equals(remoteMessage.getNotification().getTitle(), "Carpool Ride Ended"))
        {
            Log.d("hakuna", "onMessageReceived: Upcoming ride -1 executed");
          setValue("upcoming_rides",0,this);
            updateMyActivity(this,"carpool ended");
        }
        Log.d("hakuna", Objects.requireNonNull(remoteMessage.getNotification().getTitle()));
        shownotification(remoteMessage.getNotification());
    }



    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
        // add the new token to firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userToken = new HashMap<>();
        userToken.put("token", s);
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
    }

    public void shownotification(RemoteMessage.Notification message){


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.****.*****.***"; //your app package name

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Techrush Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());

    }

}